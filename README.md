# Compilerbau MiniJava
Source of the minijava compiler, created as a practical exam to the compilerbau course.

## Disclaimer
The contents of this repository are based on and extend the Antlr Code Template for the decaf language
provided by [...] DHBW-Stuttgart. 

## prerequisites
- Scala compiler
- Antlr4 generator
- Maven

## Usage
- Antlr compilation: use maven to compile the grammar definition into java code by executing
    `mvn antlr4:antlr4` in the project root (remember to mark the directory 
    `target/generated-sources/antlr4` as generated sources in your IDE)
- Execute the main method of `src/main/java/de/students/MiniJavaCompiler.scala`


## ToDo
- [ ] Read in file(s)
- [ ] Implement Antlr grammar (-> lexer and parser)
- [ ] Implement semantic check (generate typed ast)
- [ ] Implement byte code generation
- [ ] Write output bytes into file