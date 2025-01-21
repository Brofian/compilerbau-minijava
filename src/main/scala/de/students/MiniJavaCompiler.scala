package de.students

import de.students.Parser.*

object MiniJavaCompiler {

  def main(args: Array[String]): Unit = {

    if(args.isEmpty) {
      throw new RuntimeException("No input arguments given")
    }

    // input preparation
    // TODO: read the input arguments to determine the output file path, input file path(s) and options separately

    // Run the scanner and parser
    var io = new InputOutput
    // val input = io.getInput(args)
    // Parser.main(input)

    // Create the AST from the parse-tree
    // val astProgram = Package("test", List()) // TODO: AST, generated from parse-tree

    // Run the semantic- and type-check
    // val typedAst = SemanticCheck.runCheck(astProgram)

    // Translate the typed AST into bytecode
    // some testing
    val testProg = Package("p",
      List(
        ClassDecl(
          "test",
          "java/lang/Object",
          false, // isAbstract
          List(
            MethodDecl(
              "foo",
              false, // static
              false, // isAbstract
              IntType,
              List(),
              IfStatement(
                Literal(1),
                ReturnStatement(Some(TypedExpression(Literal(42), IntType))),
                Some(ReturnStatement(Some(TypedExpression(Literal(-42), IntType)))),
              )
            )
          ),
          List(),
          List() // constructors
        )
      )
    )

    val bytecode = ByteCodeGenerator.generateBytecode(testProg)
    io.writeToBinFile(bytecode.head, "test.class")
  }

}