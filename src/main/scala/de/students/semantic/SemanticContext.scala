package de.students.semantic

import de.students.Parser.*

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class SemanticContext(
  classAccessHelper: ClassAccessHelper,
  typeAssumptions: mutable.Map[String, Type],
  imports: mutable.Map[String, String],
  importWildcards: ListBuffer[String],
  packageName: String,
  className: String
) {

  // getter methods to access data
  def getPackageName: String = packageName
  def getClassName: String = className
  def getTypeAssumption(varName: String): Option[Type] = typeAssumptions.get(varName)
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
      className = newClassName.getOrElse(className)
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

}
