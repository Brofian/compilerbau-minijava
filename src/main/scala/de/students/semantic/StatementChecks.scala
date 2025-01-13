package de.students.semantic

import de.students.Parser.*


object StatementChecks {

  def checkStatement(stmt: Statement, context: SemanticContext): TypedStatement = {
    stmt match {
      case b@BlockStatement(_) => this.checkBlockStatement(b, context.createChildContext() /* New block, new context */)
      case r@ReturnStatement(_) => this.checkReturnStatement(r, context)
      case s@IfStatement(_, _, _) => this.checkIfStatement(s, context)
      case w@WhileStatement(_, _) => this.checkWhileStatement(w, context)
      case d@DoWhileStatement(_, _) => this.checkDoWhileStatement(d, context)
      case f@ForStatement(_, _, _, _) => this.checkForStatement(f, context.createChildContext() /* For loops can declare their own variables before block is entered */)
      case s@SwitchStatement(_, _, _) => this.checkSwitchStatement(s, context)
      case c@SwitchCase(_, _) => throw new SemanticException("SwitchCase statements are checked by the switchStatement and should never occur alone")
      case d@DefaultCase(_) => throw new SemanticException("DefaultCase statements are checked by the switchStatement and should never occur alone")
      case e@StatementExpressions(_) => this.checkExpressionStatement(e, context)
      case b@BreakStatement() => this.checkBreakStatement(b, context)
      case c@ContinueStatement() => this.checkContinueStatement(c, context)
      case _ => throw new SemanticException(s"Could not match statement $stmt")
    }
  }

  private def checkBlockStatement(blockStmt: BlockStatement, context: SemanticContext): TypedStatement = {

    // type check all stmts
    val typedStmtList: List[TypedStatement] = blockStmt.stmts.map(stmt => this.checkStatement(stmt, context))

    val typeList: List[Type] = typedStmtList.map(typedStmt => typedStmt.stmtType).concat(List(NoneType));

    // the block type is the combination of all return operations inside the block
    val blockType: Type = typeList.reduce((carry, stmtType) => {
      // combine the new type with the already found types (and throw an error, if not possible)
      UnionTypeFinder.getUnion(carry, stmtType, context)
    })

    TypedStatement(
      BlockStatement(typedStmtList),
      blockType
    )
  }

  private def checkReturnStatement(returnStmt: ReturnStatement, context: SemanticContext): TypedStatement = {
    returnStmt.expr match {
      case None => TypedStatement(ReturnStatement(None), VoidType)
      case Some(expr) =>
        val typedExpr = ExpressionChecks.checkExpression(expr, context)
        TypedStatement(
          ReturnStatement(Some(typedExpr)),
          typedExpr.exprType,
        )
    }
  }

  private def checkIfStatement(ifStmt: IfStatement, context: SemanticContext): TypedStatement = {
    // condition
    val typedCondition = ExpressionChecks.checkExpression(ifStmt.cond, context.createChildContext())
    if (typedCondition.exprType != BoolType) {
      throw new SemanticException("if condition does not evaluate to boolean")
    }

    // if case
    val typedThenBranch = this.checkStatement(ifStmt.thenBranch, context)
    // else case
    val typedElseBranch: Option[TypedStatement] = ifStmt.elseBranch match {
      case Some(stmt) => Some(this.checkStatement(stmt, context.createChildContext()))
      case None => None
    }

    val stmtType: Type = typedElseBranch match {
      case Some(elseBranch) => UnionTypeFinder.getUnion(typedThenBranch.stmtType, elseBranch.stmtType, context)
      case None => typedThenBranch.stmtType
    }

    TypedStatement(IfStatement(typedCondition, typedThenBranch, typedElseBranch), stmtType)
  }

  private def checkWhileStatement(whileStmt: WhileStatement, context: SemanticContext): TypedStatement = {
    // condition
    val typedCondition = ExpressionChecks.checkExpression(whileStmt.cond, context.createChildContext())
    if (typedCondition.exprType != BoolType) {
      throw new SemanticException("While condition does not evaluate to boolean")
    }

    // body
    val typedBody = this.checkStatement(whileStmt.body, context)

    TypedStatement(WhileStatement(typedCondition, typedBody), typedBody.stmtType)
  }

