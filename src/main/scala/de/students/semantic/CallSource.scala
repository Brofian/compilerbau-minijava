package de.students.semantic

import de.students.Parser.{FieldDecl, MethodDecl, UserType}

class CallSource(
  private val staticCall: Boolean,
  private val context: SemanticContext
) {

  /**
   * Assert, that the method can be called and return false otherwise
   *
   * @param targetDecl     The method or field declaration, that we attempt to call
   * @param className      The class name of the target
   * @return
   */
  def assertCanCall(
    targetDecl: MethodDecl | FieldDecl,
    className: String
  ): Unit = {
    val packageName = this.getPackageNameFromClassName(className)

    var memberName: String = ""
    var accessModifier: Option[String] = None
    val canBeCalled = targetDecl match {
      case methodDecl: MethodDecl =>
        if (!methodDecl.static && staticCall) {
          throw new SemanticException(s"Cannot call non-static method $className:${methodDecl.name} in static context")
        }
        memberName = methodDecl.name
        accessModifier = methodDecl.accessModifier
        this.checkAccessModifier(methodDecl.accessModifier, className, packageName)
      case fieldDecl: FieldDecl =>
        memberName = fieldDecl.name
        accessModifier = fieldDecl.accessModifier
        this.checkAccessModifier(fieldDecl.accessModifier, className, packageName)
    }

    if (!canBeCalled) {
      throw new SemanticException(
        s"Cannot access ${accessModifier.getOrElse("package")} member $className:$memberName from" +
          s" ${context.getClassName}"
      )
    }
  }

  /**
   * Check if the given access modifier matches the correlation of the caller and the called
   *
   * @param accessModifier  Either an access modifier or None
   * @param className       The class name of the target
   * @return
   */
  private def checkAccessModifier(accessModifier: Option[String], className: String, packageName: String): Boolean = {
    val sourceClassName = context.getClassName
    val sourcePackageName = context.getPackageName

    accessModifier match {
      case Some("public") => true // always accessible
      case Some("protected") => // visible in package and all subclasses
        sourcePackageName == packageName || UnionTypeFinder.isASubtypeOfB(
          UserType(sourceClassName),
          UserType(className),
          context.getClassAccessHelper
        )
      case Some("private") => sourceClassName == className // visible in class only
      case _               => sourcePackageName == packageName // typically None, visible in package only
    }
  }

  private def getPackageNameFromClassName(className: String): String = {
    val parts = className.split('.')
    val simpleClassName = parts.last
    className.dropRight(simpleClassName.length + 1)
  }

}
