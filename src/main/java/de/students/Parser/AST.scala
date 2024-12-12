package de.students.Parser

// Basis-Node für alle AST-Knoten
sealed trait ASTNode

// Programm-Knoten
case class Program(classes: List[ClassDecl]) extends ASTNode

// Klassendeklaration
case class ClassDecl(
                      name: String,
                      parent: Option[String],
                      methods: List[MethodDecl],
                      fields: List[VarDecl]
                    ) extends ASTNode

// Methodendeklaration
case class MethodDecl(
                       name: String,
                       returnType: Type,
                       params: List[VarDecl],
                       body: List[Statement]
                     ) extends ASTNode

// Variablendeklaration
case class VarDecl(name: String, varType: Type) extends ASTNode

// Typen
sealed trait Type
case object IntType extends Type
case object VoidType extends Type
case class ArrayType(baseType: Type) extends Type
case class UserType(name: String) extends Type

// Statements
sealed trait Statement extends ASTNode
case class ReturnStatement(expr: Option[Expression]) extends Statement
case class IfStatement(cond: Expression, thenBranch: Statement, elseBranch: Option[Statement]) extends Statement
case class WhileStatement(cond: Expression, body: Statement) extends Statement

// Ausdrücke
sealed trait Expression extends ASTNode
case class VarRef(name: String) extends Expression
case class Literal(value: Any) extends Expression
case class BinaryOp(left: Expression, op: String, right: Expression) extends Expression
