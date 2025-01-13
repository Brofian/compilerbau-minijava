package de.students.semantic

import de.students.Parser.*

import scala.collection.mutable;

object SemanticCheck {

  /**
   * Entry point for running checks on a program
   *
   * @param project The project to run semantic and type checks against
   */
  def runCheck(project: Project): Project = {

    // combine packages with the same package name for easier access
    val mergedProject = SyntacticSugarHandler.mergeClassesWithSamePackageName(project)

    // create context and make all classes and their fields known to the whole project
    val globalContext = SemanticContext(mutable.Map[String, Type](), "", "")
    this.gatherGlobalTypeAssumptions(mergedProject, globalContext)

    val typedPackages = mergedProject.packages.map((pckg: Package) => {
      // run typeCheck for package
      this.checkPackage(pckg, globalContext.createChildContext(Some(pckg.name)))
    })

    Project(typedPackages)
  }

  /**
   * Go through every class in the project and add itself, its fields and its methods to the type assumptions
   *
   * @param project The project to iterate through
   * @return
   */
  private def gatherGlobalTypeAssumptions(project: Project, context: SemanticContext): Unit = {
    // add all classes and they properties to the type assumptions
    project.packages.foreach((pckg: Package) => {

      // use context to determine fully qualified names
      val pckgContext = context.createChildContext(Some(pckg.name))

      // add all fields and methods to the type assumptions
      pckg.classes.foreach((cls: ClassDecl) => {
        val fullyQualifiedClassName = pckgContext.getFullyQualifiedClassName(cls.name)
        context.addTypeAssumption(fullyQualifiedClassName, UserType(fullyQualifiedClassName))

        // use context to determine fully qualified names
        val classContext = pckgContext.createChildContext(None, Some(fullyQualifiedClassName))

        // add fields types
        cls.fields.foreach((field: VarDecl) => {
          val fullyQualifiedFieldName = classContext.getFullyQualifiedMemberName(field.name)
          context.addTypeAssumption(fullyQualifiedFieldName, field.varType)
        })

        // add method types
        cls.methods.foreach((method: MethodDecl) => {
          val fullyQualifiedMethodName = classContext.getFullyQualifiedMemberName(method.name)
          context.addTypeAssumption(
            fullyQualifiedMethodName,
            FunctionType(method.returnType, method.params.map(p => p.varType))
          )
        })
      })
    })
  }


  /**
   * TypeCheck each class in the package and return the package with typed classes
   *
   * @param pckg    The package to check
   * @param context The current context to use
   * @return
   */
  private def checkPackage(pckg: Package, context: SemanticContext): Package = {
    // run typeCheck for class and replace with typed class
    val typedClasses = pckg.classes.map((cls: ClassDecl) => {
      
      // create a new class-level-context
      val classContext = context.createChildContext(None,
        Some(context.getFullyQualifiedClassName(cls.name)))
      // typeCheck the class
      this.checkClass(
        SyntacticSugarHandler.moveFieldInitializerToConstructor(cls, context),
        classContext  
      )
      
    })
    Package(pckg.name, typedClasses)
  }


  /**
   * TypeCheck each member of the class and return the class with typed members
   *
   * @param cls   The class to typeCheck
   * @param context   The current context to use
   * @return
   */
  private def checkClass(cls: ClassDecl, context: SemanticContext): ClassDecl = {
    val typedMethods: List[MethodDecl] = cls.methods.map((method: MethodDecl) => {
      val typedBody: TypedStatement = StatementChecks.checkStatement(method.body, context)
      if (!UnionTypeFinder.isASubtypeOfB(typedBody.stmtType, method.returnType, context)) {
        throw new SemanticException(s"Method ${method.name} in ${context.getClassName} with return type ${method.returnType} cannot return value of type ${typedBody.stmtType}")
      }
      
      val fullyQualifiedMethodName = context.getFullyQualifiedMemberName(method.name)
      MethodDecl(fullyQualifiedMethodName, method.static, method.isAbstract, method.returnType, method.params, typedBody)
    })

    // TODO: do we really need to differentiate between constructors and methods?
    val typedConstructors: List[ConstructorDecl] = cls.constructors.map((constructor: ConstructorDecl) => {
      val typedBody: TypedStatement = StatementChecks.checkStatement(constructor.body, context)
      if (!UnionTypeFinder.isASubtypeOfB(typedBody.stmtType, VoidType,  context)) {
        throw new SemanticException(s"Constructor ${constructor.name} cannot return value")
      }
      ConstructorDecl(constructor.name, constructor.params, typedBody)
    })

    // TODO: determine fully qualified name of parent by imports
    ClassDecl(context.getClassName, cls.parent, cls.isAbstract, typedMethods, cls.fields, typedConstructors)
  }
}