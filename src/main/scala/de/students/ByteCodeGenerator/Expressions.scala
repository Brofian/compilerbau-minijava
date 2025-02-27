package de.students.ByteCodeGenerator

import org.objectweb.asm.{MethodVisitor, Label}
import org.objectweb.asm.Opcodes.*

import de.students.Parser.*

private def generateExpression(expression: Expression, state: MethodGeneratorState): Unit = {
  val stringified = stringifyExpression(expression)
  debugLogStack(state, f"eval expr $stringified")

  expression match {
    case TypedExpression(variableReference: VarRef, _) =>
      generateVariableReference(variableReference, state)
    case TypedExpression(literal: Literal, t) =>
      generateLiteral(literal, t, state)
    case TypedExpression(binaryOperation: BinaryOp, _) =>
      generateBinaryOperation(binaryOperation, state)
    case TypedExpression(methodCall: MethodCall, returnType) =>
      generateMethodCall(methodCall, returnType, state)
    case TypedExpression(newObject: NewObject, _) =>
      generateNewObject(newObject, state)
    case TypedExpression(thisAccess: ThisAccess, fieldType) =>
      generateThisRValue(thisAccess, fieldType, state)
    case TypedExpression(memberAccess: MemberAccess, fieldType) =>
      generateClassRValue(memberAccess, fieldType, state)
    case TypedExpression(newArray: NewArray, arrayType) =>
      generateNewArray(newArray, state)
    case TypedExpression(arrayAccess: ArrayAccess, arrayType) =>
      generateArrayRValue(arrayAccess, arrayType, state)
    case typedExpression: TypedExpression =>
      throw ByteCodeGeneratorException(
        f"did not expect raw typed expression ($typedExpression), this may indicate a bug in the code generator"
      )
    case _ => throw ByteCodeGeneratorException(f"the expression $expression is not supported")
  }

  debugLogStack(state, f"end of expression $stringified")
}

// VARIABLE REFERENCE
private def generateVariableReference(varRef: VarRef, state: MethodGeneratorState): Unit = {
  debugLogStack(state, "start var ref")

  if (varRef.name == "this") {
    Instructions.loadThis(state)
  } else {

    val variableInfo = state.getVariable(varRef.name)
    if (variableInfo.isField) {
      Instructions.loadField(varRef.name, variableInfo.t, state)
    } else {
      val varInfo = state.getVariable(varRef.name)
      Instructions.loadVar(varInfo.id, varInfo.t, state)
    }

  }

  debugLogStack(state, "end var ref")
}

// STATIC CLASS REFERENCE
private def generateRValueStaticClassMemberReference(
  staticClassRef: StaticClassRef,
  classType: Type,
  memberName: String,
  memberType: Type,
  state: MethodGeneratorState
): Unit = {
  Instructions.loadStaticClassMember(staticClassRef.className, memberName, memberType, state)
}

private def generateLValueStaticClassMemberReference(
  staticClassRef: StaticClassRef,
  classType: Type,
  memberName: String,
  memberType: Type,
  rvalue: Expression,
  state: MethodGeneratorState
): Unit = {
  generateExpression(rvalue, state)

  Instructions.duplicateTop(state)

  Instructions.storeStaticClassMember(staticClassRef.className, memberName, memberType, state)
}

// STATIC METHOD CALL
private def generateStaticMethodCall(
  staticClass: StaticClassRef,
  classType: Type,
  methodCall: MethodCall,
  returnType: Type,
  state: MethodGeneratorState
): Unit = {
  val parameterTypes = methodCall.args.map(expr => generateTypedExpression(expr.asInstanceOf[TypedExpression], state))
  val methodDescriptor = javaSignature(FunctionType(returnType, parameterTypes))
  Instructions.callStaticMethod(
    javaifyClass(staticClass.className),
    methodCall.methodName,
    methodCall.args.size,
    methodDescriptor,
    state
  )
  Instructions.callMethod(asmUserType(classType), methodCall.methodName, methodCall.args.size, methodDescriptor, state)
}

// LITERAL
private def generateLiteral(literal: Literal, literalType: Type, state: MethodGeneratorState): Unit = {
  Instructions.pushConstant(literal.value, literalType, state)

  debugLogStack(state, f"pushed $literal")
}

// BINARY OPERATION
private def generateBinaryOperation(operation: BinaryOp, state: MethodGeneratorState): Unit = {
  debugLogStack(state, f"start bin op ${operation.op}")

  if (operation.op == "=") {
    generateAssignment(operation.left, operation.right, state)
  } else if (isBooleanOpcode(operation.op)) {
    generateBooleanOperation(operation, state)
  } else {
    val expressionType = generateTypedExpression(operation.left.asInstanceOf[TypedExpression], state)
    generateExpression(operation.right, state) // should be same type, this is not our problem if not

    val opcode = binaryOpcode(operation.op, expressionType)
    Instructions.binaryOperation(opcode, state)
  }

  debugLogStack(state, f"end bin op ${operation.op}")
}

