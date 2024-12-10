package de.dhbw.horb.ast;

import java.util.List;

public record IfElse(Expression cond, Block ifBlock, Block elseBlock) implements Statement {
}
