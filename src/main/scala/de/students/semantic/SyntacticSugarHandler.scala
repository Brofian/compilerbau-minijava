package de.students.semantic

import de.students.Parser.*

import scala.collection.mutable
import scala.collection.mutable.ListBuffer;

object SyntacticSugarHandler {

  /**
   * Resolve syntactic sugar before running the type- and semantic-check on this class
   *
   * @param cls           The class to check
   * @param classContext  The special context for this class
   * @return
   */
  def handleSyntacticSugar(cls: ClassDecl, classContext: SemanticContext): ClassDecl = {
    var updatedClass = cls
    updatedClass = this.addImplicitReturnsAtMethodEnd(updatedClass, classContext)
    updatedClass = this.moveFieldInitializers(updatedClass, classContext)
    updatedClass = this.splitVarInitializerFromDeclaration(updatedClass, classContext)
    updatedClass
  }

  /**
   * Every method must end with a return. If none is specified, an empty return should be added implicitly.
   * Otherwise, the program execution would be "Falling of the end of the code"
   *
   * @param cls          The class to check
   * @param classContext The current context of the class
   * @return
   */
  private def addImplicitReturnsAtMethodEnd(cls: ClassDecl, classContext: SemanticContext): ClassDecl = {
    val fixedMethods: List[MethodDecl] = cls.methods.map(method => {
      if (method.body.isEmpty) {
        method
      } else {
        val bodyBlock = method.body.get.asInstanceOf[BlockStatement]

        // check if all execution paths contain a return statement
        def returnFinder(stmt: Statement): Boolean = {
          stmt match {
            case ReturnStatement(expr) => true
            case IfStatement(cond, thenBranch, elseBranch) =>
              returnFinder(thenBranch) && elseBranch.nonEmpty && returnFinder(elseBranch.get)
            case BlockStatement(stmts) => stmts.exists(returnFinder)
            case _                     => false
          }
        }

        if (!returnFinder(bodyBlock)) {
          val fixedStmts = bodyBlock.stmts :+ ReturnStatement(None)
          val fixedBody = BlockStatement(fixedStmts)
          MethodDecl(
            method.accessModifier,
            method.name,
            method.isAbstract,
            method.static,
            method.isFinal,
            method.returnType,
            method.params,
            Some(fixedBody)
          )
        } else {
          method
        }
      }
    })

    ClassDecl(cls.name, cls.parent, cls.isAbstract, fixedMethods, cls.fields, cls.constructors)
  }

  /**
   * Move the initializer expressions from class fields to the start of every constructor
   *
   * @param cls          The class to check
   * @param classContext The current context of the class
   * @return
   */
  private def moveFieldInitializers(cls: ClassDecl, classContext: SemanticContext): ClassDecl = {
    val initializerList: ListBuffer[Statement] = ListBuffer()
    val fixedFields = cls.fields.map(field => {
      if (field.initializer.isEmpty) {
        field
      } else {
        val stmt = StatementExpression(BinaryOp(ThisAccess(field.name), "=", field.initializer.get))
        initializerList.addOne(stmt)
        FieldDecl(field.accessModifier, field.isStatic, field.isFinal, field.name, field.varType, None)
      }
    })

    if (initializerList.isEmpty) {
      return cls
    }

    // make sure, there is at least one constructor defined
    val constructors =
      if cls.constructors.nonEmpty then cls.constructors
      else
        List(
          ConstructorDecl(None, cls.name, List(), BlockStatement(List()))
        )

    // add the initializers to every constructor
    val fixedConstructors: List[ConstructorDecl] = constructors.map(constructor => {
      ConstructorDecl(
        constructor.accessModifier,
        constructor.name,
        constructor.params,
        BlockStatement(initializerList.toList ::: constructor.body.asInstanceOf[BlockStatement].stmts)
      )
    })

    ClassDecl(cls.name, cls.parent, cls.isAbstract, cls.methods, fixedFields, fixedConstructors)
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
      if (method.body.isEmpty) {
        method // keep empty body
      } else {
        val splitStatements: ListBuffer[Statement] = ListBuffer()
        // split var declarations with initializer into
        method.body.get.asInstanceOf[BlockStatement].stmts.foreach {
          case stmt @ (varDeclStmt: VarDecl) =>
            varDeclStmt.initializer match
              case Some(varInitializer) =>
                splitStatements.addOne(VarDecl(varDeclStmt.name, varDeclStmt.varType, None)) // remove initializer
                splitStatements.addOne(
                  StatementExpression(BinaryOp(VarRef(varDeclStmt.name), "=", varInitializer))
                ) // add initializer as separate statement
              case None => splitStatements.addOne(stmt) // no initializer, no changes required
          case stmt => splitStatements.addOne(stmt) // not a var declaration, no changes required
        }

        // construct new method with updated body
        MethodDecl(
          method.accessModifier,
          method.name,
          method.isAbstract,
          method.static,
          method.isFinal,
          method.returnType,
          method.params,
          Some(BlockStatement(splitStatements.toList))
        )
      }
    })

    ClassDecl(cls.name, cls.parent, cls.isAbstract, fixedMethods, cls.fields, cls.constructors)
  }

}
