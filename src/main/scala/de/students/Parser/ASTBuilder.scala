package de.students.Parser

import de.students
import de.students.Parser.*
import de.students.antlr.JavaParser.*
import de.students.antlr.{JavaBaseVisitor, JavaParser}
import de.students.util.Logger
import org.antlr.v4.runtime.tree.ParseTree

import scala.jdk.CollectionConverters.*

object ASTBuilder {

  class ASTGenerator extends JavaBaseVisitor[ASTNode] {

    // Generate the AST from the parse tree root
    def generateAST(tree: ParseTree): Package = {
      Logger.info(s"Generating AST from parse tree root")
      tree.accept(this).asInstanceOf[Package]
    }

    // Visit a package node and extract class declarations
    override def visitPackage(ctx: PackageContext): Package = {
      val packageName = ctx.packageId().getText
      Logger.debug(s"Visiting package: $packageName")

      val classDecls = ctx
        .class_()
        .asScala
        .map(visitClass) // Call visitClass for each class in the package
        .toList

      val imports = visitImports(ctx.imports())
      Logger.debug(
        s"Package: $packageName, Imports: ${imports.names.mkString(", ")}, Classes: ${classDecls.map(_.name).mkString(", ")}"
      )
      Package(packageName, imports, classDecls)
    }

    override def visitImports(ctx: ImportsContext): Imports = {
      Imports(
        ctx
          .importPackageId()
          .asScala
          .map { importCtx =>
            val diskreteImportPath = importCtx.IDENTIFIER().asScala.map(_.getText).mkString(".")
            // mark wildcard imports with a dot at the end
            val importPath =
              if importCtx.PACKAGE_WILDCARD() == null
              then diskreteImportPath
              else diskreteImportPath + "."
            Logger.debug(s"Visiting import $importPath ")
            importPath
          }
          .toList
      )

    }

    // Visit a class node, handling its name, inheritance, and body
    override def visitClass(ctx: ClassContext): ClassDecl = {
      val name = ctx.id(0).getText
      val maybeParent = Option(ctx.EXTENDS()).flatMap(_ => Option(ctx.id(1)).map(_.getText))
      val parent = maybeParent.getOrElse("Object")
      val isAbstract = ctx.classType().ABSTRACT() != null

      Logger.debug(s"Visiting class: $name, Parent: $parent, Is Abstract: $isAbstract")

      val methods = ctx.classbody().method().asScala.map(visitMethod).toList
      val fields = ctx.classbody().attribute().asScala.map(visitAttribute).toList
      val constructors = ctx.classbody().constructor().asScala.map(visitConstructor).toList
      ClassDecl(name, parent, isAbstract, methods, fields, constructors)
    }

    override def visitMethod(ctx: MethodContext): MethodDecl = {
      val name = ctx.IDENTIFIER().getText
      val isStatic = ctx.STATIC() != null
      val isAbstract = ctx.ABSTRACT() != null
      val isFinal = ctx.FINAL() != null
      val returnType = visitReturntype(ctx.returntype())
      val params =
        if ctx.parameterList() == null then List()
        else ctx.parameterList().parameter().asScala.map(visitParameter).toList
      val body =
        if ctx.block() != null then Option(visitBlockStmt(ctx.block()))
        else None // allowing empty body for abstract methods
      val accesModifier = visitModifiers(ctx.accessModifier())

      Logger.debug(s"Visiting method: $name, Static: $isStatic, Abstract: $isAbstract")

      MethodDecl(accesModifier, name, isAbstract, isStatic, isFinal, returnType, params, body)
    }

    override def visitConstructor(ctx: ConstructorContext): ConstructorDecl = {
      val name = ctx.id().getText // Get the constructor name
      val accessModifier = visitModifiers(ctx.accessModifier())

      // Parse parameters
      val params = if (ctx.parameterList() != null) {
        ctx.parameterList().parameter().asScala.map(visitParameter).toList
      } else {
        List() // No parameters
      }

      // Parse the method body: Iterate over each block and collect statements
      val body = visitBlockStmt(ctx.block())

      Logger.debug(s"Visiting constructor: $name, Parameters: $params, Body: $body")

      ConstructorDecl(accessModifier, name, params, body)
    }

