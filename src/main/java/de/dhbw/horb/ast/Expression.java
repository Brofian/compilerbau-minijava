package de.dhbw.horb.ast;

public sealed interface Expression extends Node permits Binary, IntConstant, BoolConstant, Location, FunctionCall {
}
