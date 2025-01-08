package de.students.ByteCodeGenerator

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*

import de.students.Parser.*

private def generateExpression(expression: Expression, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  expression match {
    case variableReference: VariableReference => generateVariableReference(variableReference, methodVisitor, state)
    case literal: Literal => generateLiteral(literal, methodVisitor, state)
    case binaryOperation: BinaryOperation => generateBinaryOperation(binaryOperation, methodVisitor, state)
    case typedExpression: TypedExpression => generateTypedExpression(typedExpression, methodVisitor, state)
    // case assignment: Assignment => ???
    // case methodCall: MethodCall => ???
    case _ => throw NotImplementedError("this expression is not yet implemented")
  }
}

// VARIABLE REFERENCE
private def generateVariableReference(varRef: VariableReference, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  val field = state.fields.find(varDecl => varDecl.name == varRef.name)
  field match {
    case Some(varDecl) => {
      state.stackDepth += 1
      methodVisitor.visitVarInsn(ALOAD, 0)
      methodVisitor.visitFieldInsn(GETFIELD, state.className, varRef.name, asmType(varDecl.varType))
    }
    case None => throw NotImplementedError("local variables are not yet implemented")
  }
}

// LITERAL
private def generateLiteral(literal: Literal, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  state.stackDepth += 1
  methodVisitor.visitLdcInsn(literal.value)
}

// BINARY OPERATION
private def generateBinaryOperation(operation: BinaryOperation, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  val expressionType = generateTypedExpression(operation.left.asInstanceOf[TypedExpression], methodVisitor, state)
  generateExpression(operation.right, methodVisitor, state) // should be same type, this is not our problem if not

  val opcode = asmOpcode(binaryOpcode(stringToOperation(operation.op), expressionType))
  methodVisitor.visitInsn(opcode)
}

// TYPED EXPRESSION
private def generateTypedExpression(expression: TypedExpression, methodVisitor: MethodVisitor, state: MethodGeneratorState): Type = {
  generateExpression(expression.expr, methodVisitor, state)
  expression.exprType
}
