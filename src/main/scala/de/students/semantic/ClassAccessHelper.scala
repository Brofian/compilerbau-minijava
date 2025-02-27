package de.students.semantic

import de.students.ByteCodeGenerator.javaSignature
import de.students.Parser.*

class ClassAccessHelper(bridge: ClassTypeBridge) {

  def getBridge: ClassTypeBridge = bridge

  /**
   * Check if a class with the specified class name exists
   *
   * @param fullyQualifiedClassName Name of the class to check
   * @return
   */
  def doesClassExist(fullyQualifiedClassName: String): Boolean = {
    try {
      bridge.getClass(fullyQualifiedClassName)
      true
    } catch case e: SemanticException => false
  }

  /**
   * Get the fully qualified name of the parent of a class with the given fully qualified name
   *
   * @param fullyQualifiedClassName The base class, which parent shall be retrieved
   * @return
   */
  def getClassParent(fullyQualifiedClassName: String): String = {
    if (fullyQualifiedClassName == "java.lang.Object") {
      throw new SemanticException(s"Forbidden attempt to fetch parent of $fullyQualifiedClassName")
    }

    bridge.getClass(fullyQualifiedClassName).parent
  }

  /**
   * Get the fully qualified name of the parent of a class with the given fully qualified name or
   * None, if the requested class is java.lang.Object
   *
   * @param fullyQualifiedClassName The base class, which parent shall be retrieved
   * @return
   */
  def getClassParentOrNone(fullyQualifiedClassName: String): Option[String] = {
    if (fullyQualifiedClassName == "java.lang.Object") {
      None
    } else {
      Some(
        bridge.getClass(fullyQualifiedClassName).parent
      )
    }
  }

  /**
   * Find the method declaration in the given class with the given names and parameters
   * @param qualifiedClassName      The class to search in
   * @param memberName              The name of the field or method to search
   * @param methodParams            When searching for a method, this contains the List of parameter types to match overloading.
   *
   * @return
   */
  def getClassMethodDecl(
    qualifiedClassName: String,
    memberName: String,
    methodParams: Option[List[Type]],
    callSource: Option[CallSource]
  ): Option[MethodDecl] = {
    // search method
    val classDecl = this.bridge.getClass(qualifiedClassName)
    val matchingMethods: List[MethodDecl] = classDecl.methods.filter(methodDecl => {
      methodDecl.name == memberName && // method has the correct name
      methodParams.nonEmpty && // we are searching for a method
      methodDecl.params.size == methodParams.get.size && // number of parameters matches number of arguments
      methodParams.get.zipWithIndex.forall((providedParam, index) => {
        val requiredParam = methodDecl.params.apply(index).varType
        UnionTypeFinder.isASubtypeOfB(requiredParam, providedParam, this)
      })
    })

    matchingMethods.size match {
      case 0 => None
      case 1 =>
        val matchingMethod = matchingMethods.head
        if (callSource.nonEmpty) {
          callSource.get.assertCanCall(matchingMethod, qualifiedClassName)
        }
        Some(matchingMethod)
      case _ =>
        val matchingMethod = matchingMethods.head
        if (callSource.nonEmpty) {
          callSource.get.assertCanCall(matchingMethod, qualifiedClassName)
        }
        Some(matchingMethod)
        // TODO: detect if two overloaded methods are equal in parameters and output an error
        /*
        throw new SemanticException(
          s"Encountered multiple possible overloaded methods for member $memberName of class ${classDecl.name} with parameter types $methodParams"
        )
       */
    }
  }

  def getClassField(
    qualifiedClassName: String,
    memberName: String,
    callSource: Option[CallSource]
  ): Option[FieldDecl] = {
    val classDecl = this.bridge.getClass(qualifiedClassName)
    classDecl.fields.find(fieldDecl => fieldDecl.name == memberName) match {
      case matchingField: Some[FieldDecl] =>
        if (callSource.nonEmpty) {
          callSource.get.assertCanCall(matchingField.get, qualifiedClassName)
        }
        matchingField
      case _ => None
    }
  }

  def getClass(fullyQualifiedName: String): ClassDecl = bridge.getClass(fullyQualifiedName)

