package de.dhbw.horb.ast;

import de.dhbw.horb.ExpressionGenerator;

import java.util.List;

public record Binary(Expression left, Operator op, Expression right) implements Expression{
}
