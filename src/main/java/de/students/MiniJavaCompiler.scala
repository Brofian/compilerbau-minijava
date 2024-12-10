package de.students

import de.dhbw.horb.Compiler

object MiniJavaCompiler {

  def main(args: Array[String]): Unit = {

    // TODO load this from file, given by args
    val inputString =
      "def int add(int x, int y)\n" +
        "{\n" + "return x + y;\n" +
        "}\n" +
        "def int main()\n" +
        "{\n" +
        "int a;\n" +
        "a = 3;\n" +
        "return add(a, 2);\n" +
        "}"
    val ast = Compiler.generateAST(inputString)

    // TODO do something useful with the ast

    System.out.println("This should output the number two: " + ast.methods.size)

  }

}