  /**
   * Retrieve the type of specific member of the given class. This method does work for fields and methods
   * at the same time. ClassNames in the retrieved type will already be fully qualified
   *
   * @param fullyQualifiedClassName The class to search in
   * @param memberName              The name of the field or method to search
   * @param methodParams            When searching for a method, this contains the List of parameter types to match overloading.
   *                                Set to None when searching for fields and List() for methods without parameters
   * @return
   */
  def getClassMemberType(
    fullyQualifiedClassName: String,
    memberName: String,
    methodParams: Option[List[Type]],
    callSource: Option[CallSource]
  ): Type = {
    var matchingMemberType: Option[Type] = None

    val matchingField = getClassField(fullyQualifiedClassName, memberName, callSource)

    if (matchingField.isDefined && methodParams.isDefined) {
      throw new SemanticException(
        s"Cannot call field ${matchingField.get.name} of class $fullyQualifiedClassName as a method"
      )
    }

    val matchingMethod = getClassMethodDecl(fullyQualifiedClassName, memberName, methodParams, callSource)

    if (matchingMethod.isDefined && methodParams.isEmpty) {
      throw new SemanticException(
        s"Cannot access method ${matchingMethod.get.name} of class $fullyQualifiedClassName as a field"
      )
    }

    (matchingMethod, matchingField) match {
      case (Some(method), None) => FunctionType(method.returnType, method.params.map(_.varType))
      case (None, Some(field))  => field.varType
      case (None, None) => {
        val parent = this.getClassParentOrNone(fullyQualifiedClassName)
        try {
          this.getClassMemberType(
            parent.getOrElse(throw InheritedSemanticException(s"Missing parent of class $fullyQualifiedClassName")),
            memberName,
            methodParams,
            callSource
          )
        } catch {
          case e: InheritedSemanticException =>
            throw new InheritedSemanticException(
              s"No matching member with name \"$memberName\" found in class $fullyQualifiedClassName " +
                (if methodParams.nonEmpty then s"with parameters of type ${methodParams.get}" else "")
            )
        }
      }
      case (Some(method), Some(field)) =>
        throw new InheritedSemanticException(
          s"Field \"$field\" has the same name as \"$method\" in class $fullyQualifiedClassName"
        )
    }
  }

  /**
   * Run the member type check for implicit array classes
   *
   * @param arrayType   The ArrayType for determining types
   * @param memberName  The member that should be accessed
   * @param methodParams  The parameters (if any) to call a method
   * @return
   */
  def getArrayMemberType(
    arrayType: ArrayType,
    memberName: String,
    methodParams: Option[List[Type]]
  ): Type = {
    // length is a special property, that does not exist in the reflection and is only supported by the JVM directly
    if (memberName == "length" && methodParams.isEmpty) {
      return IntType
    }
    val arrayClassName = javaSignature(arrayType).replace('/', '.')
    this.getClassMemberType(arrayClassName, memberName, methodParams, None)
  }

  /**
   * Check if the target class contains a constructor that matches the required parameters
   *
   * @param fullyQualifiedClassName The class name to search in
   * @param constructorParams The parameter types, that must match the constructor
   * @return
   */
  def checkClassConstructorWithParameters(fullyQualifiedClassName: String, constructorParams: List[Type]): Boolean = {
    val classDecl = bridge.getClass(fullyQualifiedClassName)

    // special case: implicit empty constructor
    if (classDecl.constructors.isEmpty) {
      return constructorParams.isEmpty // if no params given, the implicit empty constructor is sufficient
    }

    val matchingConstructors: List[ConstructorDecl] = classDecl.constructors.filter(constructor => {
      constructor.params.size == constructorParams.size &&
      constructor.params.zipWithIndex.forall((requiredParam, index) => {
        val providedParam = constructorParams.apply(index)
        UnionTypeFinder.isASubtypeOfB(requiredParam.varType, providedParam, this)
      })
    })

    matchingConstructors.size match {
      case 0 => false // constructors are not inherited, so we do not have to check the parent
      case 1 => true
      case _ =>
        throw new SemanticException(
          s"Encountered multiple possible overloaded constructors for class $fullyQualifiedClassName with parameter types $constructorParams"
        )
    }
  }
}