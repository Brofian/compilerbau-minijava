package de.dhbw.horb.ast;

import java.util.List;

public record FunctionCall(String name, List<Expression> args) implements Expression {
}
