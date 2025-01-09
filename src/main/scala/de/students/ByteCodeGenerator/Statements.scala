package de.students.ByteCodeGenerator

import org.objectweb.asm.{Label, MethodVisitor}
import org.objectweb.asm.Opcodes.*
import de.students.Parser.*

// temporary I hope
private def handleBlock(block: Block, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  block.statements.foreach(stmt => generateStatement(
    stmt, methodVisitor, state
  ))
}

val EMPTY_STATEMENT = BlockStatement(List())
val TRUE_EXPRESSION = TypedExpression(Literal(1), BoolType)

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
  val elseBranch = Label()
  val end = Label()

  generateExpression(ifStatement.cond, methodVisitor, state)
  methodVisitor.visitLdcInsn(0)
  methodVisitor.visitJumpInsn(IFEQ, elseBranch)

  handleBlock(ifStatement.thenBranch, methodVisitor, state)

  methodVisitor.visitJumpInsn(GOTO, end)
  methodVisitor.visitLabel(elseBranch)

  if (ifStatement.elseBranch.isDefined) {
    handleBlock(ifStatement.elseBranch.get, methodVisitor, state)
  }

  methodVisitor.visitLabel(end)
  generateNop(methodVisitor)
}

private def generateWhileStatement(whileStatement: WhileStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  val start = Label()
  val end = Label()

  methodVisitor.visitLabel(start)
  generateExpression(whileStatement.cond, methodVisitor, state)
  methodVisitor.visitLdcInsn(0)
  methodVisitor.visitJumpInsn(IFEQ, end)

  handleBlock(whileStatement.body, methodVisitor, state)

  methodVisitor.visitJumpInsn(GOTO, start)
  methodVisitor.visitLabel(end)
  generateNop(methodVisitor)
}

private def generateForStatement(forStatement: ForStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  generateStatement(
    BlockStatement(
      List(
        forStatement.init.getOrElse(EMPTY_STATEMENT),
        WhileStatement(
          forStatement.cond.getOrElse(TRUE_EXPRESSION),
          Block(List(
            StatementExpression(forStatement.update.getOrElse(TRUE_EXPRESSION)),
            forStatement.body
          ))
        )
      )
    ),
    methodVisitor, state
  )
}

private def generateDoWhileStatement(doWhileStatement: DoWhileStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  generateStatement(doWhileStatement.body, methodVisitor, state)
  generateWhileStatement(
    WhileStatement(doWhileStatement.cond, Block(List(doWhileStatement.body))), // this Block is not pretty
    methodVisitor, state
  )
}

private def generateSwitchStatement(switchStatement: SwitchStatement, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  val default = switchStatement.cases.find(c => c.value.isEmpty) match {
    case Some(c) => c.body
    case None => EMPTY_STATEMENT
  }
  val evaluableCases = switchStatement.cases.filter(c => c.value.isDefined)
  val keys = Array.fill(evaluableCases.size)(0) // evaluableCases.map(c => c.value.get).toArray

  val defaultLabel = Label()
  val bodyLabels = Array.fill(evaluableCases.size)(Label())

  generateExpression(switchStatement.expr, methodVisitor, state)
  methodVisitor.visitLookupSwitchInsn(defaultLabel, keys, bodyLabels)

  evaluableCases.zipWithIndex.foreach((c, i) => {
    methodVisitor.visitLabel(bodyLabels(i))
    generateStatement(c.body, methodVisitor, state)
  })
  generateNop(methodVisitor)
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

private def generateNop(methodVisitor: MethodVisitor): Unit = {
  methodVisitor.visitInsn(NOP)
}