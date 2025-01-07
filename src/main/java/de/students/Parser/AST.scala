package de.students.Parser

// basic node for all AST-trees
sealed trait ASTNode

// Program-node
case class Package(name: String, classes: List[ClassDecl]) extends ASTNode

// Class declaration
case class ClassDecl(
                      name: String,
                      parent: String,
                      isAbstract: Boolean,
                      methods: List[MethodDecl],
                      fields: List[VarDecl],
                      constructors: List[ConstructorDecl]
                    ) extends ASTNode

// method declaration
case class MethodDecl(
                       name: String,
                       static: Boolean,
                       isAbstract: Boolean,
                       returnType: Type,
                       params: List[VarDecl],
                       body: List[Statement]
                     ) extends ASTNode

// Constructor declaration
case class ConstructorDecl(
                            name: String,
                            params: List[VarDecl],
                            body: List[Statement]
                          ) extends ASTNode

// variable declaration
case class VarDecl(name: String, varType: Type, initializer : Option[Expression]) extends Statement

// types
sealed trait Type extends ASTNode
case object IntType extends Type
case object VoidType extends Type
case class ArrayType(baseType: Type) extends Type
case class UserType(name: String) extends Type



case class Block(statements : List[Statement]) extends ASTNode

// statements
sealed trait Statement extends ASTNode
case class ReturnStatement(expr: Option[Expression]) extends Statement
case class IfStatement(cond: Expression, thenBranch: Block, elseBranch: Option[Block]) extends Statement
case class WhileStatement(cond: Expression, body: Block) extends Statement
case class ForStatement(init: Option[Statement], cond: Option[Expression], update: Option[Expression], body: Statement) extends Statement
case class DoWhileStatement(cond: Expression, body: Statement) extends Statement
case class SwitchStatement(expr: Expression, cases: List[SwitchCase]) extends Statement
case class StatementExpressions(expr: Expression) extends Statement
case class BreakStatement() extends Statement
case class ContinueStatement() extends Statement

// switch case
case class SwitchCase(value: Option[Expression], body: Statement) extends ASTNode

// expressions
sealed trait Expression extends ASTNode
case class VarRef(name: String) extends Expression
case class Literal(value: Any) extends Expression
case class BinaryOp(left: Expression, op: String, right: Expression) extends Expression
case class ThisAccess(name: String) extends Expression
case class ClassAccess(className: String, memberName: String) extends Expression
case class NewObject(className: String, arguments: List[Expression]) extends Expression
case class NewArray(arrayType: Type, dimensions: List[Expression]) extends Expression
case class MethodCall(target: Expression, methodName: String, args: List[Expression]) extends Expression