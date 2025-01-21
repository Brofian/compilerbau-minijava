package de.students

import de.students.Parser.*

object MiniJavaCompiler {

  def main(args: Array[String]): Unit = {

    if(args.isEmpty && false) {
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
              "main",
              true, // static
              false, // isAbstract
              VoidType,
              List(VarDecl("args", ArrayType(UserType("java/lang/String")), None)),
              BlockStatement(List(
                // VarDecl("testVar", IntType, Some(Literal(3))),
                PrintStatement(TypedExpression(VarRef("t"), IntType)),
                ReturnStatement(None)
              ))
            ),
            MethodDecl(
              "foo",
              false,
              false,
              VoidType,
              List(VarDecl("a", IntType, None), VarDecl("b", IntType, None)),
              BlockStatement(List(
                ReturnStatement(None)
              ))
            )
          ),
          List(VarDecl("t", IntType, None)),
          List() // constructors
        )
      )
    )

    val bytecode = ByteCodeGenerator.generateBytecode(testProg)
    io.writeToBinFile(bytecode.head, "test.class")
  }

}