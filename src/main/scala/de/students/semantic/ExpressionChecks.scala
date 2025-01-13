package de.students.semantic

import de.students.Parser._

import scala.collection.mutable


object ExpressionChecks {

  def checkExpression(expr: Expression, context: SemanticContext): TypedExpression = {
    expr match {
      case l @ Literal(_) => this.checkLiteralExpression(l, context)
      case _ => throw new SemanticException(s"Could not match expression $expr")
    }
  }

  private def checkLiteralExpression(literal: Literal, context: SemanticContext): TypedExpression = {
    literal.value match {
      case _: Boolean => TypedExpression(literal, BoolType)
      case _: Int => TypedExpression(literal, IntType)
      case _: String => throw SemanticException("String is not yet implemented as a type")
      case _ => throw SemanticException(s"Unknown literal: $literal")
    }
  }




}