    override def visitReturntype(ctx: ReturntypeContext): Type = {
      Logger.debug("Visiting return type")
      if (ctx.VOID() != null) {
        VoidType
      } else {
        visitType(ctx.`type`()) // This calls the visitType method below
      }
    }

    override def visitParameter(ctx: ParameterContext): VarDecl = {
      Logger.debug(s"Visiting parameter: ${ctx.IDENTIFIER().getText}")
      val paramType = visitType(ctx.`type`())
      val paramName = ctx.IDENTIFIER().getText
      VarDecl(paramName, paramType, None)
    }

    override def visitType(ctx: TypeContext): Type = {
      Logger.debug(s"Visiting type: ${ctx.getText}")
      if (ctx.PRIMITIVE_TYPE() != null) {
        ctx.PRIMITIVE_TYPE().getText match {
          case "int"     => IntType
          case "boolean" => BoolType
          case "void"    => VoidType
          case "short"   => ShortType
          case "long"    => LongType
          case "byte"    => ByteType
          case "float"   => FloatType
          case "double"  => DoubleType
          case "char"    => CharType
          case _         => throw new RuntimeException(s"Unknown primitive type: ${ctx.PRIMITIVE_TYPE().getText}")
        }
      } else if (ctx.id() != null) {
        UserType(ctx.id().getText) // User-defined type
      } else if (ctx.`type`() != null) {
        ArrayType(visitType(ctx.`type`())) // Array type
      } else {
        throw new RuntimeException("Invalid type")
      }
    }

    def visitBlockStmt(ctx: BlockContext): BlockStatement = {
      Logger.debug("Visiting block")
      val StmtList = ctx.children.asScala.flatMap {
        case statementCtx: StatementContext =>
          Some(visitStatement(statementCtx))
        case exprCtx: ExpressionContext =>
          Some(StatementExpression(visitExpression(exprCtx)))
        case _ =>
          None
      }.toList

      BlockStatement(StmtList)
    }

    override def visitStatement(ctx: StatementContext): Statement = {
      Logger.debug(s"Visiting statement: ${ctx.getText}")
      if (ctx.variableDeclaration() != null) {
        visitVariableDeclaration(ctx.variableDeclaration())
      } else if (ctx.expressionStatement() != null) {
        visitExpressionStatement(ctx.expressionStatement())
      } else if (ctx.returnStatement() != null) {
        visitReturnStatement(ctx.returnStatement())
      } else if (ctx.ifStatement() != null) {
        visitIfStatement(ctx.ifStatement())
      } else if (ctx.whileStatement() != null) {
        visitWhileStatement(ctx.whileStatement())
      } else if (ctx.doWhileStatement() != null) {
        visitDoWhileStatement(ctx.doWhileStatement())
      } else if (ctx.forStatement() != null) {
        visitForStatement(ctx.forStatement())
      } else {
        // Handle other types of statements
        throw new UnsupportedOperationException(s"Unsupported statement: ${ctx.getText}")
      }
    }

    override def visitForStatement(ctx: ForStatementContext): Statement = {
      Logger.debug(s"Visiting for statement: ${ctx.getText}")

      // Parse initialization (either a variable declaration, an expression statement, or empty)
      val init: Option[Statement] =
        if (ctx.variableDeclaration() != null) Some(visitVariableDeclaration(ctx.variableDeclaration()))
        else if (ctx.expressionStatement() != null) Some(visitExpressionStatement(ctx.expressionStatement()))
        else None

      // Parse condition (optional)
      val cond: Option[Expression] = Option(ctx.expression(0)).map(visitExpression)

      // Parse update expression (optional)
      val update: Option[Expression] =
        if (ctx.expression().size() > 1) Option(visitExpression(ctx.expression(1)))
        else None

      // Parse loop body
      val body: Statement = visitBlockStmt(ctx.block())

      Logger.debug(s"For loop - Init: $init, Condition: $cond, Update: $update, Body: $body")

      ForStatement(init, cond, update, body)
    }

