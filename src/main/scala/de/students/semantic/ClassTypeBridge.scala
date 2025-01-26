package de.students.semantic;

import de.students.Parser.*

import java.lang.Class
import java.lang.reflect.{Field, Method}
import scala.collection.mutable
import scala.util.matching.Regex

/**
 * A bridge between the local type system and the predefined classes from the JDK. This allows to simply retrieve
 * types of classes, methods and fields by a fully qualified name, regardless of it being a local or a predefined type.
 *
 * @param baseAST The untyped root project
 */
class ClassTypeBridge(baseAST: Project) {

  /**
   * Get the fully qualified name of the parent of a class with the given fully qualified name
   *
   * @param fullyQualifiedClassName The base class, which parent shall be retrieved
   * @return
   */
  def getClassParent(fullyQualifiedClassName: String): String = {
    // option a: this is our class
    val (packageName, simpleClassName) = this.splitFullyQualifiedClassName(fullyQualifiedClassName)
    val localPackage = baseAST.packages.find(pckg => pckg.name == packageName)
    val localClass =
      if localPackage.isEmpty then None else localPackage.get.classes.find(cls => cls.name == simpleClassName)
    if (localClass.nonEmpty) {
      this
        .resolveTypeInClassContext(UserType(localClass.get.parent), localClass.get, localPackage.get)
        .asInstanceOf[UserType]
        .name
    } else {
      // option b: this is a predefined class from the JDK
      val reflectClass =
        try Some(Class.forName(fullyQualifiedClassName))
        catch case _ => None

      reflectClass match
        case Some(refClass) => refClass.getName
        case None => throw new SemanticException(s"Could not find definition of class ${fullyQualifiedClassName}")
    }
  }

  /**
   * Retrieve the type of specific member of the given class. This method does work for fields and methods
   * at the same time. ClassNames in the retrieved type will already be fully qualified
   *
   * @param fullyQualifiedClassName The class to search in
   * @param memberName  The name of the field or method to search
   * @return
   */
  def getClassMemberType(fullyQualifiedClassName: String, memberName: String): Type = {

    val (packageName, simpleClassName) = this.splitFullyQualifiedClassName(fullyQualifiedClassName)

    val localClassMember =
      this.getClassMemberTypeFromLocalClass(packageName, simpleClassName, fullyQualifiedClassName, memberName)

    localClassMember match {
      case Some(memberType) => memberType // we found a local member! Yay!
      case None =>
        val reflectClassMember = this.getClassMemberTypeFromReflection(fullyQualifiedClassName, memberName)
        reflectClassMember match {
          case Some(memberType) => memberType // we found a member from the JDK! Yay!
          case None =>
            throw new SemanticException(
              s"Could not find definition of member $memberName in class $fullyQualifiedClassName"
            )
        }
    }
  }

  /**
   * Search for the class member in the locally compiled classes
   *
   * @param packageName     The package to search in
   * @param simpleClassName The class to search in
   * @param memberName      The member, which type should be retrieved
   * @return
   */
  private def getClassMemberTypeFromLocalClass(
    packageName: String,
    simpleClassName: String,
    fqClassName: String,
    memberName: String
  ): Option[Type] = {
    // search matching class declarations from baseAST
    val classDecls = baseAST.packages
      .filter(pckg => pckg.name == packageName)
      .flatMap(pckg => pckg.classes.filter(cls => cls.name == simpleClassName).map(cls => (cls, pckg)))

    if (classDecls.size > 1) {
      throw new SemanticException(s"Encountered multiple definitions of class $fqClassName")
    }

    if (classDecls.isEmpty) {
      None // this class is unknown. Maybe a predefined class from the JDK?
    } else {
      val classDecl = classDecls.head._1
      val packageDecl = classDecls.head._2

      val member = classDecl.fields
        .find(field => field.name == memberName)
        .getOrElse(
          classDecl.methods.find(method => method.name == memberName).getOrElse(None)
        )

      val memberType: Type = member match {
        case None => throw new SemanticException(s"Class $fqClassName does not contain a member with name $memberName")
        case FieldDecl(_, _, _, varType, _) => varType
        case MethodDecl(_, _, _, _, _, returnType, params, _) =>
          FunctionType(returnType, params.map(param => param.varType))
      }

      Some(this.resolveTypeInClassContext(memberType, classDecl, packageDecl))
    }
  }

