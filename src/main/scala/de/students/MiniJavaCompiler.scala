package de.students

import Parser.{Parser, Project}
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
    val fileContents = InputOutput.getFileContents(ArgParser.filesToCompile)
    // Create the AST from the parse-tree
    val astProject: Project = Parser.main(fileContents.zip(ArgParser.filesToCompile))

    // Run the semantic- and type-check
    val typedAst = SemanticCheck.runCheck(astProject)

    Logger.debug(typedAst)

    // Translate the typed AST into bytecode
    // TODO

  }

}