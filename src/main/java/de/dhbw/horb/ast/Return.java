package de.dhbw.horb.ast;

import java.util.ArrayList;
import java.util.List;

public record Return(Expression ret) implements Statement {
}
