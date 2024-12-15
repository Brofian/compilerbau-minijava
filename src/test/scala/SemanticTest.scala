import de.students.Parser.{BlockStatement, BoolType, ClassDecl, MethodDecl, Program, ReturnStatement, VoidType}
import de.students.semantic.{SemanticCheck, SemanticException}


class SemanticTest extends FunSuite {

  /**
   * Make sure, a simple test program will be checked successfully
   */
  test("Running Semantic check on simple example") {

    val program = Program(List(
      ClassDecl("MainClass", None,
        List(
          MethodDecl("initMethod", VoidType, List(), BlockStatement(
            List(
              ReturnStatement(None)
            )
          ))
        ),
        List())
    ))

    SemanticCheck.runCheck(program)
  }


  /**
   * Make sure an error is thrown, if a method with return type A attempts to return a value of
   * type B, while B is not a subtype of A
   */
  test("Detecting wrong return type in methods") {

    intercept[SemanticException] { // we expect a SemanticException to happen
      val program = Program(List(
        ClassDecl("MainClass", None,
          List(
            MethodDecl("initMethod", BoolType, List(), BlockStatement(
              List(
                ReturnStatement(None)
              )
            ))
          ),
          List())
      ))

      SemanticCheck.runCheck(program)
    }
  }

}