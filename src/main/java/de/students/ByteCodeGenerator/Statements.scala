package de.students.ByteCodeGenerator

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*

import de.students.Parser.*

//////////////////////////
//      STATEMENTS      //
//////////////////////////

private def generateStatement(statement: Statement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  statement match {
    case block: BlockStatement => generateBlockStatement(block, methodVisitor, state)
    case returnStatement: ReturnStatement => generateReturnStatement(returnStatement, methodVisitor, state)
    case expressionStatement: ExpressionStatement => generateExpressionStatement(expressionStatement, methodVisitor, state)
    case IfStatement(cond, thenBranch, elseBranch) => ???
    case WhileStatement(cond, body) => ???
    case TypedStatement(stmt, stmtType) => ???
  }
}

// BLOCK STATEMENT
private def generateBlockStatement(statement: BlockStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  statement.stmts.foreach(stmt => generateStatement(stmt, methodVisitor, state))
}

// RETURN
private def generateReturnStatement(statement: ReturnStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  statement.expr match {
    case Some(expr) => {
      generateExpression(expr, methodVisitor, state)
      methodVisitor.visitInsn(asmReturnCode(state.returnType))
    }
    case None => {
      methodVisitor.visitInsn(RETURN)
    }
  }
}

private def generateIfStatement(statement: IfStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {

}

private def generateWhileStatement(statement: WhileStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {

}

private def generateTypedStatement(statement: TypedStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {

}

private def generateExpressionStatement(statement: ExpressionStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  generateExpression(statement.expr, methodVisitor, state)
}
