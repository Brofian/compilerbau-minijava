package de.dhbw.horb.ast;

import java.util.ArrayList;
import java.util.List;

public record Block(List<Variable> vars, List<Statement> stmts) implements Node {
}
