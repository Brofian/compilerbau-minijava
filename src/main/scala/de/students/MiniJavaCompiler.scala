package de.students

import Parser.{Package, Parser}
import de.students.semantic.SemanticCheck
import de.students.util.{ArgParser, InputOutput, Logger}

object MiniJavaCompiler {

  def main(args: Array[String]): Unit = {

    ArgParser.parseCommandLineArgs(args)

    if (ArgParser.filesToCompile.isEmpty) {
      Logger.info("Nothing to do, exiting...")
      return
    }

    // Run the scanner and parser
    val input = InputOutput.getInput(ArgParser.filesToCompile)
    /* val astProgram: Package */ Parser.main(input)

    // Create the AST from the parse-tree
    val astProgram: Package = Package("", List()) // TODO: AST, generated from parse-tree

    // Run the semantic- and type-check
    val typedAst = SemanticCheck.runCheck(astProgram)

    // Translate the typed AST into bytecode
    // TODO

  }

}