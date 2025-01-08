package de.students.Parser

import de.students.Parser._
import de.students.antlr.JavaParser._
import de.students.antlr.{JavaBaseVisitor, JavaParser}
import org.antlr.v4.runtime.tree.ParseTree
import scala.jdk.CollectionConverters._

object ASTBuilder {

  class ASTGenerator extends JavaBaseVisitor[ASTNode] {

    // Generate the AST from the parse tree root
    def generateAST(tree: ParseTree): Package = {
      println("Generating AST from parse tree root")
      tree.accept(this).asInstanceOf[Package]
    }

    // Visit a package node and extract class declarations
    override def visitPackage(ctx: PackageContext): Package = {
      val packageName = ctx.id().getText
      println(s"Visiting package: $packageName")

      val classDecls = ctx.class_()
        .asScala
        .map(visitClass) // Call visitClass for each class in the package
        .toList

      println(s"Package: $packageName, Classes: ${classDecls.map(_.name).mkString(", ")}")
      Package(packageName, classDecls)
    }

    // Visit a class node, handling its name, inheritance, and body
    override def visitClass(ctx: ClassContext): ClassDecl = {
      val name = ctx.id(0).getText
      val maybeParent = Option(ctx.EXTENDS()).flatMap(_ => Option(ctx.id(1)).map(_.getText))
      val parent = maybeParent.getOrElse("Object")
      val isAbstract = ctx.ABSTRACT() != null

      println(s"Visiting class: $name, Parent: $parent, Is Abstract: $isAbstract")

      val methods = ctx.classbody().method().asScala.map(visitMethod).toList
      val fields = ctx.classbody().attribute().asScala.map(visitAttribute).toList
      val constructors = ctx.classbody().constructor().asScala.map(visitConstructor).toList
      ClassDecl(name, parent, isAbstract, methods , fields, constructors)
    }

    override def visitMethod(ctx: MethodContext): MethodDecl = {
      val name = ctx.IDENTIFIER().getText
      val isStatic = ctx.STATIC() != null
      val isAbstract = ctx.modifier().ABSTRACT() != null
      val returnType = visitReturntype(ctx.returntype()) // A helper function for return type
      val params = ctx.parameterList().parameter().asScala.map(visitParameter).toList
      val body = visitmyMethodBody(ctx.methodBody())

      println(s"Visiting method: $name, Static: $isStatic, Abstract: $isAbstract")

      // MethodDecl(name, isStatic, isAbstract, returnType, params, body)
      ???
    }

    override def visitConstructor(ctx: ConstructorContext): ConstructorDecl = {
      val name = ctx.id().getText // Get the constructor name

      // Parse parameters
      val params = if (ctx.parameterList() != null) {
        ctx.parameterList().parameter().asScala.map(visitParameter).toList
      } else {
        List() // No parameters
      }

      // Parse the method body: Iterate over each block and collect statements
      val body = if (ctx.methodBody() != null) {
        ctx.methodBody().block().asScala.flatMap(visitmyBlock).toList
      } else {
        List() // Empty body
      }

      println(s"Visiting constructor: $name, Parameters: $params, Body: $body")

      ConstructorDecl(name, params, body)
    }



    override def visitReturntype(ctx: ReturntypeContext): Type = {
      println("Visiting return type")
      if (ctx.VOID() != null) {
        VoidType
      } else {
        visitType(ctx.`type`()) // This calls the visitType method below
      }
    }

    override def visitParameter(ctx: ParameterContext): VarDecl = {
      println(s"Visiting parameter: ${ctx.IDENTIFIER().getText}")
      val paramType = visitType(ctx.`type`())
      val paramName = ctx.IDENTIFIER().getText
      VarDecl(paramName, paramType, None)
    }

    override def visitType(ctx: TypeContext): Type = {
      println(s"Visiting type: ${ctx.getText}")
      if (ctx.PRIMITIVE_TYPE() != null) {
        ctx.PRIMITIVE_TYPE().getText match {
          case "int" => IntType
          case "boolean" => UserType("boolean")
          case "char" => UserType("char")
          case _ => throw new RuntimeException(s"Unknown primitive type: ${ctx.PRIMITIVE_TYPE().getText}")
        }
      } else if (ctx.id() != null) {
        UserType(ctx.id().getText) // User-defined type
      } else if (ctx.`type`() != null) {
        ArrayType(visitType(ctx.`type`())) // Array type
      } else {
        throw new RuntimeException("Invalid type")
      }
    }

