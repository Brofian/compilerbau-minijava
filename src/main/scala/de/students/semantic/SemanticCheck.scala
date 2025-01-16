package de.students.semantic

import de.students.Parser.*
import de.students.util.Logger

import scala.collection.mutable;

object SemanticCheck {

  /**
   * Entry point for running checks on a program
   *
   * @param project The project to run semantic and type checks against
   */
  def runCheck(project: Project): Project = {
    // create context and make all classes and their fields known to the whole project
    val globalContext = SemanticContext(mutable.Map[String, Type](), mutable.Map[String, String](), mutable.Map[String, String](), "", "")
    this.gatherGlobalTypeAssumptions(project, globalContext)

    val typedPackages = project.packages.map((pckg: Package) => {
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
    // add global imports, that are used over the whole language and do not have to be imported explicitly
    val globalImplicitImports: List[(String, String)] = List(
      ("Object", "java.lang.Object")
    )
    globalImplicitImports.foreach(globalImport => context.addImport(globalImport._1, globalImport._2))



    // add all classes and they properties to the type assumptions
    project.packages.foreach((pckg: Package) => {

      // TODO: read package specific imports from package
      val packageImports: List[(String,String)] = List();
      packageImports.foreach(pckgImport => context.addImport(pckgImport._1, pckgImport._2))

      // use context to determine fully qualified names
      val pckgContext = context.createChildContext(Some(pckg.name))

      // first: add all classes to the type assumptions, so they can be used in methods and fields
      pckg.classes.foreach((cls: ClassDecl) => {
        val fullyQualifiedClassName = pckgContext.getFullyQualifiedClassName(cls.name)
        context.addTypeAssumption(fullyQualifiedClassName, UserType(fullyQualifiedClassName))
      })

      // add all fields and methods to the type assumptions
      pckg.classes.foreach((cls: ClassDecl) => {
        val fullyQualifiedClassName = pckgContext.getFullyQualifiedClassName(cls.name)
        val fullyQualifiedParentName = pckgContext.getFullyQualifiedClassName(cls.parent)
        context.addClassRelation(fullyQualifiedClassName, fullyQualifiedParentName)

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
          val evaluatedReturnType: Type = method.returnType match {
            case UserType(clsName) => UserType(classContext.getFullyQualifiedClassName(clsName))
            case _ => method.returnType
          }

          context.addTypeAssumption(
            fullyQualifiedMethodName,
            FunctionType(evaluatedReturnType, method.params.map(param => {
              param.varType match {
                case UserType(clsName) => UserType(classContext.getFullyQualifiedClassName(clsName))
                case _ => param.varType
              }
            }))
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
      method.params.foreach(param => methodContext.addTypeAssumption(param.name, param.varType match {
        case UserType(clsName) => UserType(methodContext.getFullyQualifiedClassName(clsName))
        case _ => param.varType
      }))

      val evaluatedReturnType = method.returnType match {
        case UserType(clsName) => UserType(methodContext.getFullyQualifiedClassName(clsName))
        case _ => method.returnType
      }

      val typedBody: TypedStatement = StatementChecks.checkStatement(method.body, methodContext)
      if (!UnionTypeFinder.isASubtypeOfB(typedBody.stmtType, evaluatedReturnType, methodContext)) {
        throw new SemanticException(s"Method ${method.name} in ${methodContext.getClassName} with return type ${evaluatedReturnType} cannot return value of type ${typedBody.stmtType}")
      }

      val fullyQualifiedMethodName = context.getFullyQualifiedMemberName(method.name)
      MethodDecl(fullyQualifiedMethodName, method.static, method.isAbstract, evaluatedReturnType, method.params, typedBody)
    })


    // run typeCheck on constructors
    val typedConstructors: List[ConstructorDecl] = cls.constructors.map((constructor: ConstructorDecl) => {
      val methodContext = context.createChildContext()
      // add parameters to the list of known variables
      constructor.params.foreach(param => methodContext.addTypeAssumption(param.name, param.varType match {
        case UserType(clsName) => UserType(methodContext.getFullyQualifiedClassName(clsName))
        case _ => param.varType
      }))

      val typedBody: TypedStatement = StatementChecks.checkStatement(constructor.body, methodContext)
      if (!UnionTypeFinder.isASubtypeOfB(typedBody.stmtType, VoidType, methodContext)) {
        throw new SemanticException(s"Constructor ${constructor.name} cannot return value")
      }
      ConstructorDecl(constructor.name, constructor.params, typedBody)
    })


    val fullyQualifiedParentName = context.getFullyQualifiedClassName(cls.parent)
    ClassDecl(context.getClassName, fullyQualifiedParentName, cls.isAbstract, typedMethods, cls.fields, typedConstructors)
  }
}