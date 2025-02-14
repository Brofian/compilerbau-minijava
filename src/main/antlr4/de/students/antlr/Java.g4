grammar Java;

// Package Definition
package: PACKAGE packageId SC imports class+ ;

imports: (IMPORT packageId SC)* ;


// Class Definitions
class: accessModifier? classType id (EXTENDS id)? classbody;

classbody: '{' (method | attribute | constructor | class)* '}';

// Methods
method: accessModifier? STATIC? FINAL? ABSTRACT?  returntype IDENTIFIER '(' parameterList? ')' block?;

// Attributes
attribute: accessModifier? FINAL? type IDENTIFIER ('=' expression)? SC;

// Constructors
constructor: accessModifier? id '(' parameterList? ')' block;

// Modifiers
accessModifier: PRIVATE | PUBLIC | PROTECTED;  // Only one allowed


// Class Type
classType: CLASS | ABSTRACT CLASS; // Abstract classes must be explicitly defined

// Return Types
returntype: VOID | type;

// Types
type: PRIMITIVE_TYPE
    | id            // user-defined types
    | type '[]';    // array types

// Parameter List
parameterList: parameter (',' parameter)*;
parameter: type IDENTIFIER;

block: '{' statement* '}';

// Statements
statement: variableDeclaration
         | expressionStatement
         | returnStatement
         | ifStatement
         | whileStatement
         | forStatement
         | doWhileStatement
         | switchStatement
         | breakStatement
         | continueStatement;

variableDeclaration: type IDENTIFIER ('=' expression)? SC;

expressionStatement: expression SC;
returnStatement: RETURN expression? SC;

ifStatement: 'if' '(' expression ')'  block
              (elseifStatement)*
              (elseStatement)?;

elseifStatement: 'else if' '(' expression ')'  block ;
elseStatement: 'else' block ;

whileStatement: 'while' '(' expression ')' block;
doWhileStatement: 'do' block 'while' '(' expression ')' SC;
forStatement: 'for' '(' (variableDeclaration | expressionStatement | SC)
                 expression? SC
                 expression? ')' block;
switchStatement: 'switch' '(' expression ')' '{' switchCase* '}';
switchCase: 'case' literal ':' block
          | 'default' ':' block;
breakStatement: 'break' SC;
continueStatement: 'continue' SC;

// Expressions
// Here we split expressions into unary and binary forms, and then
// build a unified chain of member accesses (dot and array accesses)
expression
    : unaryExpression (operator unaryExpression)*
    ;

unaryExpression
    : ('!' | '-') unaryExpression
    | postfixExpression
    ;

// A postfix expression starts with a simple primary (identifier, literal, etc.)
// and then is followed by zero or more postfix operators (dot access or array access).
postfixExpression
    : simplePrimary (postfixOp)*
    ;

simplePrimary
    : IDENTIFIER ('(' argumentList? ')')?    // Implicit method call: e.g. calc(10) Variable reference or class name
    | THIS
    | literal
    | '(' expression ')'
    | objectCreation
    | arrayCreation
    ;

// Postfix operators: either a dot operator (with optional argument list for method calls)
// or an array access.
postfixOp
    : '.' IDENTIFIER ( '(' argumentList? ')' )?
    | '[' expression ']'
    ;

// Object creation
objectCreation
    : 'new' id '(' argumentList? ')'
    ;

// Array creation
arrayCreation
    : 'new' type ('[' expression ']')+
    ;

// Argument List
argumentList
    : expression (',' expression)*
    ;

// Operators
operator: '+' | '-' | '*' | '/' | '%'
        | '==' | '!=' | '<' | '<=' | '>' | '>='
        | '&&' | '||'
        | '=' | '+=' | '-=' | '*=' | '/=' | '%=';

// Literals
literal: INTEGER_LITERAL | BYTE_LITERAL | SHORT_LITERAL | LONG_LITERAL | DOUBLE_LITERAL | FLOAT_LITERAL | CHAR_LITERAL | BOOLEAN_LITERAL | STRING_LITERAL | NULL_LITERAL;
// Keywords
CLASS: 'class';
EXTENDS: 'extends';
PACKAGE: 'package';
PUBLIC: 'public';
PRIVATE: 'private';
PROTECTED: 'protected';
STATIC: 'static';
FINAL: 'final';
ABSTRACT: 'abstract';
VOID: 'void';
RETURN: 'return';
IMPORT: 'import';
THIS : 'this';
// Primitive Types
PRIMITIVE_TYPE: 'int' | 'char' | 'boolean' | 'byte' | 'double' | 'float' | 'short' | 'long';

// Literals
INTEGER_LITERAL: '-'? [0-9]+;
BYTE_LITERAL: ('0' | ('-'? [1-9] [0-9]*)) [bB];
SHORT_LITERAL: ('0' | ('-'? [1-9] [0-9]*)) [sS];
LONG_LITERAL: ('0' | ('-'? [1-9] [0-9]*)) [lL];
DOUBLE_LITERAL: ('0' | ('-'? [1-9] [0-9]*)) ('.' [0-9]+)? ('e' [+-]? [0-9]+)?;
FLOAT_LITERAL: ('0' | ('-'? [1-9] [0-9]*)) ('.' [0-9]+)? ('f' | 'F');
CHAR_LITERAL: '\'' . '\'';

STRING_LITERAL: '"' (~["\\\r\n] | '\\' .)* '"';
BOOLEAN_LITERAL: 'true' | 'false';
NULL_LITERAL: 'null';

// Identifiers
id: IDENTIFIER;
IDENTIFIER: [a-zA-Z_$][a-zA-Z0-9_$]*;
packageId: IDENTIFIER ('.' IDENTIFIER)*;

SC: ';';

// Whitespace and Comments
WS: [ \t\r\n]+ -> skip;
COMMENT: '//' ~[\r\n]* -> skip;
MULTILINE_COMMENT: '/*' .*? '*/' -> skip;
