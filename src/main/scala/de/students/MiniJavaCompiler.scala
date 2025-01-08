package de.students

import Parser.{Parser, Package}
import de.students.InputOutput
import de.students.semantic.SemanticCheck

object MiniJavaCompiler {

  def main(args: Array[String]): Unit = {

    if(args.isEmpty) {
      throw new RuntimeException("No input arguments given")
    }

    // input preparation
    // TODO: read the input arguments to determine the output file path, input file path(s) and options separately

    // Run the scanner and parser
    var io = new InputOutput
    val input = io.getInput(args)
    Parser.main(input)

    // Create the AST from the parse-tree
    val astProgram: Package = Package("", List()) // TODO: AST, generated from parse-tree

    // Run the semantic- and type-check
    val typedAst = SemanticCheck.runCheck(astProgram)

    // Translate the typed AST into bytecode
    // TODO

  }

}