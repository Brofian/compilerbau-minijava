package de.students.semantic

import de.students.Parser._

import scala.collection.mutable


object ExpressionChecks {

  def checkExpression(expr: Expression, context: SemanticContext): TypedExpression = {
    expr match {
      case _ => throw new SemanticException(s"Could not match expression $expr")
    }
  }



}