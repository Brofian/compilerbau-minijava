\* Every Node implements the shared ASTNode interface
```mermaid
classDiagram
    %% Root Node
    class Project {
      +List~Package~ packages
    }
    Project --> "*" Package : packages

    %% Package & Imports
    class Package {
      +String name
      +Imports imports
      +List~ClassDecl~ classes
    }
    Package --> "1" Imports : imports
    Package --> "*" ClassDecl : classes

    class Imports {
      +List~String~ names
    }

    %% Class Declaration and Members
    class ClassDecl {
      +String name
      +String parent
      +Boolean isAbstract
      +List~FieldDecl~ fields
      +List~MethodDecl~ methods
      +List~ConstructorDecl~ constructors
    }
    ClassDecl --> "*" FieldDecl : fields
    ClassDecl --> "*" MethodDecl : methods
    ClassDecl --> "*" ConstructorDecl : constructors

    class MethodDecl {
      +Option~String~ accessModifier
      +String name
      +Boolean isAbstract
      +Boolean static
      +Boolean isFinal
      +Type returnType
      +List~VarDecl~ params
      +Option~Statement~ body
    }
    MethodDecl --> "*" VarDecl : params
    MethodDecl --> BlockStatement : body

    class ConstructorDecl {
      +Option~String~ accessModifier
      +String name
      +List~VarDecl~ params
      +Statement body
    }
    ConstructorDecl --> "*" VarDecl : params

    class FieldDecl {
      +Option~String~ accessModifier
      +Boolean isStatic
      +Boolean isFinal
      +String name
      +Type varType
      +Option~Expression~ initializer
    }

    %% Statements Hierarchy (all extend ASTNode via Statement)
    class Statement {
      <<abstract>>
    }

    class BlockStatement {
      +List~Statement~ stmts
    }
    BlockStatement --|> Statement

    class VarDecl {
      +String name
      +Type varType
      +Option~Expression~ initializer
    }
    VarDecl --|> Statement

    class ReturnStatement {
      +Option~Expression~ expr
    }
    ReturnStatement --|> Statement

    class IfStatement {
      +Expression cond
      +Statement thenBranch
      +Option~Statement~ elseBranch
    }
    IfStatement --|> Statement

    class WhileStatement {
      +Expression cond
      +Statement body
    }
    WhileStatement --|> Statement

    class ForStatement {
      +Option~Statement~ init
      +Option~Expression~ cond
      +Option~Expression~ update
      +Statement body
    }
    ForStatement --|> Statement

    class DoWhileStatement {
      +Expression cond
      +Statement body
    }
    DoWhileStatement --|> Statement

    class PrintStatement {
      +Expression toPrint
    }
    PrintStatement --|> Statement

```
```mermaid
classDiagram

    %% Expression Hierarchy (all extend ASTNode via Expression)
    class Expression {
      <<abstract>>
    }

    class VarRef {
      +String name
    }
    VarRef --|> Expression

    class StaticClassRef {
      +String className
    }
    StaticClassRef --|> Expression

    class Literal {
      +Any value
    }
    Literal --|> Expression

    class BinaryOp {
      +Expression left
      +String op
      +Expression right
    }
    BinaryOp --|> Expression

    class UnaryOp {
      +String op
      +Expression expr
    }
    UnaryOp --|> Expression

    class MemberAccess {
      +Expression target
      +String memberName
    }
    MemberAccess --|> Expression

    class ThisAccess {
      +String name
    }
    ThisAccess --|> Expression

    class MethodCall {
      +Expression target
      +String methodName
      +List~Expression~ args
    }
    MethodCall --|> Expression

```
```mermaid
classDiagram

    %% Types Hierarchy
    class Type {
      <<abstract>>
    }

    class NoneType {
    }
    NoneType --|> Type

    class IntType {
    }
    IntType --|> Type

    class ShortType {
    }
    ShortType --|> Type

    class LongType {
    }
    LongType --|> Type

    class ByteType {
    }
    ByteType --|> Type

    class FloatType {
    }
    FloatType --|> Type

    class DoubleType {
    }
    DoubleType --|> Type

    class CharType {
    }
    CharType --|> Type

    class BoolType {
    }
    BoolType --|> Type

    class VoidType {
    }
    VoidType --|> Type

    class ArrayType {
      +Type baseType
    }
    ArrayType --|> Type

    class UserType {
      +String name
    }
    UserType --|> Type

    class FunctionType {
      +Type returnType
      +List~Type~ parameterTypes
    }
    FunctionType --|> Type
```