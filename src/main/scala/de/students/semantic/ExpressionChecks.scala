package de.students.semantic

import de.students
import de.students.Parser.*

object ExpressionChecks {

  def checkExpression(expr: Expression, context: SemanticContext): TypedExpression = {
    expr match {
      case m @ MethodCall(_, _, _) => this.checkMethodCallExpression(m, context)
      case a @ ArrayAccess(_, _)   => this.checkArrayAccessExpression(a, context)
      case a @ NewArray(_, _)      => this.checkNewArrayExpression(a, context)
      case n @ NewObject(_, _)     => this.checkNewObjectExpression(n, context)
      case c @ ClassAccess(_, _)   => this.checkClassAccessExpression(c, context)
      case t @ ThisAccess(_)       => this.checkThisAccessExpression(t, context)
      case u @ UnaryOp(_, _)       => this.checkUnaryOpExpression(u, context)
      case b @ BinaryOp(_, _, _)   => this.checkBinaryOpExpression(b, context)
      case v @ VarRef(_)           => this.checkVarRefExpression(v, context)
      case l @ Literal(_)          => this.checkLiteralExpression(l, context)
      case _                       => throw new SemanticException(s"Could not match expression $expr")
    }
  }

  private def checkMethodCallExpression(methodCall: MethodCall, context: SemanticContext): TypedExpression = {
    // determine the type, that the method is called on
    val typedTarget = ExpressionChecks.checkExpression(methodCall.target, context)
    if (!typedTarget.exprType.isInstanceOf[UserType]) {
      throw new SemanticException(
        s"Cannot call method ${methodCall.methodName} on value of type ${typedTarget.exprType}"
      )
    }
    val isStaticTarget = typedTarget.expr.isInstanceOf[StaticClassRef]

    // validate arguments
    val typedArguments = methodCall.args.map(argument => ExpressionChecks.checkExpression(argument, context))
    val argTypes = typedArguments.map(arg => arg.exprType)

    // determine method definition
    val fqClassName = typedTarget.exprType.asInstanceOf[UserType].name
    // TODO: if isStaticTarget == true, then filter for static members only!
    val methodType = context.getClassAccessHelper.getClassMemberType(fqClassName, methodCall.methodName, Some(argTypes))
    if (!methodType.isInstanceOf[FunctionType]) {
      throw new SemanticException(
        s"Cannot call member ${methodCall.methodName} of type $methodType as method with parameters of types $argTypes"
      )
    }
    val methodTypeF = methodType.asInstanceOf[FunctionType]

    // check if number of arguments matches number of parameters
    val expectedArgs = methodTypeF.parameterTypes.length
    val providedArgs = typedArguments.length
    if (expectedArgs != providedArgs) {
      throw new SemanticException(
        s"Wrong number of arguments provided for method ${methodCall.methodName} in $fqClassName ($expectedArgs expected, but $providedArgs found)"
      )
    }

    // check if arguments have the correct types
    methodTypeF.parameterTypes
      .zip(typedArguments.map(a => a.exprType))
      .foreach((parameterType, argumentType) => {
        if (!UnionTypeFinder.isASubtypeOfB(parameterType, argumentType, context.getClassAccessHelper)) {
          throw new SemanticException(s"Value of type $argumentType cannot be used for argument of type $parameterType")
        }
      })

    TypedExpression(MethodCall(typedTarget, methodCall.methodName, typedArguments), methodTypeF.returnType)
  }

  private def checkArrayAccessExpression(arrayAccess: ArrayAccess, context: SemanticContext): TypedExpression = {
    val typedArray = ExpressionChecks.checkExpression(arrayAccess.array, context)
    val typedIndex = ExpressionChecks.checkExpression(arrayAccess.index, context)

    typedArray.exprType match {
      case ArrayType(baseType) =>
        typedIndex.exprType match {
          case ByteType  => TypedExpression(ArrayAccess(typedArray, typedIndex), baseType)
          case CharType  => TypedExpression(ArrayAccess(typedArray, typedIndex), baseType)
          case ShortType => TypedExpression(ArrayAccess(typedArray, typedIndex), baseType)
          case IntType   => TypedExpression(ArrayAccess(typedArray, typedIndex), baseType)
          case _ =>
            throw new SemanticException(
              s"Array can only be indexed with integer values or smaller, but encountered array access with type ${typedIndex.exprType}"
            )
        }
      case _ => throw new SemanticException(s"Cannot access value of type ${typedArray.exprType} with array indexing")
    }
  }

