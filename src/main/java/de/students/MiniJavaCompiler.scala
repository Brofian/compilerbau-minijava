package de.students

import de.dhbw.horb.Compiler

import Parser.Parser

import de.students.InputOutput

object MiniJavaCompiler {

  def main(args: Array[String]): Unit = {
    if(args.nonEmpty) {
      var io = new InputOutput
      val input = io.getInput(args)
      Parser.main(input)
    }
  }



  // val ast = Compiler.generateAST(inputString)

  // TODO do something useful with the ast

  //System.out.println("This should output the number two: " + ast.methods.size)




}