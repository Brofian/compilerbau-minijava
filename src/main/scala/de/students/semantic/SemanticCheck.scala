package de.students.semantic

import de.students.Parser.*
import scala.collection.mutable
import scala.collection.mutable.ListBuffer;

object SemanticCheck {

  /**
   * Entry point for running checks on a program
   *
   * @param project The project to run semantic and type checks against
   */
  def runCheck(project: Project): Project = {

    // todo: make sure, no class exists twice
    // todo: make sure, no class contains two fields with the same name twice *
    // todo: respect method overloading (*)

    // create context and make all classes and their fields known to the whole project
    val globalContext = SemanticContext(
      classAccessHelper = ClassAccessHelper(ClassTypeBridge(project)),
      typeAssumptions = mutable.Map[String, Type](),
      imports = mutable.Map[String, String](),
      importWildcards = ListBuffer(),
      packageName = "",
      className = ""
    )

    val typedPackages = project.packages.map((pckg: Package) => {
      // run typeCheck for package
      this.checkPackage(pckg, globalContext.createChildContext(Some(pckg.name)))
    })

    Project(typedPackages)
  }

  /**
   * TypeCheck each class in the package and return the package with typed classes
   *
   * @param pckg    The package to check
   * @param context The current context to use
   * @return
   */
  private def checkPackage(pckg: Package, context: SemanticContext): Package = {

    // add imports
    var hasExplicitJavaLangImport = false
    pckg.imports.names.foreach(importName =>
      val parts = importName.split("""\.""")
      context.addImport(parts.last, importName)
      hasExplicitJavaLangImport ||= importName == "java.lang."
    )

    // add implicit java.lang.* import
    if (!hasExplicitJavaLangImport) {
      context.addImport("", "java.lang.")
    }

    // run typeCheck for class and replace with typed class
    val typedClasses = pckg.classes.map((cls: ClassDecl) => {

      // create a new class-level-context
      val classContext = context.createChildContext(None, Some(context.getFullyQualifiedClassName(cls.name)))
      // typeCheck the class
      this.checkClass(
        SyntacticSugarHandler.handleSyntacticSugar(cls, context),
        classContext
      )

    })
    Package(pckg.name, pckg.imports, typedClasses)
  }

  /**
   * TypeCheck each member of the class and return the class with typed members
   *
   * @param cls   The class to typeCheck
   * @param context   The current context to use
   * @return
   */
  private def checkClass(cls: ClassDecl, context: SemanticContext): ClassDecl = {
    // run typeCheck on methods
    val typedMethods: List[MethodDecl] = cls.methods.map((method: MethodDecl) => {
      val methodContext = context.createChildContext()

      // add parameters to the list of known variables
      method.params.foreach(param =>
        methodContext.addTypeAssumption(
          param.name,
          context.simpleTypeToQualified(param.varType)
        )
      )

      val evaluatedReturnType = context.simpleTypeToQualified(method.returnType)

      if (method.body.isEmpty && !method.isAbstract) {
        throw new SemanticException(s"Non-abstract method ${method.name} must provide a method body!");
      }
      if (method.body.nonEmpty && method.isAbstract) {
        throw new SemanticException(s"Abstract method ${method.name} cannot have a method body!");
      }

      val typedBody: Option[TypedStatement] = method.body match
        case Some(methodBody) =>
          val typedMethodBody = StatementChecks.checkStatement(methodBody, methodContext)
          // check if method body type matches method declaration
          if (
            !UnionTypeFinder
              .isASubtypeOfB(typedMethodBody.stmtType, evaluatedReturnType, methodContext.getClassAccessHelper)
          ) {
            throw new SemanticException(
              s"Method ${method.name} in ${methodContext.getClassName} with return type $evaluatedReturnType cannot return value of type ${typedMethodBody.stmtType}"
            )
          }
          Some(typedMethodBody)
        case None => /* Abstract method, skip body check */ None

      MethodDecl(
        method.accessModifier,
        method.name,
        method.isAbstract,
        method.static,
        method.isFinal,
        evaluatedReturnType,
        method.params,
        typedBody
      )
    })

    // run typeCheck on constructors
    val typedConstructors: List[ConstructorDecl] = cls.constructors.map((constructor: ConstructorDecl) => {
      val methodContext = context.createChildContext()
      // add parameters to the list of known variables
      constructor.params.foreach(param =>
        methodContext.addTypeAssumption(
          param.name,
          context.simpleTypeToQualified(param.varType)
        )
      )

      val typedBody: TypedStatement = StatementChecks.checkStatement(constructor.body, methodContext)
      if (!UnionTypeFinder.isASubtypeOfB(typedBody.stmtType, VoidType, methodContext.getClassAccessHelper)) {
        throw new SemanticException(s"Constructor ${constructor.name} cannot return value")
      }
      ConstructorDecl(constructor.accessModifier, constructor.name, constructor.params, typedBody)
    })

    val fullyQualifiedParentName = context.getFullyQualifiedClassName(cls.parent)
    ClassDecl(
      context.getClassName,
      fullyQualifiedParentName,
      cls.isAbstract,
      typedMethods,
      cls.fields,
      typedConstructors
    )
  }
}
