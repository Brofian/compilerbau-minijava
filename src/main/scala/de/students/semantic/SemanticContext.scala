package de.students.semantic

import de.students.Parser.*

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class SemanticContext(
  classAccessHelper: ClassAccessHelper,
  typeAssumptions: mutable.Map[String, Type],
  staticAssumptions: mutable.Map[String, Boolean],
  imports: mutable.Map[String, String],
  importWildcards: ListBuffer[String],
  packageName: String,
  className: String
) {

  // getter methods to access data
  def getPackageName: String = packageName
  def getClassName: String = className
  def getTypeAssumption(varName: String): Option[Type] = typeAssumptions.get(varName)
  def getStaticAssumption(varName: String): Option[Boolean] = staticAssumptions.get(varName)
  def getClassAccessHelper: ClassAccessHelper = classAccessHelper

  /**
   * copy the current data and return a new context, optionally with changed
   *
   * @param newPackageName A new package name, of None if there is no change
   * @param newClassName   A new class name, of None if there is no change
   * @return
   */
  def createChildContext(
    newPackageName: Option[String] = None,
    newClassName: Option[String] = None
  ): SemanticContext = {
    SemanticContext(
      classAccessHelper = classAccessHelper,
      typeAssumptions = typeAssumptions.clone(), // prevent new type assumptions from bubbling up
      imports =
        if newPackageName.isEmpty then imports else imports.clone(), // if we enter a new package (aka file) context
      importWildcards = if newPackageName.isEmpty then importWildcards else ListBuffer(),
      packageName = newPackageName.getOrElse(packageName),
      className = newClassName.getOrElse(className),
      staticAssumptions = staticAssumptions.clone()
    )
  }

  def addImport(className: String, fullyQualifiedClassName: String): Unit = {
    if (fullyQualifiedClassName.endsWith(".")) {
      importWildcards.addOne(fullyQualifiedClassName)
    } else {
      imports.addOne(className, fullyQualifiedClassName)
    }
  }

  def addTypeAssumption(identifier: String, varType: Type): Unit = {
    if (typeAssumptions.contains(identifier)) {
      throw SemanticException(s"Value of name $identifier is already declared and cannot be redeclared!")
    }
    typeAssumptions.addOne(identifier, varType)
  }

  def addStaticAssumption(identifier: String, isStatic: Boolean): Unit = {
    if (staticAssumptions.contains(identifier)) {
      throw SemanticException(s"Staticness of name $identifier is already set!")
    }
    staticAssumptions.addOne(identifier, isStatic)
  }

  def getFullyQualifiedClassName(className: String, usePackage: Option[String] = None): String = {

    def assertClassIsDefined = (fqClassName: String) =>
      if !classAccessHelper.doesClassExist(fqClassName) then
        throw new SemanticException(s"Referenced class $fqClassName is not defined")

    if (usePackage.nonEmpty) {
      // use static package name
      val fqClassName = usePackage.get + className
      assertClassIsDefined(fqClassName)
      fqClassName
    } else if (className.contains(".") && classAccessHelper.doesClassExist(className)) {
      // the class is already fully qualified
      className
    } else if (imports.contains(className)) {
      // use discrete imports
      val fqClassName = imports(className)
      assertClassIsDefined(fqClassName)
      fqClassName
    } else {
      // check if some wildcard contains a matching class
      val matchingWildcard = importWildcards.find(wildcard =>
        try {
          assertClassIsDefined(wildcard + className)
          true
        } catch case e: SemanticException => false
      )

      // use found wildcard or, as a last resort, the current package name
      val fqClassName =
        if matchingWildcard.nonEmpty then matchingWildcard.get + className else packageName + "." + className
      assertClassIsDefined(fqClassName)
      fqClassName
    }
  }

  def simpleTypeToQualified(simpleType: Type): Type = {
    simpleType match {
      case UserType(clsName)   => UserType(this.getFullyQualifiedClassName(clsName))
      case ArrayType(baseType) => ArrayType(simpleTypeToQualified(baseType))
      case FunctionType(returnType, parameterTypes) =>
        FunctionType(simpleTypeToQualified(returnType), parameterTypes.map(p => simpleTypeToQualified(p)))
      case _ => simpleType
    }
  }
}

/**
 * Shortcut for creating a new context in stacked type checks
 *
 * @param pckgDecl    The surrounding package
 * @param classDecl   The surrounding class
 * @param project     The whole AST for class reference
 * @return
 */
def createContext(pckgDecl: Package, classDecl: ClassDecl, project: Project): SemanticContext = {
  val context = SemanticContext(
    classAccessHelper = ClassAccessHelper(ClassTypeBridge(project)),
    typeAssumptions = mutable.Map[String, Type](),
    staticAssumptions = mutable.Map[String, Boolean](),
    imports = mutable.Map[String, String](),
    importWildcards = ListBuffer(),
    packageName = pckgDecl.name,
    className = classDecl.name
  )

  var hasExplicitJavaLangImport = false
  pckgDecl.imports.names.foreach(importName =>
    val parts = importName.split("""\.""")
    context.addImport(parts.last, importName)
    hasExplicitJavaLangImport ||= importName == "java.lang."
  )
  if (!hasExplicitJavaLangImport) {
    // add implicit java.lang.* import
    context.addImport("", "java.lang.")
  }

  context
}
