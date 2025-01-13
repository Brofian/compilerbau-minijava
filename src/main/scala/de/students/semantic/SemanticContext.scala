package de.students.semantic

import de.students.Parser.*

import scala.collection.mutable


class SemanticContext
(
  typeAssumptions: mutable.Map[String, Type],
  packageName: String,
  className: String,
) {

  def getPackageName: String = packageName
  def getClassName: String = className
  
  def createChildContext(newPackageName: Option[String] = None, newClassName: Option[String] = None): SemanticContext = {
    SemanticContext(
      typeAssumptions.clone(), // prevent new type assumptions from bubbling up
      newPackageName.getOrElse(packageName),
      newClassName.getOrElse(className),
    )
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