    def visitmyMethodBody(ctx: MethodBodyContext): List[Statement] = {
      println("Visiting method body")
      ctx.block().asScala.flatMap { blockCtx =>
        blockCtx.statement().asScala.map(visitStatement) ++
          blockCtx.expression().asScala.map(visitExpression).map(StatementExpression)
      }.toList
    }

    override def visitStatement(ctx: StatementContext): Statement = {
      println(s"Visiting statement: ${ctx.getText}")
      if (ctx.variableDeclaration() != null) {
        visitVariableDeclaration(ctx.variableDeclaration())
      } else if (ctx.expressionStatement() != null) {
        visitExpressionStatement(ctx.expressionStatement())
      } else if (ctx.returnStatement() != null) {
        visitReturnStatement(ctx.returnStatement())
      } else if (ctx.ifStatement() != null) {
        visitIfStatement(ctx.ifStatement())
      } // Handle while statements
      else if (ctx.whileStatement() != null) {
        visitWhileStatement(ctx.whileStatement())
      } else {
        // Handle other types of statements
        throw new UnsupportedOperationException(s"Unsupported statement: ${ctx.getText}")
      }
    }

    override def visitWhileStatement(ctx: WhileStatementContext): Statement = {
      // Get the condition (expression)
      val condition = visitExpression(ctx.expression())

      // Get the body (block of statements)
      val body = visitmyBlock(ctx.block())

      // Print the visited while statement
      println(s"Visiting while statement with condition: $condition")

      // Return the corresponding AST node for a while statement
      WhileStatement(condition, Block(body))
    }
    override def visitIfStatement(ctx: IfStatementContext): Statement = {
      val condition = visitExpression(ctx.expression())
      val thenBranch = visitmyBlock(ctx.block())

      println(s"Visiting if statement, condition: $condition")

      val elseIfBranches = ctx.elseifStatement().asScala.map(visitElseIf).toList
      val elseBranch = if (ctx.elseStatement() != null) {
        Some(Block(visitElse(ctx.elseStatement())))
      } else {
        None
      }

      IfStatement(condition, Block(thenBranch), elseBranch)
    }

    def visitmyBlock(ctx: BlockContext): List[Statement] = {
      println("Visiting block")
      ctx.children.asScala.flatMap {
        case statementCtx: StatementContext =>
          Some(visitStatement(statementCtx))
        case exprCtx: ExpressionContext =>
          Some(StatementExpression(visitExpression(exprCtx)))
        case _ =>
          None
      }.toList
    }

    def visitElseIf(ctx: ElseifStatementContext): Statement = {
      val condition = visitExpression(ctx.expression())
      val thenBranch = visitmyBlock(ctx.block())
      println(s"Visiting else-if statement, condition: $condition")
      IfStatement(condition, Block(thenBranch), None)
    }

    def visitElse(ctx: ElseStatementContext): List[Statement] = {
      println("Visiting else statement")
      visitmyBlock(ctx.block())
    }

    override def visitVariableDeclaration(ctx: VariableDeclarationContext): VarDecl = {
      println(s"Visiting variable declaration: ${ctx.IDENTIFIER().getText}")
      val varType = visitType(ctx.`type`())
      val name = ctx.IDENTIFIER().getText
      val initializer = Option(ctx.expression()).map(visitExpression)
      VarDecl(name, varType, initializer)
    }

    override def visitReturnStatement(ctx: ReturnStatementContext): ReturnStatement = {
      val expr = if (ctx.expression() != null) Some(visitExpression(ctx.expression())) else None
      println(s"Visiting return statement: ${expr}")
      ReturnStatement(expr)
    }

    override def visitExpression(ctx: ExpressionContext): Expression = {
      println(s"Visiting expression: ${ctx.getText}")
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
        visitArrayCreation(ctx.arrayCreation())}else {
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

      println(s"Visiting object creation: new $className(${arguments.mkString(", ")})")
      NewObject(className, arguments)
    }

