grammar Java;

// Package Definition
package: PACKAGE id SC class  ;

// Class Definitions
class: CLASS id classbody
     | CLASS id EXTENDS id classbody ;

classbody: '{' (method | attribute)* '}';

// Methods
defaultMethod: RETURN returntype IDENTIFIER '(' parameterList? ')' methodBody;
staticMethod : modifier STATIC returntype IDENTIFIER '(' parameterList? ')' methodBody;

method: staticMethod | defaultMethod;

// Attributes
attribute: optionalModifier type IDENTIFIER ('=' expression)? SC;

// Modifiers
modifier: PRIVATE | PUBLIC | PROTECTED | STATIC;
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

// Method Body
methodBody: '{' block* '}';

block : statement | expression;

// Statements
statement: variableDeclaration
         | expressionStatement
         | returnStatement
         | ifStatement
         | whileStatement;

variableDeclaration: type IDENTIFIER ('=' expression)? SC;
expressionStatement: expression SC;
returnStatement: RETURN expression? SC;
ifStatement: 'if' '(' expression ')' statement ('else' statement)?;
whileStatement: 'while' '(' expression ')' statement;

// Expressions
expression: IDENTIFIER
          | literal
          | expression operator expression
          | '(' expression ')';

// Operators
operator: '+' | '-' | '*' | '/' | '==' | '!=' | '<' | '>' | '<=' | '>=';

// Literals
literal: INTEGER_LITERAL | CHAR_LITERAL  | BOOLEAN_LITERAL;

// Keywords
CLASS: 'class';
EXTENDS: 'extends';
PACKAGE: 'package';
PUBLIC: 'public';
PRIVATE: 'private';
PROTECTED: 'protected';
STATIC: 'static';
VOID: 'void';
RETURN: 'return';

// Primitive Types
PRIMITIVE_TYPE: 'int' | 'char' | 'boolean' ;

// Literals
INTEGER_LITERAL: [0-9]+;
CHAR_LITERAL: '\'' . '\'';
//STRING_LITERAL: '"' .*? '"';
BOOLEAN_LITERAL: 'true' | 'false';

// Identifiers
id: IDENTIFIER;
IDENTIFIER: [a-zA-Z_$][a-zA-Z0-9_$]*;

SC : ';';

// Whitespace and Comments
WS: [ \t\r\n]+ -> skip;
COMMENT: '//' ~[\r\n]* -> skip;
MULTILINE_COMMENT: '/*' .*? '*/' -> skip;