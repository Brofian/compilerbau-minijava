package de.students.semantic

import de.students.Parser.*

import scala.collection.mutable
import scala.collection.mutable.ListBuffer;


object SyntacticSugarHandler {

  def handleSyntacticSugar(cls: ClassDecl, classContext: SemanticContext): ClassDecl = {
    var updatedClass = cls

    updatedClass = this.moveFieldInitializerToConstructor(updatedClass, classContext)
    updatedClass = this.splitVarInitializerFromDeclaration(updatedClass, classContext)

    updatedClass
  }

  /**
   * Move the initializer expressions from class fields to the start of every constructor
   * @param cls The class to check
   * @param classContext  The current context of the class
   * @return
   */
  private def moveFieldInitializerToConstructor(cls: ClassDecl, classContext: SemanticContext): ClassDecl = {
    
    // TODO
    cls
  }

  /**
   * Move the initializer expressions from a variable definition into a separate statement
   *
   * @param cls          The class to check
   * @param classContext The current context of the class
   * @return
   */
  private def splitVarInitializerFromDeclaration(cls: ClassDecl, classContext: SemanticContext): ClassDecl = {

    val fixedMethods = cls.methods.map(method => {
      val splitStatements: ListBuffer[Statement] = ListBuffer()
      // split var declarations with initializer into
      method.body.asInstanceOf[BlockStatement].stmts.foreach {
        case stmt@(varDeclStmt: VarDecl) =>
          varDeclStmt.initializer match
            case Some(varInitializer) =>
              splitStatements.addOne(VarDecl(varDeclStmt.name, varDeclStmt.varType, None)) // remove initializer
              splitStatements.addOne(StatementExpressions(BinaryOp(VarRef(varDeclStmt.name), "=", varInitializer))) // add initializer as separate statement
            case None => splitStatements.addOne(stmt) // no initializer, no changes required
        case stmt => splitStatements.addOne(stmt) // not a var declaration, no changes required
      }
      MethodDecl(method.name, method.static, method.isAbstract, method.returnType, method.params, BlockStatement(splitStatements.toList))
    })

    ClassDecl(cls.name, cls.parent, cls.isAbstract, fixedMethods, cls.fields, cls.constructors)
  }
  
}