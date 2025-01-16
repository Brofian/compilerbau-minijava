package de.students.Parser

case class Project(packages: List[Package])


// basic node for all AST-trees
sealed trait ASTNode

// Program-node
case class Package(name: String, imports : Imports, classes: List[ClassDecl]) extends ASTNode

case class Imports(names: List[String]) extends ASTNode

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
                       body: Statement
                     ) extends ASTNode

// Constructor declaration
case class ConstructorDecl(
                            name: String,
                            params: List[VarDecl],
                            body: Statement
                          ) extends ASTNode

// variable declaration
case class VarDecl(name: String, varType: Type, initializer : Option[Expression]) extends Statement

// types
sealed trait Type extends ASTNode

case object NoneType extends Type // used for statements that do not evaluate to any type
case object IntType extends Type
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
case class ForStatement(init: Option[Statement], cond: Option[Expression], update: Option[Expression], body: Statement) extends Statement
case class DoWhileStatement(cond: Expression, body: Statement) extends Statement
case class SwitchStatement(expr: Expression, cases: List[SwitchCase], default: Option[DefaultCase]) extends Statement
case class SwitchCase(caseLit: Option[Literal], caseBlock: Statement) extends Statement
case class DefaultCase(caseBlock: Statement) extends Statement
case class StatementExpressions(expr: Expression) extends Statement
case class BreakStatement() extends Statement
case class ContinueStatement() extends Statement

case class TypedStatement(stmt: Statement, stmtType: Type) extends Statement


// expressions
sealed trait Expression extends ASTNode
case class VarRef(name: String) extends Expression
case class Literal(value: Any) extends Expression
case class BinaryOp(left: Expression, op: String, right: Expression) extends Expression

case class ThisAccess(name: String) extends Expression
case class ClassAccess(className: String, memberName: String) extends Expression
case class NewObject(className: String, arguments: List[Expression]) extends Expression
case class NewArray(arrayType: Type, dimensions: List[Expression]) extends Expression
case class ArrayAccess(array: Expression, index: Expression) extends Expression

case class MethodCall(target: Expression, methodName: String, args: List[Expression]) extends Expression
case class TypedExpression(expr: Expression, exprType: Type) extends Expression