    override def visitDoWhileStatement(ctx: DoWhileStatementContext): Statement = {
      // Get the condition (expression)
      val condition = visitExpression(ctx.expression())

      // Get the body (block of statements)
      val body = visitBlockStmt(ctx.block())

      // Print the visited while statement
      Logger.debug(s"Visiting do-while statement with condition: $condition")

      // Return the corresponding AST node for a while statement
      DoWhileStatement(condition, body)
    }

    override def visitWhileStatement(ctx: WhileStatementContext): Statement = {
      // Get the condition (expression)
      val condition = visitExpression(ctx.expression())

      // Get the body (block of statements)
      val body = visitBlockStmt(ctx.block())

      // Print the visited while statement
      Logger.debug(s"Visiting while statement with condition: $condition")

      // Return the corresponding AST node for a while statement
      WhileStatement(condition, body)
    }
    override def visitIfStatement(ctx: IfStatementContext): Statement = {
      val condition = visitExpression(ctx.expression())
      val thenBranch = visitBlockStmt(ctx.block())

      Logger.debug(s"Visiting if statement, condition: $condition")

      val elseIfBranches = ctx.elseifStatement().asScala.map(visitElseIf).toList
      val elseBranch = if (ctx.elseStatement() != null) {
        Some(visitElse(ctx.elseStatement()))
      } else {
        None
      }

      IfStatement(condition, thenBranch, elseBranch)
    }

    def visitElseIf(ctx: ElseifStatementContext): Statement = {
      val condition = visitExpression(ctx.expression())
      val thenBranch = visitBlockStmt(ctx.block())
      Logger.debug(s"Visiting else-if statement, condition: $condition")
      IfStatement(condition, thenBranch, None)
    }

    def visitElse(ctx: ElseStatementContext): BlockStatement = {
      Logger.debug("Visiting else statement")
      visitBlockStmt(ctx.block())
    }

    override def visitVariableDeclaration(ctx: VariableDeclarationContext): VarDecl = {
      Logger.debug(s"Visiting variable declaration: ${ctx.IDENTIFIER().getText}")
      val varType = visitType(ctx.`type`())
      val name = ctx.IDENTIFIER().getText
      val initializer = Option(ctx.expression()).map(visitExpression)
      VarDecl(name, varType, initializer)
    }

    override def visitReturnStatement(ctx: ReturnStatementContext): ReturnStatement = {
      val expr = if (ctx.expression() != null) Some(visitExpression(ctx.expression())) else None
      Logger.debug(s"Visiting return statement: ${expr}")
      ReturnStatement(expr)
    }

    override def visitExpression(ctx: ExpressionContext): Expression = {
      Logger.debug(s"Visiting expression: ${ctx.getText}")
      // Handle unary operators (!, -)
      if (ctx.getChildCount == 2 && (ctx.getChild(0).getText == "!" || ctx.getChild(0).getText == "-")) {
        val op = ctx.getChild(0).getText
        val expr = visitExpression(ctx.expression(0)) // Get the nested expression
        return UnaryOp(op, expr)
      }

      if (ctx.literal() != null) {
        visitLiteral(ctx.literal())
      } else if (ctx.primary() != null) {
        visitPrimary(ctx.primary())
      } else if (ctx.methodCall() != null) {
        visitMethodCall(ctx.methodCall())
      } else if (ctx.operator() != null) {
        visitBinaryOp(ctx)
      } else if (ctx.objectCreation() != null) {
        visitObjectCreation(ctx.objectCreation())
      } else if (ctx.arrayCreation() != null) {
        visitArrayCreation(ctx.arrayCreation())
      } else if (ctx.arrayAccess() != null) { // Handle array access
        visitArrayAccess(ctx.arrayAccess())
      } else {
        throw new UnsupportedOperationException(s"Unsupported expression: ${ctx.getText}")
      }
    }

