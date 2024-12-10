package de.dhbw.horb.ast;

import java.util.List;

public record Assignment(Location loc, Expression value) implements Statement {
}
