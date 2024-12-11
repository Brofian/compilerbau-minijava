grammar Decaf;

program : (var | func)*;

var : type id ';';
type : INT | BOOL | VOID ;

func : 'def' type id '(' params? ')' block;
params : param (',' param)*;
param : type id;

block : '{' var* stmt* '}';

stmt : loc '=' expr ';'                         #Assign
    | funcCall ';'                              #FunctionCall
    | 'if' '(' expr ')' block ('else' block)?   #If
    | 'while' '(' expr ')' block                #While
    | 'return' expr ';'                         #Return
    | 'return' ';'                              #ReturnVoid
    | 'break' ';'                               #Break
    | 'continue' ';'                            #Continue
    ;

expr : expr binaryOp expr   #BinaryOperation
    | literal               #Constant
    | '(' expr ')'          #Expression
    | funcCall              #FunCallExpression
    | loc                   #Location
;

binaryOp : ADD | SUB | MUL;

loc : id ;

funcCall : id '(' args? ')';
args : expr (',' expr)*;

literal : number | boolean;

boolean : 'true' | 'false' ;

number : Number;
id : IDENTIFIER;

SUB : '-';
ADD : '+';
MUL : '*';

INT : 'int';
BOOL : 'bool';
VOID : 'void';

IDENTIFIER : [a-zA-Z]+;
Number : [0-9]+;
WS : [ \t\r\n] -> skip;
