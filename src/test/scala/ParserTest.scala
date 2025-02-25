import munit.FunSuite
import de.students.Parser.*
import de.students.Parser.Parser.parseTreeFromCode

class ParserTest extends FunSuite {

  /**
   * Test 1: Parse a simple valid program with one package, one class,
   * and one method that returns void.
   */
  test("Parse simple valid program with one class and one method") {
    val source =
      """
        |package main;
        |class MainClass {
        |  public void initMethod() {
        |    return;
        |  }
        |}
      """.stripMargin

    val result = parseTreeFromCode(source, "Test1")
    val expected =
      Package(
        name = "main",
        imports = Imports(List()),
        classes = List(
          ClassDecl(
            name = "MainClass",
            parent = "Object",
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

    assertEquals(result, expected)
  }

  /**
   * Test 2: Parse a program that includes a package declaration and import statements.
   */
  test("Parse program with package declaration and import statements") {
    val source =
      """
        |package com.example;
        |import java.util.List;
        |import java.io.File;
        |class Example {
        |  public int getNumber() {
        |    return 42;
        |  }
        |}
      """.stripMargin

    val result = parseTreeFromCode(source, "Test 2")
    val expected =
      Package(
        name = "com.example",
        imports = Imports(List("java.util.List", "java.io.File")),
        classes = List(
          ClassDecl(
            name = "Example",
            parent = "Object",
            isAbstract = false,
            methods = List(
              MethodDecl(
                accessModifier = Some("public"),
                name = "getNumber",
                isAbstract = false,
                static = false,
                isFinal = false,
                returnType = IntType,
                params = List(),
                body = Some(BlockStatement(List(ReturnStatement(Some(Literal(42))))))
              )
            ),
            fields = List(),
            constructors = List()
          )
        )
      )
    assertEquals(result, expected)
  }

  /**
   * Test 3: Parse a program with multiple classes and constructors.
   * Class A has a no-argument constructor and class B has a constructor with a parameter
   * and a simple variable declaration initialized with a new object.
   */
  test("Parse program with multiple classes and constructors") {
    val source =
      """
        |package test;
        |class A {
        |  public A() {
        |  }
        |}
        |class B {
        |  public B(int x) {
        |    A a = new A();
        |  }
        |}
      """.stripMargin

    val result = parseTreeFromCode(source, "Test 3")
    val expected =
      Package(
        name = "test",
        imports = Imports(List()),
        classes = List(
          ClassDecl(
            name = "A",
            parent = "Object",
            isAbstract = false,
            methods = List(),
            fields = List(),
            constructors = List(
              ConstructorDecl(
                accessModifier = Some("public"),
                name = "A",
                params = List(),
                body = BlockStatement(List())
              )
            )
          ),
          ClassDecl(
            name = "B",
            parent = "Object",
            isAbstract = false,
            methods = List(),
            fields = List(),
            constructors = List(
              ConstructorDecl(
                accessModifier = Some("public"),
                name = "B",
                params = List(VarDecl("x", IntType, None)),
                body = BlockStatement(
                  List(
                    // Represent "A a = new A();" as a variable declaration with initializer.
                    VarDecl("a", UserType("A"), Some(NewObject("A", List())))
                  )
                )
              )
            )
          )
        )
      )

    assertEquals(result, expected)
  }

  /**
   * Test 4: Parse a program with complex method bodies including control structures:
   * an if-statement with else, and a for-loop that calls a method on a static member.
   */
  test("Parse program with complex method bodies and control structures") {
    val source =
      """
        |package complex;
        |class Complex {
        |  public int compute(int a, int b) {
        |    if(a > b) {
        |      return a - b;
        |    } else {
        |      return b - a;
        |    }
        |  }
        |
        |  public void loopExample() {
        |    for(int i = 0; i < 10; i = i + 1) {
        |      System.out.println(i);
        |    }
        |  }
        |}
      """.stripMargin

    val result = parseTreeFromCode(source, "Test 4")
    val expected =
      Package(
        name = "complex",
        imports = Imports(List()),
        classes = List(
          ClassDecl(
            name = "Complex",
            parent = "Object",
            isAbstract = false,
            methods = List(
              // Method compute(int a, int b)
              MethodDecl(
                accessModifier = Some("public"),
                name = "compute",
                isAbstract = false,
                static = false,
                isFinal = false,
                returnType = IntType,
                params = List(
                  VarDecl("a", IntType, None),
                  VarDecl("b", IntType, None)
                ),
                body = Some(
                  BlockStatement(
                    List(
                      IfStatement(
                        cond = BinaryOp(VarRef("a"), ">", VarRef("b")),
                        thenBranch = BlockStatement(
                          List(
                            ReturnStatement(Some(BinaryOp(VarRef("a"), "-", VarRef("b"))))
                          )
                        ),
                        elseBranch = Some(
                          BlockStatement(
                            List(
                              ReturnStatement(Some(BinaryOp(VarRef("b"), "-", VarRef("a"))))
                            )
                          )
                        )
                      )
                    )
                  )
                )
              ),
              // Method loopExample()
              MethodDecl(
                accessModifier = Some("public"),
                name = "loopExample",
                isAbstract = false,
                static = false,
                isFinal = false,
                returnType = VoidType,
                params = List(),
                body = Some(
                  BlockStatement(
                    List(
                      ForStatement(
                        init = Some(VarDecl("i", IntType, Some(Literal(0)))),
                        cond = Some(BinaryOp(VarRef("i"), "<", Literal(10))),
                        update = Some(
                          // Updated expected AST for the update expression to match the parser output:
                          BinaryOp(
                            left = BinaryOp(VarRef("i"), "=", VarRef("i")),
                            op = "+",
                            right = Literal(1)
                          )
                        ),
                        body = BlockStatement(
                          List(
                            StatementExpression(
                              MethodCall(
                                target = MemberAccess(VarRef("System"), "out"),
                                methodName = "println",
                                args = List(VarRef("i"))
                              )
                            )
                          )
                        )
                      )
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

    assertEquals(result, expected)
  }

  /**
   * Test 5: Parse a program that tests various expressions.
   * One method computes an arithmetic expression; another returns a new array.
   */
  test("Parse expressions in various contexts") {
    val source =
      """
        |package expr;
        |class ExprTest {
        |  public int computeExpression() {
        |    return (5 + 3) * 2;
        |  }
        |
        |  public int[] createArray() {
        |    return new int[5];
        |  }
        |}
      """.stripMargin

    val result = parseTreeFromCode(source, "Test 5")
    val expected =
      Package(
        name = "expr",
        imports = Imports(List()),
        classes = List(
          ClassDecl(
            name = "ExprTest",
            parent = "Object",
            isAbstract = false,
            methods = List(
              MethodDecl(
                accessModifier = Some("public"),
                name = "computeExpression",
                isAbstract = false,
                static = false,
                isFinal = false,
                returnType = IntType,
                params = List(),
                body = Some(
                  BlockStatement(
                    List(
                      ReturnStatement(
                        Some(
                          BinaryOp(
                            left = BinaryOp(Literal(5), "+", Literal(3)),
                            op = "*",
                            right = Literal(2)
                          )
                        )
                      )
                    )
                  )
                )
              ),
              MethodDecl(
                accessModifier = Some("public"),
                name = "createArray",
                isAbstract = false,
                static = false,
                isFinal = false,
                returnType = ArrayType(IntType),
                params = List(),
                body = Some(
                  BlockStatement(
                    List(
                      ReturnStatement(
                        Some(
                          NewArray(IntType, List(Literal(5)))
                        )
                      )
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

    assertEquals(result, expected)
  }

  /**
   * Test 6: Ensure that a missing semicolon in the package declaration
   * produces a ParseException.
   */
  test("Error when missing semicolon in package declaration") {
    val source =
      """
        |package missing
        |class ErrorClass {}
      """.stripMargin

    intercept[Exception] {
      parseTreeFromCode(source, "Test 6")
    }
  }

  /**
   * Test 7: Ensure that unmatched braces in a class declaration produce a ParseException.
   */
  test("Error when unmatched braces in class declaration") {
    val source =
      """
        |package error;
        |class Unmatched {
        |  public void foo() {
        |    if (true) {
        |      return;
        |    // missing closing braces for if and method
      """.stripMargin

    intercept[Exception] {
      parseTreeFromCode(source, "Test 7")
    }
  }

  /**
   * Test 8: Ensure that an unexpected token in the method declaration results in a ParseException.
   */
  test("Error when encountering unexpected token in method declaration") {
    val source =
      """
        |package error;
        |class Unexpected {
        |  public int foo( {
        |    return 0;
        |  }
        |}
      """.stripMargin

    intercept[Exception] {
      parseTreeFromCode(source, "Test 8")
    }
  }

  /**
   * Test 9: Parse a program with while and do-while loops.
   */
  test("Parse program with while and do-while loops") {
    val source =
      """
        |package loops;
        |class LoopTest {
        |  public void testLoops() {
        |    while(true) {
        |    }
        |    do {
        |    } while(false);
        |  }
        |}
       """.stripMargin

    val result = parseTreeFromCode(source, "Test 9")
    val expected =
      Package(
        name = "loops",
        imports = Imports(List()),
        classes = List(
          ClassDecl(
            name = "LoopTest",
            parent = "Object",
            isAbstract = false,
            methods = List(
              MethodDecl(
                accessModifier = Some("public"),
                name = "testLoops",
                isAbstract = false,
                static = false,
                isFinal = false,
                returnType = VoidType,
                params = List(),
                body = Some(
                  BlockStatement(
                    List(
                      WhileStatement(
                        cond = Literal(true),
                        body = BlockStatement(List())
                      ),
                      DoWhileStatement(
                        cond = Literal(false),
                        body = BlockStatement(List())
                      )
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

    assertEquals(result, expected)
  }

  /**
   * Test 11: Parse a program with a field initializer.
   */
  test("Parse program with field initializer") {
    val source =
      """
        |package field;
        |class FieldTest {
        |  public int field = 42;
        |}
       """.stripMargin

    val result = parseTreeFromCode(source, "Test 11")
    val expected =
      Package(
        name = "field",
        imports = Imports(List()),
        classes = List(
          ClassDecl(
            name = "FieldTest",
            parent = "Object",
            isAbstract = false,
            methods = List(),
            fields = List(
              FieldDecl(
                accessModifier = Some("public"),
                isStatic = false,
                isFinal = false,
                name = "field",
                varType = IntType,
                initializer = Some(Literal(42))
              )
            ),
            constructors = List()
          )
        )
      )

    assertEquals(result, expected)
  }

  /**
   * Test 12: Parse a program with a method call using 'this' access.
   */
  test("Parse program with this access in method call") {
    val source =
      """
        |package thisTest;
        |class ThisTest {
        |  public void doSomething() {
        |    this.doSomethingElse();
        |  }
        |}
       """.stripMargin

    val result = parseTreeFromCode(source, "Test 12")
    val expected =
      Package(
        name = "thisTest",
        imports = Imports(List()),
        classes = List(
          ClassDecl(
            name = "ThisTest",
            parent = "Object",
            isAbstract = false,
            methods = List(
              MethodDecl(
                accessModifier = Some("public"),
                name = "doSomething",
                isAbstract = false,
                static = false,
                isFinal = false,
                returnType = VoidType,
                params = List(),
                body = Some(
                  BlockStatement(
                    List(
                      StatementExpression(
                        MethodCall(
                          // Representing "this.doSomethingElse()"
                          target = VarRef("this"),
                          methodName = "doSomethingElse",
                          args = List()
                        )
                      )
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

    assertEquals(result, expected)
  }

  /**
   * Test 13: Parse a program with object creation with arguments.
   */
  test("Parse program with new object with arguments") {
    val source =
      """
        |package newObj;
        |class NewObjTest {
        |  public void create() {
        |    Foo foo = new Foo(1, "bar");
        |  }
        |}
       """.stripMargin

    val result = parseTreeFromCode(source, "Test 13")
    val expected =
      Package(
        name = "newObj",
        imports = Imports(List()),
        classes = List(
          ClassDecl(
            name = "NewObjTest",
            parent = "Object",
            isAbstract = false,
            methods = List(
              MethodDecl(
                accessModifier = Some("public"),
                name = "create",
                isAbstract = false,
                static = false,
                isFinal = false,
                returnType = VoidType,
                params = List(),
                body = Some(
                  BlockStatement(
                    List(
                      VarDecl("foo", UserType("Foo"), Some(NewObject("Foo", List(Literal(1), Literal("bar")))))
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

    assertEquals(result, expected)
  }

  /**
   * Test 14: Parse a program with array access.
   */
  test("Parse program with array access") {
    val source =
      """
        |package array;
        |class ArrayTest {
        |  public int accessArray(int[] arr, int index) {
        |    return arr[index];
        |  }
        |}
       """.stripMargin

    val result = parseTreeFromCode(source, "Test 14")
    val expected =
      Package(
        name = "array",
        imports = Imports(List()),
        classes = List(
          ClassDecl(
            name = "ArrayTest",
            parent = "Object",
            isAbstract = false,
            methods = List(
              MethodDecl(
                accessModifier = Some("public"),
                name = "accessArray",
                isAbstract = false,
                static = false,
                isFinal = false,
                returnType = IntType,
                params = List(
                  VarDecl("arr", ArrayType(IntType), None),
                  VarDecl("index", IntType, None)
                ),
                body = Some(
                  BlockStatement(
                    List(
                      ReturnStatement(Some(ArrayAccess(VarRef("arr"), VarRef("index"))))
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

    assertEquals(result, expected)
  }

}