  private def checkNewArrayExpression(newArr: NewArray, context: SemanticContext): TypedExpression = {
    val typedDimensions = newArr.dimensions.map(dimensionExpression => {
      val dimType = ExpressionChecks.checkExpression(dimensionExpression, context)
      if (dimType != IntType) {
        throw new SemanticException(
          s"Array dimensions can only be set to integer sizes. Size of type ${dimType.exprType} is not allowed"
        )
      }
      dimType
    })

    var stackedArrayType: Type = newArr.arrayType match {
      case UserType(clsName) => UserType(context.getFullyQualifiedClassName(clsName))
      case _                 => newArr.arrayType
    }
    for (typedDim <- typedDimensions) {
      stackedArrayType = ArrayType(stackedArrayType)
    }

    TypedExpression(NewArray(newArr.arrayType, typedDimensions), stackedArrayType)
  }

  private def checkNewObjectExpression(newObj: NewObject, context: SemanticContext): TypedExpression = {
    val fullyQualifiedClassName = context.getFullyQualifiedClassName(newObj.className)

    val typedArguments = newObj.arguments.map(argument => {
      ExpressionChecks.checkExpression(argument, context)
    })
    val argTypes = typedArguments.map(arg => arg.exprType)

    // check if a matching constructor exists
    if (!context.getClassAccessHelper.checkClassConstructorWithParameters(fullyQualifiedClassName, argTypes)) {
      throw new SemanticException(
        s"Could not find constructor for class $fullyQualifiedClassName with parameters $argTypes"
      )
    }

    TypedExpression(NewObject(fullyQualifiedClassName, typedArguments), UserType(fullyQualifiedClassName))
  }

  private def checkClassAccessExpression(clsAccess: ClassAccess, context: SemanticContext): TypedExpression = {
    val varTypeAssumption = context.getTypeAssumption(clsAccess.className)
    if (varTypeAssumption.nonEmpty) {
      varTypeAssumption.get match {
        case UserType(fullyQualifiedClassName) =>
          val memberType =
            context.getClassAccessHelper.getClassMemberType(fullyQualifiedClassName, clsAccess.memberName, None)
          TypedExpression(ClassAccess(fullyQualifiedClassName, clsAccess.memberName), memberType)
        case _ =>
          throw new SemanticException(
            s"Cannot access member ${clsAccess.memberName} on instance of type ${varTypeAssumption.get}"
          )
      }
    } else {
      val fullyQualifiedClassName = context.getFullyQualifiedClassName(clsAccess.className)
      val memberType =
        context.getClassAccessHelper.getClassMemberType(fullyQualifiedClassName, clsAccess.memberName, None)
      TypedExpression(ClassAccess(fullyQualifiedClassName, clsAccess.memberName), memberType)
    }
  }

  private def checkThisAccessExpression(thisAccess: ThisAccess, context: SemanticContext): TypedExpression = {
    val memberType = context.getClassAccessHelper.getClassMemberType(context.getClassName, thisAccess.name, None)
    TypedExpression(thisAccess, memberType)
  }

  private def checkUnaryOpExpression(unaryOp: UnaryOp, context: SemanticContext): TypedExpression = {
    val typedExpr = ExpressionChecks.checkExpression(unaryOp.expr, context);

    unaryOp.op match {
      case "!" =>
        if typedExpr.exprType != BoolType then
          throw new SemanticException(s"Unary operator \"${unaryOp.op}\" is only allowed for data type $BoolType")
      case "-" =>
        if !List(CharType, ByteType, ShortType, IntType, LongType, FloatType, DoubleType).contains(typedExpr.exprType)
        then
          throw new SemanticException(
            s"Unary operator \"${unaryOp.op}\" is not allowed for data type ${typedExpr.exprType}"
          )
      case _ => throw new SemanticException(s"Unknown unary operator \"${unaryOp.op}\"")
    }

    TypedExpression(UnaryOp(unaryOp.op, typedExpr), typedExpr.exprType)
  }

