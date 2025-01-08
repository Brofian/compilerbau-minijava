package de.students.semantic

import de.students.Parser.*
import scala.collection.mutable;

object SemanticCheck {

  /**
   * Entry point for running checks on a program
   *
   * @param packageDecl The program to run semantic and type checks against
   */
  def runCheck(packageDecl: Package): Package = {

    val typeAssumptions = mutable.Map[String, Type]()

    // instantly add all class names to the list of known types
    packageDecl.classes.foreach((cls: ClassDecl) => {
      typeAssumptions.addOne(
        (cls.name, UserType(cls.name))
      )
    })


    val typedClasses = packageDecl.classes.map((cls: ClassDecl) => {
      // run typeCheck for class and replace with typed class
      this.checkClass(cls, typeAssumptions)
    })

    Package(packageDecl.name, typedClasses)
  }


  private def checkClass(cls: ClassDecl, typeAssumptions: mutable.Map[String, Type]): ClassDecl = {

    // add all the fields to the current context
    cls.fields.foreach((field: VarDecl) => {
      // TODO: check if this field already exists as final in the parent. If so, throw an error
      typeAssumptions.addOne((field.name, field.varType))
    })

    // add all methods to the current context
    cls.methods.foreach((method: MethodDecl) => {
      // TODO: check if this method already exists as final in the parent. If so, throw an error
      val paramTypes: List[Type] = method.params.map(param => param.varType)
      val methodType: FunctionType = FunctionType(method.returnType, paramTypes)
      typeAssumptions.addOne((method.name, methodType))
    })


    val typedMethods: List[MethodDecl] = cls.methods.map((method: MethodDecl) => {
      val typedBody: TypedStatement = StatementChecks.checkStatement(method.body, typeAssumptions)

      if (!UnionTypeFinder.isASubtypeOfB(typedBody.stmtType, method.returnType)) {
        throw new SemanticException(s"Method ${method.name} with return type ${method.returnType} cannot return value of type ${typedBody.stmtType}")
      }

      MethodDecl(method.name, method.static, method.isAbstract, method.returnType, method.params, typedBody)
    })

    // TODO: do we really need to differentiate between constructors and methods?
    val typedConstructors: List[ConstructorDecl] = cls.constructors.map((constructor: ConstructorDecl) => {
      val typedBody: TypedStatement = StatementChecks.checkStatement(constructor.body, typeAssumptions)

      if (!UnionTypeFinder.isASubtypeOfB(typedBody.stmtType, VoidType)) {
        throw new SemanticException(s"Constructor ${constructor.name} cannot return value")
      }

      ConstructorDecl(constructor.name, constructor.params, typedBody)
    })


    ClassDecl(cls.name, cls.parent, cls.isAbstract, typedMethods, cls.fields, typedConstructors)
  }
}