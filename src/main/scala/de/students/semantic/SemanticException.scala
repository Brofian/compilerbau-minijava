package de.students.semantic

class SemanticException(err: String) extends Exception(err) {}

class InheritedSemanticException(err: String) extends SemanticException(err) {}