  private def checkBinaryOpExpression(binOp: BinaryOp, context: SemanticContext): TypedExpression = {
    val typedLeft = ExpressionChecks.checkExpression(binOp.left, context)
    val typedRight = ExpressionChecks.checkExpression(binOp.right, context)

    def restrictToPrimitive = (op: String, t1: Type, t2: Type, result: Type) => {
      if (
        t1.isInstanceOf[UserType] || t2.isInstanceOf[UserType] ||
        t1.isInstanceOf[ArrayType] || t2.isInstanceOf[ArrayType]
      ) {
        throw new SemanticException(s"Operator $op cannot be used with values of type $t1 and $t2")
      }
      result
    }

    val opType: Type = binOp.op match {
      case "&&" => restrictToPrimitive("&&", typedLeft.exprType, typedRight.exprType, BoolType)
      case "||" => restrictToPrimitive("||", typedLeft.exprType, typedRight.exprType, BoolType)
      case "<"  => restrictToPrimitive("<", typedLeft.exprType, typedRight.exprType, BoolType)
      case ">"  => restrictToPrimitive(">", typedLeft.exprType, typedRight.exprType, BoolType)
      case "<=" => restrictToPrimitive("<=", typedLeft.exprType, typedRight.exprType, BoolType)
      case ">=" => restrictToPrimitive(">=", typedLeft.exprType, typedRight.exprType, BoolType)
      case "!=" => BoolType
      case "==" => BoolType
      case "+"  => UnionTypeFinder.getLargerPrimitive(typedLeft.exprType, typedRight.exprType)
      case "-"  => UnionTypeFinder.getLargerPrimitive(typedLeft.exprType, typedRight.exprType)
      case "*"  => UnionTypeFinder.getLargerPrimitive(typedLeft.exprType, typedRight.exprType)
      case "/"  => UnionTypeFinder.getLargerPrimitive(typedLeft.exprType, typedRight.exprType)
      case "%"  => UnionTypeFinder.getLargerPrimitive(typedLeft.exprType, typedRight.exprType)
      case "+=" => typedLeft.exprType
      case "-=" => typedLeft.exprType
      case "*=" => typedLeft.exprType
      case "/=" => typedLeft.exprType
      case "%=" => typedLeft.exprType
      case "=" =>
        if (UnionTypeFinder.getLargerPrimitive(typedLeft.exprType, typedRight.exprType) != typedLeft.exprType) {
          throw new SemanticException(
            s"Implicit conversion of type ${typedRight.exprType} to type ${typedLeft.exprType} could result in data loss"
          )
        }
        typedLeft.exprType
      case _ =>
        throw new SemanticException(
          s"Binary operator ${binOp.op} is not defined for types ${typedLeft.exprType} and ${typedRight.exprType}"
        )
    }

    // if this is an assignment calculation operator, we unbox it into its full form: a += b * c => a = a + (b * c)
    val expandedOp: Expression = binOp.op match {
      case "+=" => BinaryOp(typedLeft, "=", TypedExpression(BinaryOp(typedLeft, "+", typedRight), opType))
      case "-=" => BinaryOp(typedLeft, "=", TypedExpression(BinaryOp(typedLeft, "-", typedRight), opType))
      case "*=" => BinaryOp(typedLeft, "=", TypedExpression(BinaryOp(typedLeft, "*", typedRight), opType))
      case "/=" => BinaryOp(typedLeft, "=", TypedExpression(BinaryOp(typedLeft, "/", typedRight), opType))
      case "%=" => BinaryOp(typedLeft, "=", TypedExpression(BinaryOp(typedLeft, "%", typedRight), opType))
      case _    => BinaryOp(typedLeft, binOp.op, typedRight)
    }

    TypedExpression(expandedOp, opType)
  }

  private def checkVarRefExpression(varRef: VarRef, context: SemanticContext): TypedExpression = {
    val varTypeOption = context.getTypeAssumption(varRef.name)
    varTypeOption match {
      case Some(varType) => TypedExpression(varRef, varType)
      case None =>
        try {
          // check if this is not a variable, but a class
          val fullyQualifiedClassName = context.getFullyQualifiedClassName(varRef.name)
          TypedExpression(StaticClassRef(fullyQualifiedClassName), UserType(fullyQualifiedClassName))
        } catch case e: SemanticException => throw new SemanticException(s"Identifier ${varRef.name} is not defined")
    }
  }

  private def checkLiteralExpression(literal: Literal, context: SemanticContext): TypedExpression = {
    literal.value match {
      case _: Boolean => TypedExpression(literal, BoolType)
      case _: Int     => TypedExpression(literal, IntType)
      case _: Short   => TypedExpression(literal, ShortType)
      case _: Long    => TypedExpression(literal, LongType)
      case _: Char    => TypedExpression(literal, CharType)
      case _: Byte    => TypedExpression(literal, ByteType)
      case _: Double  => TypedExpression(literal, DoubleType)
      case _: Float   => TypedExpression(literal, FloatType)
      case _: String =>
        TypedExpression(
          literal,
          UserType("java.lang.String")
        ) /* throw SemanticException("String is not yet implemented as a type") */
      case _ => throw SemanticException(s"Unknown literal: $literal")
    }
  }

}
