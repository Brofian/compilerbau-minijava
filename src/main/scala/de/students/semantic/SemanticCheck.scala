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
      staticAssumptions = mutable.Map[String, Boolean](),
      imports = mutable.Map[String, String](),
      importWildcards = ListBuffer(),
      packageName = "",
      className = ""
    )

    val typedPackages = project.files.map((file: JavaFile) => {
      // run typeCheck for file
      this.checkFile(file, globalContext.createChildContext(Some(file.packageName)))
    })

    Project(typedPackages)
  }

  /**
   * TypeCheck each class in the package and return the package with typed classes
   *
   * @param file    The file to check
   * @param context The current context to use
   * @return
   */
  private def checkFile(file: JavaFile, context: SemanticContext): JavaFile = {

    // add imports
    var hasExplicitJavaLangImport = false
    file.imports.names.foreach(importName =>
      val parts = importName.split("""\.""")
      context.addImport(parts.last, importName)
      hasExplicitJavaLangImport ||= importName == "java.lang."
    )

    // add implicit java.lang.* import
    if (!hasExplicitJavaLangImport) {
      context.addImport("", "java.lang.")
    }

    // run typeCheck for class and replace with typed class
    val typedClasses = file.classes.map((cls: ClassDecl) => {

      // create a new class-level-context
      val classContext = context.createChildContext(None, Some(context.getFullyQualifiedClassName(cls.name)))
      // remove syntactic sugar and typeCheck the class
      this.checkClass(
        SyntacticSugarHandler.handleSyntacticSugar(cls, classContext),
        classContext
      )
    })
    JavaFile(file.packageName, file.imports, typedClasses)
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

      val typedParams = method.params.map(param => {
        val evaluatedType = context.simpleTypeToQualified(param.varType)
        val evaluatedInitializer = param.initializer match {
          case Some(initializer) =>
            val typedInitializer = ExpressionChecks.checkExpression(initializer, context)
            if (
              !UnionTypeFinder.isASubtypeOfB(evaluatedType, typedInitializer.exprType, context.getClassAccessHelper)
            ) {
              throw new SemanticException(
                s"Cannot initialize parameter ${param.name} with value of type ${typedInitializer.exprType}"
              )
            }
            Some(typedInitializer)
          case None => None
        }
        VarDecl(param.name, evaluatedType, evaluatedInitializer)
      })

      // add parameters to the list of known variables
      typedParams.foreach(param => methodContext.addTypeAssumption(param.name, param.varType))

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
        typedParams,
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

    val typedFields: List[FieldDecl] = cls.fields.map((field: FieldDecl) => {
      val fieldType = context.simpleTypeToQualified(field.varType)
      val typedInitializer = field.initializer match {
        case Some(initializer) => Some(ExpressionChecks.checkExpression(field.initializer.get, context))
        case None              => None
      }
      FieldDecl(field.accessModifier, field.isStatic, field.isFinal, field.name, fieldType, typedInitializer)
    })

    val fullyQualifiedParentName = context.getFullyQualifiedClassName(cls.parent)

    ClassDecl(
      context.getClassName,
      fullyQualifiedParentName,
      cls.isAbstract,
      typedMethods,
      typedFields,
      typedConstructors
    )
  }
}
