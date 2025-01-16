package de.students.semantic

import de.students.Parser.*


object ExpressionChecks {

  def checkExpression(expr: Expression, context: SemanticContext): TypedExpression = {
    expr match {
      case m@MethodCall(_, _, _) => this.checkMethodCallExpression(m, context)
      case a@ArrayAccess(_, _) => this.checkArrayAccessExpression(a, context)
      case a@NewArray(_, _) => this.checkNewArrayExpression(a, context)
      case n@NewObject(_, _) => this.checkNewObjectExpression(n, context)
      case c@ClassAccess(_, _) => this.checkClassAccessExpression(c, context)
      case t@ThisAccess(_) => this.checkThisAccessExpression(t, context)
      case b@BinaryOp(_, _, _) => this.checkBinaryOpExpression(b, context)
      case v@VarRef(_) => this.checkVarRefExpression(v, context)
      case l@Literal(_) => this.checkLiteralExpression(l, context)
      case _ => throw new SemanticException(s"Could not match expression $expr")
    }
  }

  private def checkMethodCallExpression(methodCall: MethodCall, context: SemanticContext): TypedExpression = {
    // determine the type, that the method is called on
    val typedTarget = ExpressionChecks.checkExpression(methodCall.target, context)
    if (!typedTarget.exprType.isInstanceOf[UserType]) {
      throw new SemanticException(s"Cannot call method ${methodCall.methodName} on value of type ${typedTarget.exprType}")
    }

    // determine method definition
    val fullyQualifiedMethodName = context.getFullyQualifiedMemberName(methodCall.methodName,
      Some(typedTarget.exprType.asInstanceOf[UserType].name)
    )
    val methodTypeOption = context.getTypeAssumption(fullyQualifiedMethodName)
    if (methodTypeOption.isEmpty) {
      throw new SemanticException(s"Cannot call undefined method $fullyQualifiedMethodName")
    }
    else if (!methodTypeOption.get.isInstanceOf[FunctionType]) {
      throw new SemanticException(s"Cannot call value of type ${methodTypeOption.get} as a function")
    }
    val methodType = methodTypeOption.get.asInstanceOf[FunctionType]

    // validate arguments
    val typedArguments = methodCall.args.map(argument => ExpressionChecks.checkExpression(argument, context))

    // check if number of arguments matches number of parameters
    val expectedArgs = methodType.parameterTypes.length
    val providedArgs = typedArguments.length
    if (expectedArgs != providedArgs) {
      throw new SemanticException(s"Wrong number of arguments provided for method $fullyQualifiedMethodName ($expectedArgs expected, but ${providedArgs} found)")
    }

    // check if arguments have the correct types
    methodType.parameterTypes.zip(typedArguments.map(a => a.exprType)).foreach((parameterType, argumentType) => {
      if (!UnionTypeFinder.isASubtypeOfB(parameterType, argumentType, context)) {
        throw new SemanticException(s"Wrong number of arguments provided for method $fullyQualifiedMethodName ($expectedArgs expected, but ${providedArgs} found)")
      }
    })

    TypedExpression(MethodCall(typedTarget, methodCall.methodName, typedArguments), methodType.returnType)
  }

  private def checkArrayAccessExpression(arrayAccess: ArrayAccess, context: SemanticContext): TypedExpression = {
    val typedArray = ExpressionChecks.checkExpression(arrayAccess.array, context)
    val typedIndex = ExpressionChecks.checkExpression(arrayAccess.index, context)

    typedArray.exprType match {
      case ArrayType(baseType) =>
        typedIndex.exprType match {
          case IntType => TypedExpression(ArrayAccess(typedArray, typedIndex), baseType)
          case _ => throw new SemanticException(s"Array can only be indexed with integer values, but encountered array access with type ${typedIndex.exprType}")
        }
      case _ => throw new SemanticException(s"Cannot access value of type ${typedArray.exprType} with array indexing")
    }
  }

  private def checkNewArrayExpression(newArr: NewArray, context: SemanticContext): TypedExpression = {
    val typedDimensions = newArr.dimensions.map(dimensionExpression => {
      val dimType = ExpressionChecks.checkExpression(dimensionExpression, context)
      if (dimType != IntType) {
        throw new SemanticException(s"Array dimensions can only be set to integer sizes. Size of type ${dimType.exprType} is not allowed")
      }
      dimType
    })

    var stackedArrayType: Type = newArr.arrayType match {
      case UserType(clsName) => UserType(context.getFullyQualifiedClassName(clsName))
      case _ => newArr.arrayType
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

    // TODO: check if there is a matching constructor for the class

    TypedExpression(NewObject(fullyQualifiedClassName, typedArguments), UserType(fullyQualifiedClassName))
  }

  private def checkClassAccessExpression(clsAccess: ClassAccess, context: SemanticContext): TypedExpression = {
    val fullyQualifiedClassName = context.getFullyQualifiedClassName(clsAccess.className)
    val fullyQualifiedMemberName = context.getFullyQualifiedMemberName(clsAccess.memberName, Some(fullyQualifiedClassName))
    val memberTypeOption = context.getTypeAssumption(fullyQualifiedMemberName)

    memberTypeOption match {
      case Some(memberType) => TypedExpression(ClassAccess(fullyQualifiedClassName, clsAccess.memberName), memberType)
      case None => throw new SemanticException(s"Referenced class member $fullyQualifiedMemberName is not defined")
    }
  }

  private def checkThisAccessExpression(thisAccess: ThisAccess, context: SemanticContext): TypedExpression = {
    val fullyQualifiedMember = context.getFullyQualifiedMemberName(thisAccess.name)
    val memberTypeOption = context.getTypeAssumption(fullyQualifiedMember)

    memberTypeOption match {
      case Some(memberType) => TypedExpression(thisAccess, memberType)
      case None => throw new SemanticException(s"Referenced class member $fullyQualifiedMember is not defined")
    }
  }

  private def checkBinaryOpExpression(binOp: BinaryOp, context: SemanticContext): TypedExpression = {
    val typedLeft = ExpressionChecks.checkExpression(binOp.left, context)
    val typedRight = ExpressionChecks.checkExpression(binOp.right, context)

    // TODO: check if binOp.op is defined for the types of the left and right expressions
    val unionType = UnionTypeFinder.getUnion(typedLeft.exprType, typedRight.exprType, context)
    TypedExpression(BinaryOp(typedLeft, binOp.op, typedRight), unionType)
  }

  private def checkVarRefExpression(varRef: VarRef, context: SemanticContext): TypedExpression = {
    val varTypeOption = context.getTypeAssumption(varRef.name)
    varTypeOption match {
      case Some(varType) => TypedExpression(varRef, varType)
      case None => throw new SemanticException(s"Identifier ${varRef.name} is not defined")
    }
  }

  private def checkLiteralExpression(literal: Literal, context: SemanticContext): TypedExpression = {
    literal.value match {
      case _: Boolean => TypedExpression(literal, BoolType)
      case _: Int => TypedExpression(literal, IntType)
      case _: String => throw SemanticException("String is not yet implemented as a type")
      case _ => throw SemanticException(s"Unknown literal: $literal")
    }
  }


}