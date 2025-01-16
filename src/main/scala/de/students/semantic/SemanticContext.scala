package de.students.semantic

import de.students.Parser.*

import scala.collection.mutable

class SemanticContext
(
  typeAssumptions: mutable.Map[String, Type],
  classRelations: mutable.Map[String, String],
  imports: mutable.Map[String, String],
  packageName: String,
  className: String,
) {

  // getter methods to access data
  def getPackageName: String = packageName

  def getClassName: String = className

  def getClassRelation(cls: String): Option[String] = classRelations.get(cls)

  def getTypeAssumption(varName: String): Option[Type] = typeAssumptions.get(varName)

  /**
   * copy the current data and return a new context, optionally with changed
   *
   * @param newPackageName  A new package name, of None if there is no change
   * @param newClassName    A new class name, of None if there is no change
   * @return
   */
  def createChildContext(newPackageName: Option[String] = None, newClassName: Option[String] = None): SemanticContext = {
    SemanticContext(
      typeAssumptions.clone(), // prevent new type assumptions from bubbling up
      classRelations, // class relations will not change after they have been set at the beginning
      if newPackageName.isEmpty then imports else imports.clone(), // if we enter a new package (aka file) context
      newPackageName.getOrElse(packageName),
      newClassName.getOrElse(className),
    )
  }

  def addClassRelation(cls: String, parent: String): Unit = {
    classRelations.addOne(cls, parent)
  }

  def addImport(className: String, fullyQualifiedClassName: String): Unit = {
    imports.addOne(className, fullyQualifiedClassName)
  }

  def addTypeAssumption(identifier: String, varType: Type): Unit = {
    if (typeAssumptions.contains(identifier)) {
      throw SemanticException(s"Value of name $identifier is already declared and cannot be redeclared!")
    }
    typeAssumptions.addOne(identifier, varType)
  }

  def getFullyQualifiedClassName(className: String, usePackage: Option[String] = None): String = {
    // do not check imports, when a package is specified
    val importedName: Option[String] = if usePackage.nonEmpty then None else imports.get(className)
    importedName.getOrElse(
      // assume, the class is related to the current package
      usePackage.getOrElse(packageName) + "." + className
    )
  }

  def getFullyQualifiedMemberName(member: String, useClass: Option[String] = None): String = {
    useClass.getOrElse(className) + "->" + member
  }


}