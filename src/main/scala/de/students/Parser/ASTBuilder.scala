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
          .packageId()
          .asScala
          .map { importCtx =>
            val importPath = importCtx.IDENTIFIER().asScala.map(_.getText).mkString(".")
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
        if (ctx.block() != null) Option(visitBlockStmt(ctx.block()))
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
        visitType(ctx.`type`()) // Calls visitType below
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
          case other     => throw new RuntimeException(s"Unknown primitive type: $other")
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
      val stmtList = ctx.children.asScala.flatMap {
        case statementCtx: StatementContext =>
          Some(visitStatement(statementCtx))
        case exprCtx: ExpressionContext =>
          Some(StatementExpression(visitExpression(exprCtx)))
        case _ =>
          None
      }.toList
      BlockStatement(stmtList)
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
        throw new UnsupportedOperationException(s"Unsupported statement: ${ctx.getText}")
      }
    }

    override def visitForStatement(ctx: ForStatementContext): Statement = {
      Logger.debug(s"Visiting for statement: ${ctx.getText}")
      val init: Option[Statement] =
        if (ctx.variableDeclaration() != null) Some(visitVariableDeclaration(ctx.variableDeclaration()))
        else if (ctx.expressionStatement() != null) Some(visitExpressionStatement(ctx.expressionStatement()))
        else None
      val cond: Option[Expression] = Option(ctx.expression(0)).map(visitExpression)
      val update: Option[Expression] =
        if (ctx.expression().size() > 1) Option(visitExpression(ctx.expression(1)))
        else None
      val body: Statement = visitBlockStmt(ctx.block())
      Logger.debug(s"For loop - Init: $init, Condition: $cond, Update: $update, Body: $body")
      ForStatement(init, cond, update, body)
    }

    override def visitDoWhileStatement(ctx: DoWhileStatementContext): Statement = {
      val condition = visitExpression(ctx.expression())
      val body = visitBlockStmt(ctx.block())
      Logger.debug(s"Visiting do-while statement with condition: $condition")
      DoWhileStatement(condition, body)
    }

    override def visitWhileStatement(ctx: WhileStatementContext): Statement = {
      val condition = visitExpression(ctx.expression())
      val body = visitBlockStmt(ctx.block())
      Logger.debug(s"Visiting while statement with condition: $condition")
      WhileStatement(condition, body)
    }

    override def visitIfStatement(ctx: IfStatementContext): Statement = {
      val condition = visitExpression(ctx.expression())
      val thenBranch = visitBlockStmt(ctx.block())
      Logger.debug(s"Visiting if statement, condition: $condition")
      val elseBranch = if (ctx.elseStatement() != null) Some(visitElse(ctx.elseStatement())) else None
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
      Logger.debug(s"Visiting return statement: $expr")
      ReturnStatement(expr)
    }

    // ========================================================
    // New Expression handling using unified primary & postfix chain
    // ========================================================

    /**
     * expression : unaryExpression (operator unaryExpression)* ;
     */
    override def visitExpression(ctx: ExpressionContext): Expression = {
      Logger.debug(s"Visiting expression: ${ctx.getText}")
      val unaryExprs = ctx.unaryExpression().asScala.toList
      var expr = visitUnaryExpression(unaryExprs.head)
      val opList = ctx.operator().asScala.toList
      for ((op, idx) <- opList.zipWithIndex) {
        val right = visitUnaryExpression(unaryExprs(idx + 1))
        expr = BinaryOp(expr, op.getText, right)
      }
      expr
    }

    /**
     * unaryExpression : ('!' | '-') unaryExpression | postfixExpression ;
     */
    override def visitUnaryExpression(ctx: UnaryExpressionContext): Expression = {
      Logger.debug(s"Visiting unary expression: ${ctx.getText}")
      if (ctx.getChildCount == 1) {
        visitPostfixExpression(ctx.postfixExpression())
      } else {
        val op = ctx.getChild(0).getText
        val expr = visitUnaryExpression(ctx.unaryExpression())
        UnaryOp(op, expr)
      }
    }

    /**
     * postfixExpression : simplePrimary (postfixOp)* ;
     */
    override def visitPostfixExpression(ctx: PostfixExpressionContext): Expression = {
      Logger.debug(s"Visiting postfix expression: ${ctx.getText}")
      var expr = visitSimplePrimary(ctx.simplePrimary())
      for (postfixOp <- ctx.postfixOp().asScala) {
        expr = visitPostfixOp(expr, postfixOp)
      }
      expr
    }

    /**
     * simplePrimary : IDENTIFIER | THIS | literal | '(' expression ')' | objectCreation | arrayCreation ;
     */
    override def visitSimplePrimary(ctx: SimplePrimaryContext): Expression = {
      Logger.debug(s"Visiting simple primary: ${ctx.getText}")

      if (ctx.IDENTIFIER() != null) {
        // Check if the IDENTIFIER is followed by a parenthesized argument list
        if (ctx.getChildCount > 1 && ctx.getChild(1).getText == "(") {
          // It's an implicit method call: e.g., calc(10) becomes MethodCall(this, "calc", args)
          val methodName = ctx.IDENTIFIER().getText
          val args = if (ctx.argumentList() != null) visitMyArgumentList(ctx.argumentList()) else List()
          MethodCall(VarRef("this"), methodName, args)
        } else {
          // It's just a variable reference or a class name
          VarRef(ctx.IDENTIFIER().getText)
        }
      } else if (ctx.THIS() != null) {
        VarRef("this")
      } else if (ctx.literal() != null) {
        visitLiteral(ctx.literal())
      } else if (ctx.expression() != null) {
        visitExpression(ctx.expression())
      } else if (ctx.objectCreation() != null) {
        visitObjectCreation(ctx.objectCreation())
      } else if (ctx.arrayCreation() != null) {
        visitArrayCreation(ctx.arrayCreation())
      } else {
        throw new UnsupportedOperationException("Unrecognized simple primary: " + ctx.getText)
      }
    }

    /**
     * postfixOp : '.' IDENTIFIER ( '(' argumentList? ')' )? | '[' expression ']' ;
     */
    def visitPostfixOp(target: Expression, ctx: PostfixOpContext): Expression = {
      Logger.debug(s"Visiting postfix operator: ${ctx.getText} on target: $target")
      ctx.getChild(0).getText match {
        case "." =>
          val memberName = ctx.IDENTIFIER().getText
          if (ctx.getChildCount > 2 && ctx.getChild(2).getText == "(") {
            val args = if (ctx.argumentList() != null) visitMyArgumentList(ctx.argumentList()) else List()
            MethodCall(target, memberName, args)
          } else {
            target match {
              case VarRef(name) =>
                if (name == "this")
                  ThisAccess(memberName)
                else
                  ClassAccess(name, memberName)

              case _ =>
                throw new UnsupportedOperationException("Unsupported postfix usage: " + target)
            }
          }
        case "[" =>
          val index = visitExpression(ctx.expression())
          ArrayAccess(target, index)
        case other =>
          throw new UnsupportedOperationException("Unsupported postfix operator: " + other)
      }
    }

    /**
     * argumentList : expression (',' expression)* ;
     */
    def visitMyArgumentList(ctx: ArgumentListContext): List[Expression] = {
      Logger.debug(s"Visiting argument list: ${ctx.getText}")
      ctx.expression().asScala.map(visitExpression).toList
    }

    // ========================================================
    // End of new Expression handling
    // ========================================================

    override def visitExpressionStatement(ctx: ExpressionStatementContext): Statement = {
      val expr = visitExpression(ctx.expression())
      Logger.debug(s"Visiting expression statement: $expr")
      StatementExpression(expr)
    }

    override def visitLiteral(ctx: LiteralContext): Literal = {
      Logger.debug(s"Visiting literal: ${ctx.getText}")
      if (ctx.INTEGER_LITERAL() != null) {
        Literal(ctx.INTEGER_LITERAL().getText.toInt)
      } else if (ctx.STRING_LITERAL() != null) {
        Literal(ctx.STRING_LITERAL().getText)
      } else if (ctx.BOOLEAN_LITERAL() != null) {
        ctx.BOOLEAN_LITERAL().getText match {
          case "true"  => Literal(true)
          case "false" => Literal(false)
          case _       => throw new UnsupportedOperationException(s"Unsupported boolean literal: ${ctx.getText}")
        }
      } else if (ctx.BYTE_LITERAL() != null) {
        Literal(ctx.BYTE_LITERAL().getText.dropRight(1).toByte)
      } else if (ctx.SHORT_LITERAL() != null) {
        Literal(ctx.SHORT_LITERAL().getText.dropRight(1).toShort)
      } else if (ctx.LONG_LITERAL() != null) {
        Literal(ctx.LONG_LITERAL().getText.dropRight(1).toLong)
      } else if (ctx.FLOAT_LITERAL() != null) {
        Literal(ctx.FLOAT_LITERAL().getText.dropRight(1).toFloat)
      } else if (ctx.DOUBLE_LITERAL() != null) {
        Literal(ctx.DOUBLE_LITERAL().getText.toDouble)
      } else if (ctx.CHAR_LITERAL() != null) {
        Literal(ctx.CHAR_LITERAL().getText)
      } else {
        throw new UnsupportedOperationException(s"Unsupported literal: ${ctx.getText}")
      }
    }

    override def visitObjectCreation(ctx: ObjectCreationContext): NewObject = {
      val className = ctx.id().getText
      val arguments = if (ctx.argumentList() != null) visitMyArgumentList(ctx.argumentList()) else List()
      Logger.debug(s"Visiting object creation: new $className(${arguments.mkString(", ")})")
      NewObject(className, arguments)
    }

    override def visitArrayCreation(ctx: ArrayCreationContext): NewArray = {
      val arrayType = visitType(ctx.`type`())
      val dimensions = ctx.expression().asScala.map(visitExpression).toList
      Logger.debug(s"Visiting array creation: new ${arrayType.toString}[${dimensions.mkString("][")}]")
      NewArray(arrayType, dimensions)
    }

    override def visitAttribute(ctx: AttributeContext): FieldDecl = {
      Logger.debug(s"Visiting attribute: ${ctx.IDENTIFIER().getText}")
      val accessModifiers = visitModifiers(ctx.accessModifier())
      val isFinal = ctx.FINAL() != null
      val varType = visitType(ctx.`type`())
      val name = ctx.IDENTIFIER().getText
      val initializer = Option(ctx.expression()).map(visitExpression)
      FieldDecl(accessModifiers, isFinal, name, varType, initializer)
    }

    // Visit an access modifier (PRIVATE, PUBLIC, PROTECTED)
    def visitModifiers(ctx: AccessModifierContext): Option[String] = {
      if (ctx == null) return None
      if (ctx.PRIVATE() != null) Some("private")
      else if (ctx.PUBLIC() != null) Some("public")
      else if (ctx.PROTECTED() != null) Some("protected")
      else None
    }

  }
}
