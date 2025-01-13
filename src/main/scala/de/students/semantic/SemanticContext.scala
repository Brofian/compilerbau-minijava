package de.students.semantic

import de.students.Parser.*

import scala.collection.mutable

class SemanticContext
(
  typeAssumptions: mutable.Map[String, Type],
  classRelations: mutable.Map[String, String],
  packageName: String,
  className: String,
) {

  // getter methods to access data
  def getPackageName: String = packageName

  def getClassName: String = className

  def getClassRelation(cls: String): Option[String] = classRelations.get(cls)

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
      classRelations,
      newPackageName.getOrElse(packageName),
      newClassName.getOrElse(className),
    )
  }

  def addClassRelation(cls: String, parent: String): Unit = {
    classRelations.addOne(cls, parent)
  }

  def addTypeAssumption(identifier: String, varType: Type): Unit = {
    if (typeAssumptions.contains(identifier)) {
      throw SemanticException(s"Value of name $identifier is already declared and cannot be redeclared!")
    }
    typeAssumptions.addOne(identifier, varType)
  }

  def getFullyQualifiedClassName(childClassName: String): String = {
    packageName + "." + childClassName
  }

  def getFullyQualifiedMemberName(member: String): String = {
    className + "->" + member
  }


}