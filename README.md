# Compilerbau MiniJava
Source of the minijava compiler, created as a practical exam to the compilerbau course.

## Disclaimer
The contents of this repository are based on and extend the Antlr Code Template for the decaf language
provided by [...] DHBW-Stuttgart. 

## prerequisites
- JDK (v21.x.x recommended)
- Scala and sbt

## Usage
- Make sure you have the FULL Antler4 jar file (not just the generator and not just the runtime) in the lib directory
- Run `sbt generateAntlrTask` to generate the parser code from the grammar file
- Run `sbt "run secondinput.java"` to compile and execute the scala code once OR
- Run `sbt "~run secondinput.java"` to compile and execute the scala code and rerun automatically on changes


## ToDo
- [x] Read in file(s)
- [x] Implement Antlr grammar (-> lexer and parser)
- [ ] Translate from ParseTree to AST
- [ ] Implement semantic check (generate typed ast)
- [ ] Implement byte code generation
- [ ] Write output bytes into file
