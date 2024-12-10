package de.dhbw.horb.ast;

import java.util.List;

public record While(Expression cond, Block block) implements Statement {
}
