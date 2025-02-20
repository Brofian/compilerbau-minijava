package de.students.Parser

case class Project(packages: List[Package])

// basic node for all AST-trees
sealed trait ASTNode

// Program-node
case class Package(name: String, imports: Imports, classes: List[ClassDecl]) extends ASTNode

case class Imports(names: List[String]) extends ASTNode

// Class declaration
case class ClassDecl(
  name: String,
  parent: String,
  isAbstract: Boolean,
  methods: List[MethodDecl],
  fields: List[FieldDecl],
  constructors: List[ConstructorDecl]
) extends ASTNode

// method declaration
case class MethodDecl(
  accessModifier: Option[String], // Optional (default to package-private)
  name: String,
  isAbstract: Boolean,
  static: Boolean,
  isFinal: Boolean,
  returnType: Type,
  params: List[VarDecl],
  body: Option[Statement] // Optional to handle abstract methods
) extends ASTNode

// Constructor declaration
case class ConstructorDecl(
  accessModifier: Option[String],
  name: String,
  params: List[VarDecl],
  body: Statement
) extends ASTNode

// Field declaration
case class FieldDecl(
  accessModifier: Option[String],
  isFinal: Boolean,
  name: String,
  varType: Type,
  initializer: Option[Expression]
) extends ASTNode

// Variable declaration
case class VarDecl(name: String, varType: Type, initializer: Option[Expression]) extends Statement

// Types
sealed trait Type extends ASTNode

case object NoneType extends Type // used for statements that do not evaluate to any type
case object IntType extends Type
case object ShortType extends Type
case object LongType extends Type
case object ByteType extends Type
case object FloatType extends Type
case object DoubleType extends Type
case object CharType extends Type
case object BoolType extends Type
case object VoidType extends Type
case class ArrayType(baseType: Type) extends Type
case class UserType(name: String) extends Type
case class FunctionType(returnType: Type, parameterTypes: List[Type]) extends Type

// statements
sealed trait Statement extends ASTNode
case class BlockStatement(stmts: List[Statement]) extends Statement
case class ReturnStatement(expr: Option[Expression]) extends Statement
case class IfStatement(cond: Expression, thenBranch: Statement, elseBranch: Option[Statement]) extends Statement
case class WhileStatement(cond: Expression, body: Statement) extends Statement
case class ForStatement(init: Option[Statement], cond: Option[Expression], update: Option[Expression], body: Statement)
    extends Statement
case class DoWhileStatement(cond: Expression, body: Statement) extends Statement
case class SwitchStatement(expr: Expression, cases: List[SwitchCase], default: Option[DefaultCase]) extends Statement
case class SwitchCase(caseLit: Option[Literal], caseBlock: Statement) extends Statement
case class DefaultCase(caseBlock: Statement) extends Statement
case class StatementExpression(expr: Expression) extends Statement
case class BreakStatement() extends Statement
case class ContinueStatement() extends Statement
case class TypedStatement(stmt: Statement, stmtType: Type) extends Statement
case class PrintStatement(toPrint: Expression) extends Statement

// expressions
sealed trait Expression extends ASTNode
case class VarRef(name: String) extends Expression
// special case for VarRef is the reference of a static class context
case class StaticClassRef(className: String) extends Expression
case class Literal(value: Any) extends Expression
case class BinaryOp(left: Expression, op: String, right: Expression) extends Expression
case class UnaryOp(op: String, expr: Expression) extends Expression

// Unified member access node
// This node represents an access of the form "target.member" (without parentheses).
// Later in the ASTBuilder we decide whether the target is the literal `this` (to yield a ThisAccess)
// or a class name (to yield a ClassAccess) if needed.

// Represents `ClassName.member`
case class MemberAccess(target: Expression, memberName: String) extends Expression

// Represents `this.member`
case class ThisAccess(name: String) extends Expression

case class NewObject(className: String, arguments: List[Expression]) extends Expression
case class NewArray(arrayType: Type, dimensions: List[Expression]) extends Expression
case class ArrayAccess(array: Expression, index: Expression) extends Expression

// Method calls (member access with parentheses)
case class MethodCall(target: Expression, methodName: String, args: List[Expression]) extends Expression

case class TypedExpression(expr: Expression, exprType: Type) extends Expression
