grammar Java;

// Package Definition
package: PACKAGE id SC class+ ;

// Class Definitions
class: PUBLIC? CLASS id (EXTENDS id)? classbody
     | PUBLIC? ABSTRACT id classbody;

classbody: '{' (method | attribute | constructor | class)* '}';

// Methods
method: modifier STATIC? returntype IDENTIFIER '(' parameterList? ')' block;

// Attributes
attribute: optionalModifier type IDENTIFIER ('=' expression)? SC;

// Constructors
constructor: PUBLIC? id '(' parameterList? ')' block;

// Modifiers
modifier: PRIVATE | PUBLIC | PROTECTED  | FINAL | ABSTRACT;
optionalModifier: modifier?;

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
expression: literal
          | primary
          | methodCall
          | thisAccess
          | arrayAccess
          | objectCreation
          | arrayCreation
          | '(' expression ')'
          | expression operator expression;

// Object creation
objectCreation: 'new' id '(' argumentList? ')';

// Array creation
arrayCreation: 'new' type ('[' expression ']')+; // Updated for multi-dimensional arrays

// Array access
arrayAccess: primary '[' expression ']'; // Accessing elements in arrays

// A primary expression can be an identifier, 'this', a class access, or a method call
primary: IDENTIFIER
       | thisAccess
       | classAccess
       | '(' expression ')'
       | objectCreation
       | arrayCreation;

// Method calls, allowing chaining without left recursion
methodCall: primary ('.' IDENTIFIER '(' argumentList? ')')*;

thisAccess: 'this' '.' IDENTIFIER;
classAccess: IDENTIFIER '.' IDENTIFIER;

argumentList: expression (',' expression)*;

// Operators
operator: '+' | '-' | '*' | '/' | '%'
        | '==' | '!=' | '<' | '<=' | '>' | '>='
        | '&&' | '||'
        | '=' | '+=' | '-=' | '*=' | '/=' | '%=';

// Literals
literal: INTEGER_LITERAL | CHAR_LITERAL | BOOLEAN_LITERAL | STRING_LITERAL | NULL_LITERAL;

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

// Primitive Types
PRIMITIVE_TYPE: 'int' | 'char' | 'boolean';

// Literals
INTEGER_LITERAL: [0-9]+;
CHAR_LITERAL: '\'' . '\'';
STRING_LITERAL: '"' (~["\\\r\n] | '\\' .)* '"';
BOOLEAN_LITERAL: 'true' | 'false';
NULL_LITERAL: 'null';

// Identifiers
id: IDENTIFIER;
IDENTIFIER: [a-zA-Z_$][a-zA-Z0-9_$]*;

SC: ';';

// Whitespace and Comments
WS: [ \t\r\n]+ -> skip;
COMMENT: '//' ~[\r\n]* -> skip;
MULTILINE_COMMENT: '/*' .*? '*/' -> skip;
