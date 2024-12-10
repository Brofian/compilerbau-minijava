package de.dhbw.horb.ast;

public sealed interface Statement extends Node permits Assignment, VoidFunctionCall, IfElse, While, Return, ReturnVoid, Break, Continue {
}
