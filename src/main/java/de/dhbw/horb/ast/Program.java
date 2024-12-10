package de.dhbw.horb.ast;

import java.util.List;

public record Program(List<Variable> variables, List<Function> methods) implements Node {
}
