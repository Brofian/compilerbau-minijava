grammar Java;

package: PACKAGE id class ; //| 'package' id abstractclass ;

/*
abstractclass : 'abstract class' id abstractclassbody;
abstractclassbody: 'not started yet';
*/

class: CLASS id classbody | CLASS id EXTENDS id classbody ;
classbody: '{' method* attribute* '}';

method : modifier;
attribute : 'help';

modifier: PRIVATE | PUBLIC | PROTECTED | STATIC;
optionalModifier: modifier?;



id : IDENTIFIER;

// Keywords
CLASS : 'class';
EXTENDS : 'extends';
PACKAGE : 'package';
PUBLIC : 'public';
PRIVATE : 'private';
PROTECTED : 'protected';
STATIC : 'static';
VOID : 'void';
RETURN : 'return';
IDENTIFIER : [a-zA-Z_$][a-zA-Z0-9_$]*;
WS : [ \t\r\n] -> skip;

