package de.students.semantic

import de.students.Parser._

import scala.collection.mutable


object StatementChecks {

  def checkStatement(stmt: Statement, typeAssumptions: mutable.Map[String, Type]): TypedStatement = {
    stmt match {
      case b @ BlockStatement(_) => this.checkBlockStatement(b, typeAssumptions.clone() /* New block, new context */)
      case r @ ReturnStatement(_) => this.checkReturnStatement(r, typeAssumptions)
      case s @ IfStatement(_, _, _) => this.checkIfStatement(s, typeAssumptions)
      case w @ WhileStatement(_, _) => this.checkWhileStatement(w, typeAssumptions)
      case _ => throw new SemanticException(s"Could not match statement $stmt")
    }
  }

  private def checkBlockStatement(blockStmt: BlockStatement, typedAssumptions: mutable.Map[String,Type]): TypedStatement = {

    // type check all stmts
    val typedStmtList: List[TypedStatement] = blockStmt.stmts.map(stmt => this.checkStatement(stmt, typedAssumptions))

    val typeList: List[Type] = typedStmtList.map(typedStmt => typedStmt.stmtType).concat(List(NoneType));

    // the block type is the combination of all return operations inside the block
    val blockType: Type = typeList.reduce((carry, stmtType) => {
      // combine the new type with the already found types (and throw an error, if not possible)
      UnionTypeFinder.getUnion(carry, stmtType, typedAssumptions)
    })

    TypedStatement(
      BlockStatement(typedStmtList),
      blockType
    )
  }

  private def checkReturnStatement(returnStmt: ReturnStatement, typeAssumptions: mutable.Map[String, Type]): TypedStatement = {
    returnStmt.expr match {
      case None => TypedStatement(ReturnStatement(None), VoidType)
      case Some(expr) =>
        val typedExpr = ExpressionChecks.checkExpression(expr, typeAssumptions)
        TypedStatement(
          ReturnStatement(Some(typedExpr)),
          typedExpr.exprType,
        )
    }
  }

  private def checkIfStatement(ifStmt: IfStatement, typeAssumptions: mutable.Map[String, Type]): TypedStatement = {
    // condition
    val typedCondition = ExpressionChecks.checkExpression(ifStmt.cond, typeAssumptions.clone())
    if (typedCondition.exprType != BoolType) {
      throw new SemanticException("if condition does not evaluate to boolean")
    }

    // if case
    val typedThenBranch = this.checkBlockStatement(BlockStatement(ifStmt.thenBranch.statements), typeAssumptions)
    // else case
    val typedElseBranch: Option[TypedStatement] = ifStmt.elseBranch match {
      case Some(stmt) => Some(this.checkBlockStatement(BlockStatement(stmt.statements), typeAssumptions.clone()))
      case None => None
    }

    val stmtType: Type = typedElseBranch match {
      case Some(elseBranch) => UnionTypeFinder.getUnion(typedThenBranch.stmtType, elseBranch.stmtType, typeAssumptions)
      case None => typedThenBranch.stmtType
    }

    ???
    // TypedStatement(IfStatement(typedCondition, typedThenBranch, typedElseBranch), stmtType)
  }

  private def checkWhileStatement(whileStmt: WhileStatement, typeAssumptions: mutable.Map[String, Type]): TypedStatement = {
    // condition
    val typedCondition = ExpressionChecks.checkExpression(whileStmt.cond, typeAssumptions.clone())
    if (typedCondition.exprType != BoolType) {
      throw new SemanticException("While condition does not evaluate to boolean")
    }

    // body
    // val typedBody = this.checkStatement(whileStmt.body, typeAssumptions)

    // TypedStatement(WhileStatement(typedCondition, typedBody), typedBody.stmtType)
    ???
  }


}