    override def visitObjectCreation(ctx: ObjectCreationContext): NewObject = {
      val className = ctx.id().getText
      val arguments = if (ctx.argumentList() != null) {
        visitmyArgumentList(ctx.argumentList())
      } else {
        List()
      }

      Logger.debug(s"Visiting object creation: new $className(${arguments.mkString(", ")})")
      NewObject(className, arguments)
    }

    override def visitArrayAccess(ctx: ArrayAccessContext): ArrayAccess = {
      val arrayExpr = visitPrimary(ctx.primary())
      val index = visitExpression(ctx.expression())

      Logger.debug(s"Visiting array access: ${arrayExpr} with indix ${index}]")
      ArrayAccess(arrayExpr, index)
    }

    override def visitArrayCreation(ctx: ArrayCreationContext): NewArray = {
      val arrayType = visitType(ctx.`type`())
      val dimensions = ctx.expression().asScala.map(visitExpression).toList

      Logger.debug(s"Visiting array creation: new ${arrayType.toString}[${dimensions.mkString("][")}]")
      NewArray(arrayType, dimensions)
    }
    def visitBinaryOp(ctx: JavaParser.ExpressionContext): Expression = {
      val left = visitExpression(ctx.expression(0))
      val operator = ctx.getChild(1).getText
      val right = visitExpression(ctx.expression(1))

      Logger.debug(s"Visiting binary operation: $left $operator $right")
      BinaryOp(left, operator, right)
    }

    override def visitMethodCall(ctx: MethodCallContext): Expression = {
      var target = visitPrimary(ctx.primary()) // Base expression (like `new A()` or a variable)

      // Get all method calls in the chain (like `.getB()` and `.getNumber()`)
      val methodCalls = ctx.IDENTIFIER().asScala

      var finalTarget: Expression = target // This will hold the current expression being built

      // Iterate over each method call in the chain
      methodCalls.foreach { method =>
        val methodName = method.getText

        // If the argumentList exists, iterate over all argumentContexts in the list
        val arguments = if (ctx.argumentList() != null) {
          // Iterate over all argument lists and visit each expression
          ctx
            .argumentList()
            .asScala
            .flatMap { argListCtx =>
              // For each argument list, we get all its expressions and visit each one
              argListCtx.expression().asScala.map { exprCtx =>
                visitExpression(exprCtx) // Visit each expression context
              }
            }
            .toList
        } else {
          List() // No arguments, return an empty list
        }

        Logger.debug(s"Visiting method call: $methodName with arguments: $arguments")

        // Update the final target expression by wrapping it with the new method call
        finalTarget = MethodCall(finalTarget, methodName, arguments)
      }

      finalTarget // Return the final target expression after all method calls
    }

    def visitmyArgumentList(ctx: ArgumentListContext): List[Expression] = {
      Logger.debug("Visiting argument list")
      ctx.expression().asScala.map(visitExpression).toList
    }

    override def visitPrimary(ctx: PrimaryContext): Expression = {
      Logger.debug(s"Visiting primary expression: ${ctx.getText}")
      if (ctx.IDENTIFIER() != null) {
        return VarRef(ctx.IDENTIFIER().getText)
      }

      if (ctx.thisAccess() != null) {
        return ThisAccess(ctx.thisAccess().IDENTIFIER().getText)
      }

      if (ctx.classAccess() != null) {
        val className = ctx.classAccess().IDENTIFIER(0).getText
        val memberName = ctx.classAccess().IDENTIFIER(1).getText
        return ClassAccess(className, memberName)
      }

      if (ctx.expression() != null) {
        return visitExpression(ctx.expression())
      }
      if (ctx.objectCreation() != null) {
        return visitObjectCreation(ctx.objectCreation())
      }
      if (ctx.arrayCreation() != null) {
        return visitArrayCreation(ctx.arrayCreation())
      }

      throw new IllegalArgumentException("Unrecognized primary expression: " + ctx.getText)
    }

