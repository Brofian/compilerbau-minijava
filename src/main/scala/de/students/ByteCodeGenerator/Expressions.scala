package de.students.ByteCodeGenerator

import org.objectweb.asm.{MethodVisitor, Label}
import org.objectweb.asm.Opcodes.*

import de.students.Parser.*

private def generateExpression(expression: Expression, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  debugLogStack(state, f"eval expr $expression")

  expression match {
    case variableReference: VarRef =>
      generateVariableReference(variableReference, methodVisitor, state)
    case literal: Literal =>
      generateLiteral(literal, methodVisitor, state)
    case binaryOperation: BinaryOp =>
      generateBinaryOperation(binaryOperation, methodVisitor, state)
    case typedExpression: TypedExpression =>
      generateTypedExpression(typedExpression, methodVisitor, state)
    case methodCall: MethodCall =>
      generateMethodCall(methodCall, methodVisitor, state)
    case _ => throw NotImplementedError("this expression is not yet implemented")
  }
}

// VARIABLE REFERENCE
private def generateVariableReference(varRef: VarRef, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  debugLogStack(state, "start var ref")

  state.pushStack()

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
private def generateLiteral(literal: Literal, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  state.pushStack()

  methodVisitor.visitLdcInsn(literal.value)

  debugLogStack(state, f"pushed $literal")
}

// BINARY OPERATION
private def generateBinaryOperation(operation: BinaryOp, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  debugLogStack(state, f"start bin op ${operation.op}")

  if (operation.op == "=") {
    generateAssignment(operation.left, operation.right, methodVisitor, state)
  } else if (isBooleanOpcode(operation.op)) {
    generateBooleanOperation(operation, methodVisitor, state)
  } else {
    val expressionType = generateTypedExpression(operation.left.asInstanceOf[TypedExpression], methodVisitor, state)
    generateExpression(operation.right, methodVisitor, state) // should be same type, this is not our problem if not

    val opcode = asmOpcode(binaryOpcode(operation.op, expressionType))
    methodVisitor.visitInsn(opcode)

    state.popStack(1)
  }

  debugLogStack(state, f"end bin op ${operation.op}")
}

// BOOLEAN OPERATION
private def generateBooleanOperation(operation: BinaryOp, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  // && and || need special treatment
  if (operation.op == "&&") {
    val rightEval = Label()
    val falsePush = Label()
    val end = Label()

    generateExpression(operation.left, methodVisitor, state)
    methodVisitor.visitLdcInsn(1)
    methodVisitor.visitJumpInsn(IFEQ, rightEval)
    methodVisitor.visitJumpInsn(GOTO, falsePush)
    methodVisitor.visitLabel(rightEval)
    generateExpression(operation.right, methodVisitor, state)
    methodVisitor.visitLdcInsn(0)
    methodVisitor.visitJumpInsn(IFEQ, falsePush)
    methodVisitor.visitLdcInsn(1) // true
    methodVisitor.visitJumpInsn(GOTO, end)
    methodVisitor.visitLabel(falsePush)
    methodVisitor.visitLdcInsn(0) // false
    methodVisitor.visitLabel(end)

    state.popStack(2)
  } else if (operation.op == "||") {
    val truePush = Label()
    val end = Label()

    generateExpression(operation.left, methodVisitor, state)
    methodVisitor.visitLdcInsn(1)
    methodVisitor.visitJumpInsn(IFEQ, truePush)
    generateExpression(operation.right, methodVisitor, state)
    methodVisitor.visitLdcInsn(1)
    methodVisitor.visitJumpInsn(IFEQ, truePush)
    methodVisitor.visitLdcInsn(0) // false
    methodVisitor.visitJumpInsn(GOTO, end)
    methodVisitor.visitLabel(truePush)
    methodVisitor.visitLdcInsn(1) //true
    methodVisitor.visitLabel(end)

    state.popStack(2)
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

    generateExpression(operation.left, methodVisitor, state)
    generateExpression(operation.right, methodVisitor, state)
    methodVisitor.visitJumpInsn(ifInsn, truePush)
    methodVisitor.visitLdcInsn(0) // false
    methodVisitor.visitJumpInsn(GOTO, end)
    methodVisitor.visitLabel(truePush)
    methodVisitor.visitLdcInsn(1) // true
    methodVisitor.visitLabel(end)

    state.popStack(2)
  }
}

// ASSIGNMENT
private def generateAssignment(left: Expression, right: Expression, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  val varName = left match {
    case VarRef(name) => name
    case TypedExpression(VarRef(name), _) => name
    case _ => throw ByteCodeGeneratorException(f"lvalue expected, instead got $left")
  }

  val variableInfo = state.getVariable(varName)
  if (variableInfo.isField) {
    state.pushStack()

    methodVisitor.visitVarInsn(ALOAD, 0)
    generateExpression(right, methodVisitor, state)

    state.pushStack()
    methodVisitor.visitInsn(DUP)

    methodVisitor.visitFieldInsn(PUTFIELD, state.className, varName, asmType(variableInfo.t))

    state.popStack(2)
  } else {
    generateExpression(right, methodVisitor, state)

    state.pushStack()
    methodVisitor.visitInsn(DUP)

    methodVisitor.visitVarInsn(ISTORE, variableInfo.id)

    state.popStack(1)
  }
}

// TYPED EXPRESSION
private def generateTypedExpression(expression: TypedExpression, methodVisitor: MethodVisitor, state: MethodGeneratorState): Type = {
  generateExpression(expression.expr, methodVisitor, state)
  expression.exprType
}

// METHOD CALL
private def generateMethodCall(methodCall: MethodCall, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  state.pushStack()

  methodVisitor.visitVarInsn(ALOAD, 0)
  methodCall.args.foreach(expr => generateExpression(expr, methodVisitor, state))
  methodVisitor.visitMethodInsn(INVOKEVIRTUAL, state.className, methodCall.methodName, state.methodDescriptors(methodCall.methodName), false)

  state.popStack(1 + methodCall.args.size)
}