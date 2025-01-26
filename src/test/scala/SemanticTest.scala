import munit.FunSuite
import de.students.Parser._
import de.students.semantic.{SemanticCheck, SemanticException}

class SemanticTest extends FunSuite {

  /**
   * Ensure a simple test program passes semantic checking
   */
  test("Running Semantic check on simple example") {

    val program = Project(List(
      Package("main", Imports(List()),
        List(
          ClassDecl(
            name = "MainClass",
            parent = "java.lang.Object",
            isAbstract = false,
            methods = List(
              MethodDecl(
                accessModifier = Some("public"),
                name = "initMethod",
                isAbstract = false,
                static = false,
                isFinal = false,
                returnType = VoidType,
                params = List(),
                body = Some(BlockStatement(List(ReturnStatement(None))))
              )
            ),
            fields = List(),
            constructors = List()
          )
        )
      )
    ))

    SemanticCheck.runCheck(program)
  }

  /**
   * Ensure an error is thrown if a method with return type A attempts to return a value of type B,
   * where B is not a subtype of A
   */
  test("Detecting wrong return type in methods") {

    intercept[SemanticException] { // Expect a SemanticException to occur
      val program = Project(List(
        Package("main", Imports(List()),
          List(
            ClassDecl(
              name = "MainClass",
              parent = "",
              isAbstract = false,
              methods = List(
                MethodDecl(
                  accessModifier = Some("public"),
                  name = "initMethod",
                  isAbstract = false,
                  static = false,
                  isFinal = false,
                  returnType = BoolType,
                  params = List(),
                  body = Some(BlockStatement(List(ReturnStatement(None))))
                )
              ),
              fields = List(),
              constructors = List()
            )
          )
        )
      ))

      SemanticCheck.runCheck(program)
    }
  }
}