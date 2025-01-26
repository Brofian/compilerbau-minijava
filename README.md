# Compilerbau MiniJava
Source of the minijava compiler, created as a practical exam to the compilerbau course.

## prerequisites
- A working sbt installation in version >= 1.10.0

## Usage
- Run `sbt clean compile` to regenerate the parser code from the grammar file
- Run `sbt "run --help"` to get an overview over the usage and arguments of the compiler 
- Run `sbt "~run ..."` to compile and execute the scala code and rerun automatically on changes