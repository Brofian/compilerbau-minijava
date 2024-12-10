package de.dhbw.horb.ast;

import java.util.List;

public record Variable(String name, Type type) implements Node{
}
