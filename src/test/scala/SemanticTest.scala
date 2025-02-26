import munit.FunSuite
import de.students.Parser._
import de.students.semantic.{SemanticCheck, SemanticException}

class SemanticTest extends FunSuite {

  /**
   * Ensure a simple test program passes semantic checking
   */
  test("Running Semantic check on simple example") {

    val program = Project(
      List(
        JavaFile(
          "main",
          Imports(List()),
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
      )
    )

    SemanticCheck.runCheck(program)
  }

  /**
   * Ensure an error is thrown if a method with return type A attempts to return a value of type B,
   * where B is not a subtype of A
   */
  test("Detecting wrong return type in methods") {

    intercept[SemanticException] { // Expect a SemanticException to occur
      val program = Project(
        List(
          JavaFile(
            "main",
            Imports(List()),
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
        )
      )

      SemanticCheck.runCheck(program)
    }
  }

  /**
   * Ensure an error is thrown if a non-accessible member is called
   */
  test("Respecting access modifiers") {

    def sampleWithAccessModifier(accessModifier: Option[String], useSamePackage: Boolean): Project = {
      val secondPackage = if useSamePackage then "first" else "second"
      Project(
        List(
          JavaFile(
            packageName = "first",
            imports = Imports(List()),
            classes = List(
              ClassDecl(
                name = "A",
                parent = "Object",
                isAbstract = false,
                methods = List(),
                fields = List(
                  FieldDecl(accessModifier, false, false, "prop", IntType, None)
                ),
                constructors = List()
              )
            )
          ),
          JavaFile(
            packageName = secondPackage,
            imports = Imports(List("first.A")),
            classes = List(
              ClassDecl(
                name = "B",
                parent = "Object",
                isAbstract = false,
                methods = List(
                  MethodDecl(
                    accessModifier = None,
                    name = "main",
                    isAbstract = false,
                    static = false,
                    isFinal = false,
                    returnType = VoidType,
                    params = List(),
                    body = Some(
                      BlockStatement(
                        List(
                          VarDecl("a", UserType("A"), None),
                          StatementExpression(MemberAccess(VarRef("a"), "prop"))
                        )
                      )
                    )
                  )
                ),
                fields = List(),
                constructors = List()
              )
            )
          )
        )
      )
    }

    val configs: List[(Option[String], Boolean, Boolean)] = List(
      // (accessModifier, useSamePackage,   expectException)
      (Some("private"), false, true),
      (Some("protected"), false, true),
      (Some("public"), false, false),
      (None, false, true),
      (Some("private"), true, true),
      (Some("protected"), true, false),
      (Some("public"), true, false),
      (None, true, false)
    );

    configs.foreach(config => {
      val (accessModifier, useSamePackage, expectException) = config
      val testProgram = sampleWithAccessModifier(accessModifier, useSamePackage)
      if (expectException) {
        intercept[SemanticException] {
          SemanticCheck.runCheck(testProgram)
        }
      } else {
        SemanticCheck.runCheck(testProgram)
      }
    })
  }
}
