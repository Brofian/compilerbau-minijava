package de.students.ByteCodeGenerator

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*

import de.students.Parser.*

private def generateExpression(expression: Expression, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  expression match {
    case variableReference: VarRef =>
      generateVariableReference(variableReference, methodVisitor, state)
    case literal: Literal =>
      generateLiteral(literal, methodVisitor, state)
    case binaryOperation: BinaryOp =>
      generateBinaryOperation(binaryOperation, methodVisitor, state)
    case typedExpression: TypedExpression =>
      generateTypedExpression(typedExpression, methodVisitor, state)
    // case methodCall: MethodCall => ???
    case _ => throw NotImplementedError("this expression is not yet implemented")
  }
}

// VARIABLE REFERENCE
private def generateVariableReference(varRef: VarRef, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  state.pushStack()

  val variableInfo = state.getVariable(varRef.name)
  if (variableInfo.isField) {
    methodVisitor.visitVarInsn(asmLoadInsn(variableInfo.t), 0)
    methodVisitor.visitFieldInsn(GETFIELD, state.className, varRef.name, asmType(variableInfo.t))
  } else {
    val varInfo = state.getVariable(varRef.name)
    methodVisitor.visitVarInsn(asmLoadInsn(varInfo.t), varInfo.id)
  }
}

// LITERAL
private def generateLiteral(literal: Literal, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  state.pushStack()

  methodVisitor.visitLdcInsn(literal.value)
}

// BINARY OPERATION
private def generateBinaryOperation(operation: BinaryOp, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  if (operation.op == "=") {
    generateAssignment(operation.left, operation.right, methodVisitor, state)
  } else {
    val expressionType = generateTypedExpression(operation.left.asInstanceOf[TypedExpression], methodVisitor, state)
    generateExpression(operation.right, methodVisitor, state) // should be same type, this is not our problem if not

    val opcode = asmOpcode(binaryOpcode(operation.op, expressionType))
    methodVisitor.visitInsn(opcode)

    state.popStack()
  }
}

// ASSIGNMENT
private def generateAssignment(left: Expression, right: Expression, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  val varName = left match {
    case VarRef(name) => name
    case _ => throw ByteCodeGeneratorException(f"lvalue expected, instead got $left")
  }

  val variableInfo = state.getVariable(varName)
  if (variableInfo.isField) {
    methodVisitor.visitVarInsn(asmLoadInsn(variableInfo.t), 0)
    generateExpression(right, methodVisitor, state)
    methodVisitor.visitFieldInsn(PUTFIELD, state.className, varName, asmType(variableInfo.t))
  } else {
    generateExpression(right, methodVisitor, state)
    methodVisitor.visitVarInsn(ISTORE, variableInfo.id)
  }
  
  state.popStack()
}

// TYPED EXPRESSION
private def generateTypedExpression(expression: TypedExpression, methodVisitor: MethodVisitor, state: MethodGeneratorState): Type = {
  generateExpression(expression.expr, methodVisitor, state)
  expression.exprType
}