    override def visitExpressionStatement(ctx: ExpressionStatementContext): Statement = {
      val expr = visitExpression(ctx.expression())
      Logger.debug(s"Visiting expression statement: $expr")
      StatementExpression(expr)
    }

    override def visitLiteral(ctx: LiteralContext): Literal = {
      Logger.debug(s"Visiting literal: ${ctx.getText}")

      if (ctx.INTEGER_LITERAL() != null) {
        // Handle integer literals
        Literal(ctx.INTEGER_LITERAL().getText.toInt)
      } else if (ctx.STRING_LITERAL() != null) {
        // Handle string literals
        Literal(ctx.STRING_LITERAL().getText)
      } else if (ctx.BOOLEAN_LITERAL() != null) {
        // Handle boolean literals (true/false)
        ctx.BOOLEAN_LITERAL().getText match {
          case "true"  => Literal(true)
          case "false" => Literal(false)
          case _       => throw new UnsupportedOperationException(s"Unsupported boolean literal: ${ctx.getText}")
        }
      } else if (ctx.BYTE_LITERAL() != null) {
        // Handle byte literals
        Literal(ctx.BYTE_LITERAL().getText.dropRight(1).toByte) // Drop the 'b'/'B' suffix and convert to byte
      } else if (ctx.SHORT_LITERAL() != null) {
        // Handle short literals
        Literal(ctx.SHORT_LITERAL().getText.dropRight(1).toShort) // Drop the 's'/'S' suffix and convert to short
      } else if (ctx.LONG_LITERAL() != null) {
        // Handle long literals
        Literal(ctx.LONG_LITERAL().getText.dropRight(1).toLong) // Drop the 'l'/'L' suffix and convert to long
      } else if (ctx.FLOAT_LITERAL() != null) {
        // Handle float literals
        Literal(ctx.FLOAT_LITERAL().getText.dropRight(1).toFloat) // Drop the 'f'/'F' suffix and convert to float
      } else if (ctx.DOUBLE_LITERAL() != null) {
        // Handle double literals
        Literal(ctx.DOUBLE_LITERAL().getText.toDouble) // Double literals may have 'd'/'D' suffix which is optional
      } else if (ctx.CHAR_LITERAL() != null) {
        // Handle double literals
        Literal(ctx.CHAR_LITERAL().getText) // Char Literal
      } else {
        // If literal is not recognized
        throw new UnsupportedOperationException(s"Unsupported literal: ${ctx.getText}")
      }
    }

    override def visitAttribute(ctx: AttributeContext): FieldDecl = {
      // Log the attribute being visited
      Logger.debug(s"Visiting attribute: ${ctx.IDENTIFIER().getText}")

      // Handle modifiers
      val accessModifiers = visitModifiers(ctx.accessModifier())
      val isFinal = ctx.FINAL() != null
      // Get the type of the attribute
      val varType = visitType(ctx.`type`())

      // Get the name of the attribute
      val name = ctx.IDENTIFIER().getText

      // Handle the initializer if it exists (optional part of the attribute)
      val initializer = Option(ctx.expression()).map(visitExpression)

      // Create and return the VarDecl node with the modifiers, type, and initializer
      FieldDecl(accessModifiers, isFinal, name, varType, initializer)
    }

    // Visit an access modifier (PRIVATE, PUBLIC, PROTECTED)
    def visitModifiers(ctx: AccessModifierContext): Option[String] = {
      // Check if the access modifier is present, and return the corresponding string if found
      if ctx == null then return None // Access modifiers are optional
      if (ctx.PRIVATE() != null) {
        Some("private")
      } else if (ctx.PUBLIC() != null) {
        Some("public")
      } else if (ctx.PROTECTED() != null) {
        Some("protected")
      } else {
        // If no access modifier is found, return None
        None
      }
    }
  }

}
