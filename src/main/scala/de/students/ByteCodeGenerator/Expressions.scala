package de.students.ByteCodeGenerator

import org.objectweb.asm.{MethodVisitor, Label}
import org.objectweb.asm.Opcodes.*

import de.students.Parser.*

private def generateExpression(expression: Expression, state: MethodGeneratorState): Unit = {
  debugLogStack(state, f"eval expr $expression")

  expression match {
    case TypedExpression(variableReference: VarRef, _) =>
      generateVariableReference(variableReference, state)
    case TypedExpression(literal: Literal, _) =>
      generateLiteral(literal, state)
    case TypedExpression(binaryOperation: BinaryOp, _) =>
      generateBinaryOperation(binaryOperation, state)
    case TypedExpression(methodCall: MethodCall, returnType) =>
      generateMethodCall(methodCall, returnType, state)
    case TypedExpression(newObject: NewObject, _) =>
      generateNewObject(newObject, state)
    case TypedExpression(thisAccess: ThisAccess, fieldType) =>
      generateThisRValue(thisAccess, fieldType, state)
    case TypedExpression(classAccess: ClassAccess, fieldType) =>
      generateClassRValue(classAccess, fieldType, state)
    case typedExpression: TypedExpression =>
      throw ByteCodeGeneratorException("did not expect raw typed expression, this may indicate a bug in the code generator")
    case _ => throw ByteCodeGeneratorException(f"the expression $expression is not supported")
  }
}

// VARIABLE REFERENCE
private def generateVariableReference(varRef: VarRef, state: MethodGeneratorState): Unit = {
  debugLogStack(state, "start var ref")

  val variableInfo = state.getVariable(varRef.name)
  if (variableInfo.isField) {
    Instructions.loadField(varRef.name, variableInfo.t, state)
  } else {
    val varInfo = state.getVariable(varRef.name)
    Instructions.loadVar(varInfo.id, varInfo.t, state)
  }

  debugLogStack(state, "end var ref")
}

// LITERAL
private def generateLiteral(literal: Literal, state: MethodGeneratorState): Unit = {
  Instructions.pushConstant(literal.value, state)

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

    val opcode = asmOpcode(binaryOpcode(operation.op, expressionType))
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
    state.popStack(1)
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
    state.popStack(1)
  } else {
    val ifInsn = operation.op match {
      case "==" => IFEQ
      case "!=" => IFNE
      case "<" => IFLT
      case "<=" => IFLE
      case ">" => IFGT
      case ">=" => IFGE
      case _ => throw ByteCodeGeneratorException(f"the operator ${operation.op} is not allowed as boolean operation")
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
    case VarRef(name) => generateVariableAccess(name, rvalue, state)
    case TypedExpression(VarRef(name), _) => generateVariableAccess(name, rvalue, state)
    case TypedExpression(thisAccess: ThisAccess, fieldType) => generateThisLValue(thisAccess, fieldType, rvalue, state)
    case TypedExpression(classAccess: ClassAccess, fieldType) => generateClassLValue(classAccess, fieldType, rvalue, state)
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

    Instructions.duplicateTop(state)

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
  val classType = generateTypedExpression(methodCall.target.asInstanceOf[TypedExpression], state)
  val parameterTypes = methodCall.args.map(expr => generateTypedExpression(expr.asInstanceOf[TypedExpression], state))
  val methodDescriptor = asmType(FunctionType(returnType, parameterTypes))
  Instructions.callMethod(asmUserType(classType), methodCall.methodName, methodCall.args.size, methodDescriptor, state)
}

// NEW OBJECT
private def generateNewObject(newObject: NewObject, state: MethodGeneratorState): Unit = {
  Instructions.newObject(newObject.className, state)
  Instructions.duplicateTop(state)
  newObject.arguments.foreach(expr => generateExpression(expr, state))
  Instructions.callConstructor(newObject.className, newObject.arguments.map(expr => expr.asInstanceOf[TypedExpression].exprType), state)
}

// THIS ACCESS
/**
 * set field of this to rvalue and leave value of rvalue on stack
 * @param access
 * @param fieldType
 * @param rvalue
 * @param state
 */
private def generateThisLValue(access: ThisAccess, fieldType: Type, rvalue: Expression, state: MethodGeneratorState): Unit = {
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
 * @param classAccess
 * @param fieldType
 * @param rvalue
 * @param state
 */
private def generateClassLValue(classAccess: ClassAccess, fieldType: Type, rvalue: Expression, state: MethodGeneratorState): Unit = {
  generateExpression(rvalue, state)
  loadLValueObject(classAccess.className, state)

  Instructions.duplicateTopTwo(state) // val | object | val | object
  Instructions.pop(state) // val | object | val

  Instructions.storeField(classAccess.memberName, fieldType, state)
}
/**
 * push field of object given in classAccess on stack
 * @param classAccess
 * @param fieldType
 * @param state
 */
private def generateClassRValue(classAccess: ClassAccess, fieldType: Type, state: MethodGeneratorState): Unit = {
  loadLValueObject(classAccess.className, state)

  Instructions.loadField(classAccess.memberName, fieldType, state)
}