  private def checkDoWhileStatement(doWhileStmt: DoWhileStatement, context: SemanticContext): TypedStatement = {
    // condition
    val typedCondition = ExpressionChecks.checkExpression(doWhileStmt.cond, context.createChildContext())
    if (typedCondition.exprType != BoolType) {
      throw new SemanticException("DoWhile condition does not evaluate to boolean")
    }

    // body
    val typedBody = this.checkStatement(doWhileStmt.body, context)

    TypedStatement(DoWhileStatement(typedCondition, typedBody), typedBody.stmtType)
  }

  private def checkForStatement(forStmt: ForStatement, context: SemanticContext): TypedStatement = {

    val typedInit = if forStmt.init.isEmpty then None else
      Some(StatementChecks.checkStatement(forStmt.init.get, context))

    val typedCond = if forStmt.cond.isEmpty then None else
      Some(ExpressionChecks.checkExpression(forStmt.cond.get, context))

    val typedUpdate = if forStmt.update.isEmpty then None else
      Some(ExpressionChecks.checkExpression(forStmt.update.get, context))


    if (typedCond.isEmpty || (typedCond.get.exprType != BoolType)) {
      throw new SemanticException("For condition does not evaluate to boolean")
    }

    // body
    val typedBody = this.checkStatement(forStmt.body, context)
    TypedStatement(ForStatement(typedInit, typedCond, typedUpdate, typedBody), typedBody.stmtType)
  }


  private def checkSwitchStatement(switchStmt: SwitchStatement, context: SemanticContext): TypedStatement = {

    val typedExpr = ExpressionChecks.checkExpression(switchStmt.expr, context)

    val typedCases: List[SwitchCase] = switchStmt.cases.map(caseStmt =>

      // check if the switch value and the case literal are the same
      if (caseStmt.caseLit.nonEmpty) {
        val literal = ExpressionChecks.checkExpression(caseStmt.caseLit.get, context)
        if (literal.exprType != typedExpr.exprType) {
          throw new SemanticException(s"Switch-Case literal ${caseStmt.caseLit} does not match type with checked value")
        }
      }

      SwitchCase(
        caseStmt.caseLit,
        StatementChecks.checkStatement(caseStmt.caseBlock, context.createChildContext())
      )
    )

    val typedDefault = if switchStmt.default.isEmpty then None else
      Some(DefaultCase(StatementChecks.checkStatement(switchStmt.default.get.caseBlock, context.createChildContext())))


    val caseUnionType: Type = typedCases.map(c => c.caseBlock.asInstanceOf[TypedStatement])
      .reduce((a: TypedStatement, b: TypedStatement) => {
        TypedStatement(
          BreakStatement(), // placeholder, as we need a statement for the reduce
          UnionTypeFinder.getUnion(a.stmtType, b.stmtType, context)
        )
      }).stmtType

    val switchUnionType: Type = if typedDefault.isEmpty then caseUnionType else
      UnionTypeFinder.getUnion(caseUnionType, typedDefault.get.caseBlock.asInstanceOf[TypedStatement].stmtType, context)

    TypedStatement(SwitchStatement(typedExpr, typedCases, typedDefault), switchUnionType)
  }

  private def checkExpressionStatement(expressionStmt: StatementExpressions, context: SemanticContext): TypedStatement = {
    val typedExpr = ExpressionChecks.checkExpression(expressionStmt.expr, context)
    TypedStatement(StatementExpressions(typedExpr), NoneType)
  }

  private def checkBreakStatement(breakStmt: BreakStatement, context: SemanticContext): TypedStatement = {
    TypedStatement(breakStmt, NoneType)
  }

  private def checkContinueStatement(continueStmt: ContinueStatement, context: SemanticContext): TypedStatement = {
    TypedStatement(continueStmt, NoneType)
  }


}