package de.students.semantic

import de.students
import de.students.Parser.*

object ExpressionChecks {

  def checkExpression(expr: Expression, context: SemanticContext): TypedExpression = {
    expr match {
      case m @ MethodCall(_, _, _, _) => this.checkMethodCallExpression(m, context)
      case a @ ArrayAccess(_, _)      => this.checkArrayAccessExpression(a, context)
      case a @ NewArray(_, _)         => this.checkNewArrayExpression(a, context)
      case n @ NewObject(_, _)        => this.checkNewObjectExpression(n, context)
      case m @ MemberAccess(_, _)     => this.checkMemberAccessExpression(m, context)
      case t @ ThisAccess(_)          => this.checkThisAccessExpression(t, context)
      case u @ UnaryOp(_, _)          => this.checkUnaryOpExpression(u, context)
      case b @ BinaryOp(_, _, _)      => this.checkBinaryOpExpression(b, context)
      case v @ VarRef(_)              => this.checkVarRefExpression(v, context)
      case s @ StaticClassRef(_)      => this.checkStaticClassRefExpression(s, context)
      case l @ Literal(_)             => this.checkLiteralExpression(l, context)
      case _                          => throw new SemanticException(s"Could not match expression $expr")
    }
  }

  private def checkMethodCallExpression(methodCall: MethodCall, context: SemanticContext): TypedExpression = {
    // catch and convert static method calls
    methodCall.target match {
      case VarRef("this") =>
        val currentClass = context.getClassAccessHelper.getBridge.getClass(context.getClassName)
        val method = currentClass.methods.find(m => m.name == methodCall.methodName)
        if (method.nonEmpty && method.get.static) {
          return checkMethodCallExpression(
            MethodCall(
              StaticClassRef(currentClass.name),
              methodCall.methodName,
              methodCall.args,
              true
            ),
            context
          )
        }
      case _ =>
    }

    // determine the type, that the method is called on
    val typedTarget = ExpressionChecks.checkExpression(methodCall.target, context)
    if (!typedTarget.exprType.isInstanceOf[UserType]) {
      throw new SemanticException(
        s"Cannot call method ${methodCall.methodName} on value of type ${typedTarget.exprType}"
      )
    }

    // validate arguments
    val typedArguments = methodCall.args.map(argument => ExpressionChecks.checkExpression(argument, context))
    val argTypes = typedArguments.map(arg => arg.exprType)

    // determine method definition
    val fqClassName = typedTarget.exprType.asInstanceOf[UserType].name

    val isStaticTarget = typedTarget.expr.isInstanceOf[StaticClassRef]
    val callSource = CallSource(isStaticTarget, context)

    val methodType =
      context.getClassAccessHelper.getClassMemberType(
        fqClassName,
        methodCall.methodName,
        Some(argTypes),
        Some(callSource)
      )
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

    // TODO remove expensive method call and do correct None handling
    val isStatic = context.getClassAccessHelper.getClassMethodDecl(
      fqClassName,
      methodCall.methodName,
      Some(argTypes),
      Some(callSource)
    ) match {
      case methodDecl: Some[MethodDecl] => methodDecl.get.static
      case None                         => false
    }

    TypedExpression(
      MethodCall(typedTarget, methodCall.methodName, typedArguments, isStatic),
      methodTypeF.returnType
    )
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
      if (dimType.exprType != IntType) {
        throw new SemanticException(
          s"Array dimensions can only be set to integer sizes. Size of type ${dimType.exprType} is not allowed"
        )
      }
      dimType
    })

    var stackedArrayType: Type = context.simpleTypeToQualified(newArr.arrayType)
    for (typedDim <- typedDimensions) {
      stackedArrayType = ArrayType(stackedArrayType)
    }

    TypedExpression(NewArray(stackedArrayType, typedDimensions), stackedArrayType)
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

  private def checkMemberAccessExpression(memberAccess: MemberAccess, context: SemanticContext): TypedExpression = {
    val typedTarget = ExpressionChecks.checkExpression(memberAccess.target, context)

    val isStatic = typedTarget.exprType match {
      case UserType(name) =>
        context.getClassAccessHelper
          .getClassField(context.getFullyQualifiedClassName(name), memberAccess.memberName, None)
          .get
          .isStatic
      case _ => false
    }

    typedTarget.exprType match {
      case UserType(qualifiedClassName) =>
        val callSource = CallSource(isStatic, context)
        val memberType =
          context.getClassAccessHelper.getClassMemberType(
            qualifiedClassName,
            memberAccess.memberName,
            None,
            Some(callSource)
          )
        TypedExpression(MemberAccess(typedTarget, memberAccess.memberName), memberType)
      case ArrayType(baseType) =>
        val memberType =
          context.getClassAccessHelper.getArrayMemberType(ArrayType(baseType), memberAccess.memberName, None)
        TypedExpression(MemberAccess(typedTarget, memberAccess.memberName), memberType)
      case _ =>
        val iName = if isStatic then "static instance" else "instance"
        throw new SemanticException(
          s"Cannot access member ${memberAccess.memberName} on $iName of type ${typedTarget.exprType}"
        )
    }
  }

  private def checkThisAccessExpression(thisAccess: ThisAccess, context: SemanticContext): TypedExpression = {
    val callSource = CallSource(false, context)
    val memberType =
      context.getClassAccessHelper.getClassMemberType(context.getClassName, thisAccess.name, None, Some(callSource))
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
        val isLeftPrimitive = UnionTypeFinder.isPrimitive(typedLeft.exprType)
        val isRightPrimitive = UnionTypeFinder.isPrimitive(typedRight.exprType)
        if (!isLeftPrimitive || !isRightPrimitive) {
          // at least one is an object
          if (!UnionTypeFinder.isASubtypeOfB(typedLeft.exprType, typedLeft.exprType, context.getClassAccessHelper)) {
            throw new SemanticException(
              s"Cannot assign value of type ${typedRight.exprType} to type ${typedLeft.exprType}"
            )
          }
        } else {
          // both are primitives
          if (UnionTypeFinder.getLargerPrimitive(typedLeft.exprType, typedRight.exprType) != typedLeft.exprType) {
            throw new SemanticException(
              s"Implicit conversion of type ${typedRight.exprType} to type ${typedLeft.exprType} could result in data loss"
            )
          }
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

  private def checkStaticClassRefExpression(
    staticClassRef: StaticClassRef,
    context: SemanticContext
  ): TypedExpression = {
    val qualifiedClassName = context.getFullyQualifiedClassName(staticClassRef.className);
    TypedExpression(StaticClassRef(qualifiedClassName), UserType(qualifiedClassName))
  }

  private def checkVarRefExpression(varRef: VarRef, context: SemanticContext): TypedExpression = {
    val qualifiedClass: Option[String] =
      try Some(context.getFullyQualifiedClassName(varRef.name))
      catch case e => None

    if (qualifiedClass.nonEmpty) {
      // is reference to static class context
      TypedExpression(StaticClassRef(qualifiedClass.get), UserType(qualifiedClass.get))
    } else {
      val typeAssumption = context.getTypeAssumption(varRef.name)
      if (typeAssumption.nonEmpty) {
        TypedExpression(varRef, typeAssumption.get)
      } else {
        val currentClass = context.getClassAccessHelper.getBridge.getClass(context.getClassName)
        // check if this var is an implicit this.<varName>
        if (currentClass.fields.exists(f => f.name == varRef.name)) {
          ExpressionChecks.checkExpression(
            ThisAccess(varRef.name),
            context
          )
        } else {
          throw new SemanticException(s"Identifier ${varRef.name} is not defined")
        }
      }
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
