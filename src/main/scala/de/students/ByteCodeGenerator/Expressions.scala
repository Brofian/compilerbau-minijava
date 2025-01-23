package de.students.ByteCodeGenerator

import org.objectweb.asm.{MethodVisitor, Label}
import org.objectweb.asm.Opcodes.*

import de.students.Parser.*

private def generateExpression(expression: Expression, state: MethodGeneratorState): Unit = {
  debugLogStack(state, f"eval expr $expression")

  expression match {
    case variableReference: VarRef =>
      generateVariableReference(variableReference, state)
    case literal: Literal =>
      generateLiteral(literal, state)
    case binaryOperation: BinaryOp =>
      generateBinaryOperation(binaryOperation, state)
    case typedExpression: TypedExpression =>
      generateTypedExpression(typedExpression, state)
    case methodCall: MethodCall =>
      generateMethodCall(methodCall, state)
    case newObject: NewObject =>
      generateNewObject(newObject, state)
    case thisAccess: ThisAccess =>
      generateThisAccess(thisAccess, state)
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
private def generateAssignment(left: Expression, right: Expression, state: MethodGeneratorState): Unit = {
  val varName = left match {
    case VarRef(name) => name
    case TypedExpression(VarRef(name), _) => name
    case _ => throw ByteCodeGeneratorException(f"lvalue expected, instead got $left")
  }

  val variableInfo = state.getVariable(varName)
  if (variableInfo.isField) {
    generateExpression(right, state)
    Instructions.loadThis(state)

    Instructions.duplicateTopTwo(state) // val | this | val | this
    Instructions.pop(state) // val | this | val

    Instructions.storeField(varName, variableInfo.t, state)
  } else {
    generateExpression(right, state)

    Instructions.duplicateTop(state)

    Instructions.storeVar(variableInfo.id, variableInfo.t, state)
  }
}

// TYPED EXPRESSION
private def generateTypedExpression(expression: TypedExpression, state: MethodGeneratorState): Type = {
  generateExpression(expression.expr, state)
  expression.exprType
}

// METHOD CALL
private def generateMethodCall(methodCall: MethodCall, state: MethodGeneratorState): Unit = {
  val classType = generateTypedExpression(methodCall.target.asInstanceOf[TypedExpression], state)
  methodCall.args.foreach(expr => generateExpression(expr, state))
  Instructions.callMethod(asmUserType(classType), methodCall.methodName, methodCall.args.size, state)
}

// NEW OBJECT
private def generateNewObject(newObject: NewObject, state: MethodGeneratorState): Unit = {
  val javaClassName = javaifyClass(newObject.className)
  Instructions.newObject(javaClassName, state)
  Instructions.duplicateTop(state)
  newObject.arguments.foreach(expr => generateExpression(expr, state))
  Instructions.callConstructor(javaClassName, newObject.arguments.map(expr => expr.asInstanceOf[TypedExpression].exprType), state)
}

// THIS ACCESS
private def generateThisAccess(access: ThisAccess, state: MethodGeneratorState): Unit = {

}