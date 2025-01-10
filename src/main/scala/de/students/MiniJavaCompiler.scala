package de.students

import Parser.{Parser, Package}
import de.students.InputOutput
import de.students.semantic.SemanticCheck

object MiniJavaCompiler {

  def main(args: Array[String]): Unit = {

    if(args.nonEmpty) {
      val io = new InputOutput
      val input = io.getInput(args)
      Parser.main(input)
    }else{
      testAll()
    }

    // input preparation
    // TODO: read the input arguments to determine the output file path, input file path(s) and options separately

    // Run the scanner and parser

    // Create the AST from the parse-tree
    val astProgram: Package = Package("", List()) // TODO: AST, generated from parse-tree

    // Run the semantic- and type-check
    val typedAst = SemanticCheck.runCheck(astProgram)

    // Translate the typed AST into bytecode
    // TODO

  }

  private def testAll(): Unit = {
    println("Testing these Files:  Arrays.java ===== Objects.Java =====  FullProgramm.java \n \n")
    println("-".repeat(50))

    val io = new InputOutput

    println("Testing Arrays.java: \n")
    Parser.main(io.getInput(Array("Arrays.java")))

    println( "\n \n" + "-".repeat(50))

    println("Testing Object.java: \n")
    Parser.main(io.getInput(Array("Objects.java")))

    println("\n \n" + "-".repeat(50))

    println("Testing FullProgramm.java: \n")
    Parser.main(io.getInput(Array("FullProgramm.java")))


  }

}