// BOOLEAN OPERATION
private def generateBooleanOperation(operation: BinaryOp, state: MethodGeneratorState): Unit = {
  // && and || need special treatment
  if (operation.op == "&&") {
    val rightEval = Label()
    val falsePush = Label()
    val end = Label()

    generateExpression(operation.left, state)
    Instructions.condJump(IFNE, rightEval, state)
    Instructions.goto(falsePush, state)
    Instructions.visitLabel(rightEval, state)
    generateExpression(operation.right, state)
    Instructions.condJump(IFEQ, falsePush, state)
    Instructions.pushTrue(state)
    Instructions.goto(end, state)
    Instructions.visitLabel(falsePush, state)
    Instructions.pushFalse(state)
    Instructions.visitLabel(end, state)

    // the stack counter has to be manually decreased to account for branching
    state.popStack()
  } else if (operation.op == "||") {
    val truePush = Label()
    val end = Label()

    generateExpression(operation.left, state)
    Instructions.condJump(IFNE, truePush, state)
    generateExpression(operation.right, state)
    Instructions.condJump(IFNE, truePush, state)
    Instructions.pushFalse(state)
    Instructions.goto(end, state)
    Instructions.visitLabel(truePush, state)
    Instructions.pushTrue(state)
    Instructions.visitLabel(end, state)

    // the stack counter has to be manually decreased to account for branching
    state.popStack()
  } else {
    val ifInsn = operation.op match {
      case "==" => IFEQ
      case "!=" => IFNE
      case "<"  => IFLT
      case "<=" => IFLE
      case ">"  => IFGT
      case ">=" => IFGE
      case _    => throw ByteCodeGeneratorException(f"the operator ${operation.op} is not allowed as boolean operation")
    }

    val truePush = Label()
    val end = Label()

    generateExpression(operation.left, state)
    generateExpression(operation.right, state)
    Instructions.condJump(ifInsn, truePush, state)
    Instructions.pushFalse(state)
    Instructions.goto(end, state)
    Instructions.visitLabel(truePush, state)
    Instructions.pushTrue(state)
    Instructions.visitLabel(end, state)
  }
}

// ASSIGNMENT
private def generateAssignment(lvalue: Expression, rvalue: Expression, state: MethodGeneratorState): Unit = {
  lvalue match {
    case VarRef(name)                                       => generateVariableAccess(name, rvalue, state)
    case TypedExpression(VarRef(name), _)                   => generateVariableAccess(name, rvalue, state)
    case TypedExpression(thisAccess: ThisAccess, fieldType) => generateThisLValue(thisAccess, fieldType, rvalue, state)
    case TypedExpression(memberAccess: MemberAccess, fieldType) =>
      generateClassLValue(memberAccess, fieldType, rvalue, state)
    case TypedExpression(arrayAccess: ArrayAccess, arrayType) =>
      generateArrayLValue(arrayAccess, arrayType, rvalue, state)
    case _ => throw ByteCodeGeneratorException(f"lvalue expected, instead got $lvalue")
  }
}

/**
 * sets the variable or field to the evaluated rvalue
 * @param varName name of a local variable or field of this
 * @param rvalue
 * @param state
 */
private def generateVariableAccess(varName: String, rvalue: Expression, state: MethodGeneratorState): Unit = {
  val variableInfo = state.getVariable(varName)
  if (variableInfo.isField) {
    generateExpression(rvalue, state)
    Instructions.loadThis(state)

    Instructions.duplicateTopTwo(state) // val | this | val | this
    Instructions.pop(state) // val | this | val

    Instructions.storeField(varName, variableInfo.t, state)
  } else {
    generateExpression(rvalue, state)

    Instructions.duplicateTopType(state)

    Instructions.storeVar(variableInfo.id, variableInfo.t, state)
  }
}

// TYPED EXPRESSION
private def generateTypedExpression(expression: TypedExpression, state: MethodGeneratorState): Type = {
  generateExpression(TypedExpression(expression.expr, expression.exprType), state) // TODO refactor generateExpression
  expression.exprType
}

