package de.students

import de.dhbw.horb.Compiler

import Parser.Parser;

import de.students.InputOutput

object MiniJavaCompiler {

  def main(args: Array[String]): Unit = {


    // Check if arguments are provided
    if (args.isEmpty) {
      println("No input folder or files were specified in the arguments.")
      return
    }

    val io = new InputOutput
    val input = io.loadFiles(args)

    input match {
      case Some(content) => println(content)
      case None => println("No content could be loaded from the specified paths.")
    }
  }

  Parser.main()
  // val ast = Compiler.generateAST(inputString)

  // TODO do something useful with the ast

  //System.out.println("This should output the number two: " + ast.methods.size)




}