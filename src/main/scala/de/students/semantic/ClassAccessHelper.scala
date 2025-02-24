package de.students.semantic

import de.students.Parser.*

class ClassAccessHelper(bridge: ClassTypeBridge) {

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
    callSource: CallSource
  ): Type = {
    val classDecl = bridge.getClass(fullyQualifiedClassName)

    var matchingMemberType: Option[Type] = None

    // search fields
    val matchingField: Option[FieldDecl] = classDecl.fields.find(fieldDecl => fieldDecl.name == memberName)
    matchingMemberType = matchingField match {
      case Some(field) =>
        if (methodParams.nonEmpty) {
          throw new SemanticException(s"Cannot call field ${field.name} of class $fullyQualifiedClassName as a method")
        } else {
          callSource.assertCanCall(field, fullyQualifiedClassName)
          Some(field.varType)
        }
      case None => None
    }

    // search method
    val matchingMethods: List[MethodDecl] = classDecl.methods.filter(methodDecl => {
      methodDecl.name == memberName && // method has the correct name
      methodParams.nonEmpty && // we are searching for a method
      methodDecl.params.size == methodParams.get.size && // number of parameters matches number of arguments
      methodParams.get.zipWithIndex.forall((providedParam, index) => {
        val requiredParam = methodDecl.params.apply(index).varType
        UnionTypeFinder.isASubtypeOfB(requiredParam, providedParam, this)
      })
    })

    if (matchingMethods.nonEmpty && matchingMemberType.nonEmpty) {
      throw new SemanticException(s"Duplicate definition for member name $memberName in class $fullyQualifiedClassName")
    }

    val matchingMethod: Option[MethodDecl] = matchingMethods.size match {
      case 0 => None
      case 1 => Some(matchingMethods.head)
      case _ =>
        throw new SemanticException(
          s"Encountered multiple possible overloaded methods for member $memberName of class $fullyQualifiedClassName with parameter types $methodParams"
        )
    }

    matchingMemberType = matchingMethod match {
      case Some(method) =>
        if (methodParams.isEmpty) {
          throw new SemanticException(
            s"Cannot access method ${method.name} of class $fullyQualifiedClassName as a field"
          )
        } else {
          callSource.assertCanCall(method, fullyQualifiedClassName)
          Some(FunctionType(method.returnType, method.params.map(p => p.varType)))
        }
      case None => matchingMemberType
    }

    matchingMemberType match {
      case Some(memberType) => memberType
      case None => // if this class has a parent, check there for a matching member
        this.getClassParentOrNone(fullyQualifiedClassName) match {
          case Some(parentClassName) =>
            try {
              this.getClassMemberType(parentClassName, memberName, methodParams, callSource)
            } catch
              case e: Exception => // if no parent has this member either, let the exception bubble back up
                throw new SemanticException(
                  s"No matching member with name \"$memberName\" found in class $fullyQualifiedClassName " +
                    (if methodParams.nonEmpty then s"with parameters of type ${methodParams.get}" else "")
                )
          case None =>
            throw new SemanticException(
              s"No matching member with name \"$memberName\" found in class $fullyQualifiedClassName " +
                (if methodParams.nonEmpty then s"with parameters of type ${methodParams.get}" else "")
            )
        }
    }
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