    override def visitArrayCreation(ctx: ArrayCreationContext): NewArray = {
      val arrayType = visitType(ctx.`type`())
      val dimensions = ctx.expression().asScala.map(visitExpression).toList

      println(s"Visiting array creation: new ${arrayType.toString}[${dimensions.mkString("][")}]")
      NewArray(arrayType, dimensions)
    }
    def visitBinaryOp(ctx: JavaParser.ExpressionContext): Expression = {
      val left = visitExpression(ctx.expression(0))
      val operator = ctx.getChild(1).getText
      val right = visitExpression(ctx.expression(1))

      println(s"Visiting binary operation: $left $operator $right")
      BinaryOp(left, operator, right)
    }

    override def visitMethodCall(ctx: MethodCallContext): Expression = {
      var target = visitPrimary(ctx.primary())  // Base expression (like `new A()` or a variable)

      // Get all method calls in the chain (like `.getB()` and `.getNumber()`)
      val methodCalls = ctx.IDENTIFIER().asScala

      var finalTarget: Expression = target  // This will hold the current expression being built

      // Iterate over each method call in the chain
      methodCalls.foreach { method =>
        val methodName = method.getText

        // If the argumentList exists, iterate over all argumentContexts in the list
        val arguments = if (ctx.argumentList() != null) {
          // Iterate over all argument lists and visit each expression
          ctx.argumentList().asScala.flatMap { argListCtx =>
            // For each argument list, we get all its expressions and visit each one
            argListCtx.expression().asScala.map { exprCtx =>
              visitExpression(exprCtx)  // Visit each expression context
            }
          }.toList
        } else {
          List()  // No arguments, return an empty list
        }

        println(s"Visiting method call: $methodName with arguments: $arguments")

        // Update the final target expression by wrapping it with the new method call
        finalTarget = MethodCall(finalTarget, methodName, arguments)
      }

      finalTarget  // Return the final target expression after all method calls
    }



    def visitmyArgumentList(ctx: ArgumentListContext): List[Expression] = {
      println("Visiting argument list")
      ctx.expression().asScala.map(visitExpression).toList
    }

    override def visitPrimary(ctx: PrimaryContext): Expression = {
      println(s"Visiting primary expression: ${ctx.getText}")
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


      throw new IllegalArgumentException("Unrecognized primary expression: " + ctx.getText)
    }

    override def visitExpressionStatement(ctx: ExpressionStatementContext): Statement = {
      val expr = visitExpression(ctx.expression())
      println(s"Visiting expression statement: $expr")
      StatementExpression(expr)
    }

    override def visitLiteral(ctx: LiteralContext): Literal = {
      println(s"Visiting literal: ${ctx.getText}")

      if (ctx.INTEGER_LITERAL() != null) {
        // Handle integer literals
        Literal(ctx.INTEGER_LITERAL().getText.toInt)
      } else if (ctx.STRING_LITERAL() != null) {
        // Handle string literals
        Literal(ctx.STRING_LITERAL().getText)
      } else if (ctx.BOOLEAN_LITERAL() != null) {
        // Handle boolean literals (true/false)
        ctx.BOOLEAN_LITERAL().getText match {
          case "true" => Literal(true)
          case "false" => Literal(false)
          case _ => throw new UnsupportedOperationException(s"Unsupported boolean literal: ${ctx.getText}")
        }
      } else {
        // If literal is not recognized
        throw new UnsupportedOperationException(s"Unsupported literal: ${ctx.getText}")
      }
    }


    override def visitAttribute(ctx: AttributeContext): VarDecl = {
      println(s"Visiting attribute: ${ctx.IDENTIFIER().getText}")
      val modifier = Option(ctx.optionalModifier()).flatMap(m => Option(m.getText)).getOrElse("")
      val varType = visitType(ctx.`type`())
      val name = ctx.IDENTIFIER().getText
      val initializer = Option(ctx.expression()).map(visitExpression)

      VarDecl(name, varType, initializer)
    }
  }
}
