package de.students.Parser

// basic node for all AST-trees
sealed trait ASTNode

// Program-node
case class Package( name : String,
                    classes: List[ClassDecl]) extends ASTNode

// Class declaration
case class ClassDecl(
                      name: String,
                      parent: String,
                      isAbstract : Boolean,
                      methods: List[MethodDecl],
                      fields: List[VarDecl]
                    ) extends ASTNode

// method declaration
case class MethodDecl(
                       name: String,
                       static : Boolean,
                       isAbstract : Boolean,
                       returnType: Type,
                       params: List[VarDecl],
                       body: List[Statement]
                     ) extends ASTNode

// variable declaration
case class VarDecl(name: String, varType: Type) extends ASTNode

// types
sealed trait Type
case object IntType extends Type
case object VoidType extends Type
case class ArrayType(baseType: Type) extends Type
case class UserType(name: String) extends Type

// statements
sealed trait Statement extends ASTNode
case class ReturnStatement(expr: Option[Expression]) extends Statement
case class IfStatement(cond: Expression, thenBranch: Statement, elseBranch: Option[Statement]) extends Statement
case class WhileStatement(cond: Expression, body: Statement) extends Statement

// expressions
sealed trait Expression extends ASTNode
case class VarRef(name: String) extends Expression
case class Literal(value: Any) extends Expression
case class BinaryOp(left: Expression, op: String, right: Expression) extends Expression
