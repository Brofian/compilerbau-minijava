package de.students.Parser

import de.dhbw.horb.ast.{Function, Program, Variable}
import de.students.Parser.*
import de.students.antlr.JavaParser.*
import de.students.antlr.{JavaBaseVisitor, JavaParser}

import scala.jdk.CollectionConverters.*
import org.antlr.v4.runtime.tree.ParseTree

object ASTGenerator {

  class ASTBuilder extends JavaBaseVisitor[ASTNode] {
    // Generate the AST from the parse tree root
    def generateAST(tree: ParseTree): Package = tree.accept(this).asInstanceOf[Package]

    override def visitPackage(ctx: PackageContext): Package = {
      val packageName = ctx.id().getText // Assuming the package name is a single identifier
      val classDecls = ctx.class_()
        .asScala
        .map(visitClass) // Call visitClass for each class in the package
        .toList

      println(s"Package: $packageName, Classes: ${classDecls.map(_.name).mkString(", ")}")
      Package(packageName, classDecls)
    }

    override def visitClass(ctx: ClassContext): ClassDecl = {
      val name = ctx.id(0).getText // Der Name der Klasse
      val maybeParent = Option(ctx.EXTENDS()).flatMap(_ => Option(ctx.id(1)).map(_.getText)) // Überprüfung auf EXTENDS
      // Setze den Standardwert "Object", wenn kein Parent angegeben ist
      val parent = maybeParent.getOrElse("Object")

      val isAbstract = ctx.ABSTRACT() != null // Überprüfung auf ABSTRACT

//      val methods = ctx.classbody().method().asScala.map(visitMethod).toList
     // val fields = ctx.classbody().attribute().asScala.map(visitAttribute).toList


      ClassDecl(name, parent, isAbstract, null, null)
    }


    override def visitMethod(ctx: MethodContext): MethodDecl = {
      val static = (ctx.STATIC() != null)
      
     
      val name = ctx.IDENTIFIER().getText
      val isAbstract = (ctx.modifier().ABSTRACT() != null)
      val returnType = visitMyType(ctx.returntype().`type`())
      val params: List[VarDecl] = ctx.parameterList()
        .parameter()
        .asScala
        .map(visitParameter)
        .toList
      val body = Option(ctx.methodBody()).map(visitMyMethodBody(_).toList).orNull

      MethodDecl(name,static, isAbstract, returnType, params, body)
    }

    override def visitParameter(ctx: ParameterContext): VarDecl = {
      val name = ctx.IDENTIFIER().getText
      val paramType = visitMyType(ctx.`type`())
      VarDecl(name, paramType)
    }


    override def visitAttribute(ctx: AttributeContext): VarDecl = {
      val name = ctx.IDENTIFIER().getText
      val varType = visitMyType(ctx.`type`())
      VarDecl(name, varType)
    }

     def visitMyType(ctx: TypeContext): Type = {
      if (ctx.PRIMITIVE_TYPE() != null) {
        ctx.PRIMITIVE_TYPE().getText match {
          case "int"    => IntType
          case "char"   => UserType("char")
          case "boolean" => UserType("boolean")
        }
      } else if (ctx.id() != null) {
        UserType(ctx.id().getText)
      } else if (ctx.`type`() != null) {
        ArrayType(visitMyType(ctx.`type`()))
      } else {
        throw new RuntimeException("Invalid type")
      }
    }

    

    def visitMyMethodBody(ctx: MethodBodyContext): List[Statement] = {
      ctx.block().asScala.map(visitBlock).toList
    }

    override def visitBlock(ctx: BlockContext): Statement = {
      if (ctx.statement() != null) visitStatement(ctx.statement())
      else throw new RuntimeException("Unsupported block content")
    }

    override def visitStatement(ctx: StatementContext): Statement = {
      if (ctx.returnStatement() != null) {
        visitReturnStatement(ctx.returnStatement())
      } else if (ctx.ifStatement() != null) {
        visitIfStatement(ctx.ifStatement())
      } else if (ctx.whileStatement() != null) {
        visitWhileStatement(ctx.whileStatement())
      } else {
        throw new RuntimeException("Unsupported statement")
      }
    }

    override def visitReturnStatement(ctx: ReturnStatementContext): ReturnStatement = {
      val expr = Option(ctx.expression()).map(visitExpression)
      ReturnStatement(expr)
    }

    override def visitIfStatement(ctx: IfStatementContext): IfStatement = {
      val cond = visitExpression(ctx.expression())
      val thenBranch = visitBlock(ctx.block())
      val elseBranch = Option(ctx.elseStatement()).map(es => visitBlock(es.block()))
      IfStatement(cond, thenBranch, elseBranch)
    }

    override def visitWhileStatement(ctx: WhileStatementContext): WhileStatement = {
      val cond = visitExpression(ctx.expression())
      val body = visitBlock(ctx.block())
      WhileStatement(cond, body)
    }

    override def visitExpression(ctx: ExpressionContext): Expression = {
      if (ctx.literal() != null) {
        visitLiteral(ctx.literal())
      } else if (ctx.IDENTIFIER != null) {
        VarRef(ctx.IDENTIFIER.getText)
      } else if (ctx.operator() != null) {
        val left = visitExpression(ctx.expression(0))
        val op = ctx.operator().getText
        val right = visitExpression(ctx.expression(1))
        BinaryOp(left, op, right)
      } else {

        throw new RuntimeException("Unsupported expression")
      }
    }

    override def visitLiteral(ctx: LiteralContext): Literal = {
      if (ctx.INTEGER_LITERAL != null) Literal(ctx.INTEGER_LITERAL.getText.toInt)
      else if (ctx.BOOLEAN_LITERAL != null) Literal(ctx.BOOLEAN_LITERAL.getText.toBoolean)
      else Literal(ctx.getText)
    }
  }

}