  /**
   * Resolve simple class names to their fully qualified form
   *
   * @param rawType The raw type, which should be made fully qualified
   * @param classDecl The current class we are in
   * @param packageDecl The current package we are in
   * @return
   */
  private def resolveTypeInClassContext(rawType: Type, classDecl: ClassDecl, packageDecl: Package): Type = {
    // gather all mappings from simple class name to fully qualified names
    val knownFullyQualifiedNames: mutable.Map[String, String] = mutable.Map[String, String]()
    knownFullyQualifiedNames.addOne(classDecl.name, packageDecl.name + "." + classDecl.name) // current class
    knownFullyQualifiedNames.addAll(
      packageDecl.imports.names.map(importName => {
        val splitImportName = this.splitFullyQualifiedClassName(importName)
        (splitImportName._2, importName)
      })
    )

    // resolve class names to fully qualified class names
    def resolveClassTypes(rawType: Type): Type = rawType match {
      case UserType(name) =>
        // search imports, otherwise use current package prefix
        UserType(knownFullyQualifiedNames.getOrElse(name, packageDecl.name + "." + name))
      case FunctionType(returnType, paramTypes) =>
        // recursively resolve parts of the function type (typically only one recursion step, as there are no function variables in our java)
        FunctionType(
          resolveClassTypes(returnType),
          paramTypes.map(paramType => resolveClassTypes(paramType))
        )
      case _ => rawType
    }

    resolveClassTypes(rawType)
  }

  /**
   * Search for the class member in the definitions from the JDK
   *
   * @param memberName The member, which type should be retrieved
   * @return
   */
  private def getClassMemberTypeFromReflection(fqClassName: String, memberName: String): Option[Type] = {

    // search class in JDK
    val reflectionClassOption: Option[Class[?]] =
      try Some(Class.forName(fqClassName))
      catch case _ => None
    if (reflectionClassOption.isEmpty) {
      return None
    }
    val reflectionClass = reflectionClassOption.get

    // helper function to convert a reflection type to our custom type case classes
    def reflectionTypeToCustomType(refType: Class[?]): Type = {
      if refType.isPrimitive then
        refType.getName match {
          // boolean, byte, char, short, int, long, float, and double.
          case "boolean" => BoolType
          case "int"     => IntType
          case _         => throw new SemanticException(s"Primitive type $refType is not yet implemented")
        }
      else UserType(refType.getName)
    }

    // check methods
    val reflectionMethods: List[Method] = reflectionClass.getDeclaredMethods.toList
    val matchingMethods = reflectionMethods.filter(method =>
      method.getName == memberName
    ) // todo implement parameter matching for overloading
    if (matchingMethods.nonEmpty) {
      val matchingMethod = matchingMethods.head
      val returnType = reflectionTypeToCustomType(matchingMethod.getReturnType)
      val parameterTypes = matchingMethod.getParameterTypes.map(reflectionTypeToCustomType).toList
      return Some(FunctionType(returnType, parameterTypes))
    }

    // check fields
    val matchingField: Option[Field] =
      try Some(reflectionClass.getDeclaredField(memberName))
      catch case _ => None
    matchingField match {
      case Some(field) => Some(reflectionTypeToCustomType(field.getType))
      case None        => None
    }
  }

  /**
   * Extract the package name and the simple class name from a fully qualified class name
   *
   * @param fqClassName The fully qualified class name that shall be split
   * @return
   */
  private def splitFullyQualifiedClassName(fqClassName: String): (String, String) = {
    val fqParts: Option[Regex.Match] = """(.+\.)*([^.]+)""".r.findFirstMatchIn(fqClassName)

    fqParts match {
      case None => throw new SemanticException(s"Encountered malformed fully qualified class name: $fqClassName")
      case Some(regMatch) =>
        val packageName = if regMatch.group(1).endsWith(".") then regMatch.group(1).dropRight(1) else regMatch.group(1)
        val simpleClassName = regMatch.group(2)
        (packageName, simpleClassName)
    }
  }

}
