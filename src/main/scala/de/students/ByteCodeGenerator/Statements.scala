package de.students.ByteCodeGenerator

import org.objectweb.asm.{Label, MethodVisitor}
import org.objectweb.asm.Opcodes.*
import de.students.Parser.*
import de.students.util.Logger

private val EMPTY_STATEMENT = BlockStatement(List())
private val TRUE_EXPRESSION = TypedExpression(Literal(1), BoolType)

//////////////////////////
//      STATEMENTS      //
//////////////////////////

private def generateStatement(statement: Statement, state: MethodGeneratorState): Unit = {
  statement match {
    case block: BlockStatement => generateBlockStatement(block, state)
    case returnStatement: ReturnStatement => generateReturnStatement(returnStatement, state)
    case expressionStatement: StatementExpression => generateExpressionStatement(expressionStatement, state)
    case ifStatement: IfStatement => generateIfStatement(ifStatement, state)
    case whileStatement: WhileStatement => generateWhileStatement(whileStatement, state)
    case forStatement: ForStatement => generateForStatement(forStatement, state)
    case doWhileStatement: DoWhileStatement => generateDoWhileStatement(doWhileStatement, state)
    case switchStatement: SwitchStatement => generateSwitchStatement(switchStatement, state)
    case breakStatement: BreakStatement => generateBreakStatement(breakStatement, state)
    case continueStatement: ContinueStatement => generateContinueStatement(continueStatement, state)
    case typedStatement: TypedStatement => generateTypedStatement(typedStatement, state)
    case varDecl: VarDecl => generateVariableDeclaration(varDecl, state)
    case printStatement: PrintStatement => makePrintStatement(printStatement.toPrint, state.methodVisitor, state)
    case _ => throw NotImplementedError("unknown statement")
  }

  debugLogStack(state, f"end of statement ${statement.getClass.toString.split('.').last}")
}

// BLOCK STATEMENT
private def generateBlockStatement(statement: BlockStatement, state: MethodGeneratorState): Unit = {
  statement.stmts.foreach(stmt => generateStatement(stmt, state))
}

// RETURN
private def generateReturnStatement(statement: ReturnStatement, state: MethodGeneratorState): Unit = {
  statement.expr match {
    case Some(expr) => {
      generateExpression(expr, state)
      Instructions.returnType(state.returnType, state)
    }
    case None => {
      Instructions.returnVoid(state)
    }
  }
}

private def generateIfStatement(ifStatement: IfStatement, state: MethodGeneratorState): Unit = {
  val elseBranch = Label()
  val end = Label()

  generateExpression(ifStatement.cond, state)
  Instructions.condJump(IFEQ, elseBranch, state)

  generateStatement(ifStatement.thenBranch, state)

  Instructions.goto(end, state)
  Instructions.visitLabel(elseBranch, state)

  if (ifStatement.elseBranch.isDefined) {
    generateStatement(ifStatement.elseBranch.get, state)
  }

  Instructions.visitLabel(end, state)
  Instructions.nop(state)
}

private def generateWhileStatement(whileStatement: WhileStatement, state: MethodGeneratorState): Unit = {
  val start = Label()
  val end = Label()

  state.startLoopScope(start, end)

  Instructions.visitLabel(start, state)
  generateExpression(whileStatement.cond, state)
  Instructions.condJump(IFEQ, end, state)

  generateStatement(whileStatement.body, state)

  Instructions.goto(start, state)
  Instructions.visitLabel(end, state)
  Instructions.nop(state)

  state.endLoopScope()
}

private def generateForStatement(forStatement: ForStatement, state: MethodGeneratorState): Unit = {
  generateStatement(
    BlockStatement(
      List(
        forStatement.init.getOrElse(EMPTY_STATEMENT),
        WhileStatement(
          forStatement.cond.getOrElse(TRUE_EXPRESSION),
          BlockStatement(List(
            StatementExpression(forStatement.update.getOrElse(TRUE_EXPRESSION)),
            forStatement.body
          ))
        )
      )
    ),
    state
  )
}

private def generateDoWhileStatement(doWhileStatement: DoWhileStatement, state: MethodGeneratorState): Unit = {
  generateStatement(doWhileStatement.body, state)
  generateWhileStatement(
    WhileStatement(doWhileStatement.cond, doWhileStatement.body),
    state
  )
}

private def generateSwitchStatement(switchStatement: SwitchStatement, state: MethodGeneratorState): Unit = {
  val evaluableCases = switchStatement.cases.filter(c => c.caseLit.isDefined)
  val keys = evaluableCases.map(c => c.caseLit.get.asInstanceOf[Int]).toArray

  val end = Label()
  state.startSimpleScope(end)

  val defaultLabel = Label()
  val bodyLabels = Array.fill(evaluableCases.size)(Label())

  generateExpression(switchStatement.expr, state)
  Instructions.switch(defaultLabel, bodyLabels, keys, state)

  evaluableCases.zipWithIndex.foreach((c, i) => {
    Instructions.visitLabel(bodyLabels(i), state)
    generateStatement(c.caseBlock, state)
  })
  Instructions.visitLabel(defaultLabel, state)
  generateStatement(switchStatement.default.getOrElse(EMPTY_STATEMENT), state)
  Instructions.visitLabel(end, state)
  Instructions.nop(state)

  state.endSimpleScope()
}

private def generateBreakStatement(breakStatement: BreakStatement, state: MethodGeneratorState): Unit = {
  if (state.scopeEnds.isEmpty) {
    throw ByteCodeGeneratorException("no scope to break out from")
  }
  Instructions.goto(state.scopeEnds.last, state)
}

private def generateContinueStatement(statement: ContinueStatement, state: MethodGeneratorState): Unit = {
  if (state.loopStarts.isEmpty) {
    throw ByteCodeGeneratorException("can't continue, not in loop")
  }
  Instructions.goto(state.loopStarts.last, state)
}

private def generateVariableDeclaration(varDecl: VarDecl, state: MethodGeneratorState): Unit = {
  val varId = state.addVariable(varDecl.name, varDecl.varType)
  if (varDecl.initializer.isDefined) {
    generateExpression(varDecl.initializer.get, state)
    Instructions.storeVar(varId, varDecl.varType, state)
  }
}

private def generateTypedStatement(statement: TypedStatement, state: MethodGeneratorState): Unit = {
  // I have no idea why a statement should be typed :D
  generateStatement(statement.stmt, state)
}

private def generateExpressionStatement(statement: StatementExpression, state: MethodGeneratorState): Unit = {
  val t = generateTypedExpression(statement.expr.asInstanceOf[TypedExpression], state)

  // expression result is not used, so the stack must be popped
  if (t != VoidType) {
    Instructions.pop(state)
  }
}
