package de.students.semantic

import de.students.Parser._

import scala.collection.mutable


object StatementChecks {

  def checkStatement(stmt: Statement, context: SemanticContext): TypedStatement = {
    stmt match {
      case b @ BlockStatement(_) => this.checkBlockStatement(b, context.createChildContext() /* New block, new context */)
      case r @ ReturnStatement(_) => this.checkReturnStatement(r, context)
      case s @ IfStatement(_, _, _) => this.checkIfStatement(s, context)
      case w @ WhileStatement(_, _) => this.checkWhileStatement(w, context)
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


}