// METHOD CALL
private def generateMethodCall(methodCall: MethodCall, returnType: Type, state: MethodGeneratorState): Unit = {
  // TODO refactor _ case into own function
  methodCall.target match {
    case TypedExpression(staticClassRef: StaticClassRef, classType: Type) =>
      generateStaticMethodCall(staticClassRef, classType, methodCall, returnType, state)
    case _ => {
      val classType = generateTypedExpression(methodCall.target.asInstanceOf[TypedExpression], state)
      val parameterTypes =
        methodCall.args.map(expr => generateTypedExpression(expr.asInstanceOf[TypedExpression], state))
      val methodDescriptor = javaSignature(FunctionType(returnType, parameterTypes))
      Instructions.callMethod(
        asmUserType(classType),
        methodCall.methodName,
        methodCall.args.size,
        methodDescriptor,
        state
      )
    }
  }
  // virtual element
  Instructions.pushConstant(0, IntType, state)
}

// NEW OBJECT
private def generateNewObject(newObject: NewObject, state: MethodGeneratorState): Unit = {
  Instructions.newObject(newObject.className, state)
  Instructions.duplicateTop(state)
  newObject.arguments.foreach(expr => generateExpression(expr, state))
  Instructions.callConstructor(
    newObject.className,
    newObject.arguments.map(expr => expr.asInstanceOf[TypedExpression].exprType),
    state
  )
}

// THIS ACCESS
/**
 * set field of this to rvalue and leave value of rvalue on stack
 * @param access
 * @param fieldType
 * @param rvalue
 * @param state
 */
private def generateThisLValue(
  access: ThisAccess,
  fieldType: Type,
  rvalue: Expression,
  state: MethodGeneratorState
): Unit = {
  generateExpression(rvalue, state)
  Instructions.loadThis(state)

  Instructions.duplicateTopTwo(state) // val | this | val | this
  Instructions.pop(state) // val | this | val

  Instructions.storeField(access.name, fieldType, state)
}

/**
 * push field of this on stack
 * @param access
 * @param fieldType
 * @param state
 */
private def generateThisRValue(access: ThisAccess, fieldType: Type, state: MethodGeneratorState): Unit = {
  Instructions.loadThis(state)
  Instructions.loadField(access.name, fieldType, state)
}

// CLASS ACCESS
/**
 * push object saved as field or local variable under className on stack
 * @param className
 * @param state
 */
private def loadLValueObject(className: String, state: MethodGeneratorState): Unit = {
  val variableInfo = state.getVariable(className)
  if (variableInfo.isField) {
    Instructions.loadThis(state)
    Instructions.loadField(className, variableInfo.t, state)
  } else {
    Instructions.loadVar(variableInfo.id, variableInfo.t, state)
  }
}

/**
 * set field of object given in classAccess to rvalue and leave value of rvalue on the stack
 * @param memberAccess
 * @param fieldType
 * @param rvalue
 * @param state
 */
private def generateClassLValue(
  memberAccess: MemberAccess,
  fieldType: Type,
  rvalue: Expression,
  state: MethodGeneratorState
): Unit = {
  // TODO refactor _ case into own function
  memberAccess.target match {
    case TypedExpression(staticClassRef: StaticClassRef, classType: Type) =>
      generateLValueStaticClassMemberReference(
        staticClassRef,
        classType,
        memberAccess.memberName,
        fieldType,
        rvalue,
        state
      )
    case _ => {
      generateExpression(memberAccess.target, state)

      generateExpression(rvalue, state)

      Instructions.duplicateTopTwo(state) // val | object | val | object
      Instructions.pop(state) // val | object | val

      Instructions.storeField(memberAccess.memberName, fieldType, state)
    }
  }
}

/**
 * push field of object given in classAccess on stack
 * @param memberAccess
 * @param fieldType
 * @param state
 */
private def generateClassRValue(memberAccess: MemberAccess, fieldType: Type, state: MethodGeneratorState): Unit = {
  // TODO refactor _ case into own function
  memberAccess.target match {
    case TypedExpression(staticClassRef: StaticClassRef, classType: Type) =>
      generateRValueStaticClassMemberReference(staticClassRef, classType, memberAccess.memberName, fieldType, state)
    case _ => {
      generateExpression(memberAccess.target, state)
      Instructions.loadField(memberAccess.memberName, fieldType, state)
    }
  }
}

private def generateNewArray(array: NewArray, state: MethodGeneratorState): Unit = {

  generateExpression(array.dimensions.head, state)
  Instructions.newArray(javaSignature(array.arrayType), state)
}

private def generateArrayLValue(
  arrayAccess: ArrayAccess,
  arrayType: Type,
  rvalue: Expression,
  state: MethodGeneratorState
): Unit = {
  generateExpression(arrayAccess.array, state)
  generateExpression(arrayAccess.index, state)
  generateExpression(rvalue, state)

  Instructions.storeArray(arrayType, state)
}

private def generateArrayRValue(
  arrayAccess: ArrayAccess,
  arrayType: Type,
  state: MethodGeneratorState
): Unit = {
  generateExpression(arrayAccess.array, state)
  generateExpression(arrayAccess.index, state)
  Instructions.accessArray(arrayType, state)
}
