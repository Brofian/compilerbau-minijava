package de.students.ByteCodeGenerator

import org.objectweb.asm.{Label, MethodVisitor}
import org.objectweb.asm.Opcodes.*
import de.students.Parser.*

// temporary I hope
def handleBlock(block: Block, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  block.statements.foreach(stmt => generateStatement(
    stmt, methodVisitor, state
  ))
}

//////////////////////////
//      STATEMENTS      //
//////////////////////////

private def generateStatement(statement: Statement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  statement match {
    case block: BlockStatement => generateBlockStatement(block, methodVisitor, state)
    case returnStatement: ReturnStatement => generateReturnStatement(returnStatement, methodVisitor, state)
    case expressionStatement: StatementExpression => generateExpressionStatement(expressionStatement, methodVisitor, state)
    case ifStatement: IfStatement => generateIfStatement(ifStatement, methodVisitor, state)
    case whileStatement: WhileStatement => generateWhileStatement(whileStatement, methodVisitor, state)
    case forStatement: ForStatement => generateForStatement(forStatement, methodVisitor, state)
    case doWhileStatement: DoWhileStatement => generateDoWhileStatement(doWhileStatement, methodVisitor, state)
    case switchStatement: SwitchStatement => generateSwitchStatement(switchStatement, methodVisitor, state)
    case breakStatement: BreakStatement => generateBreakStatement(breakStatement, methodVisitor, state)
    case continueStatement: ContinueStatement => generateContinueStatement(continueStatement, methodVisitor, state)
    case typedStatement: TypedStatement => generateTypedStatement(typedStatement, methodVisitor, state)
    case varDecl: VarDecl => generateVariableDeclaration(varDecl, methodVisitor, state)
    case _ => throw NotImplementedError("unknown statement")
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

private def generateIfStatement(ifStatement: IfStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  generateExpression(ifStatement.cond, methodVisitor, state)
  val elseBranch = Label()
  val end = Label()

  methodVisitor.visitLdcInsn(1)
  methodVisitor.visitJumpInsn(IFEQ, elseBranch)

  handleBlock(ifStatement.thenBranch, methodVisitor, state)

  methodVisitor.visitJumpInsn(GOTO, end)
  methodVisitor.visitLabel(elseBranch)

  if (ifStatement.elseBranch.isDefined) {
    handleBlock(ifStatement.elseBranch.get, methodVisitor, state)
  }

  methodVisitor.visitLabel(end)
  methodVisitor.visitInsn(NOP)
}

private def generateWhileStatement(statement: WhileStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {

}

private def generateForStatement(statement: ForStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {

}

private def generateDoWhileStatement(statement: DoWhileStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {

}

private def generateSwitchStatement(statement: SwitchStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {

}

private def generateBreakStatement(statement: BreakStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {

}

private def generateContinueStatement(statement: ContinueStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {

}

private def generateVariableDeclaration(statement: VarDecl, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {

}

private def generateTypedStatement(statement: TypedStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {

}

private def generateExpressionStatement(statement: StatementExpression, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  generateExpression(statement.expr, methodVisitor, state)
}
