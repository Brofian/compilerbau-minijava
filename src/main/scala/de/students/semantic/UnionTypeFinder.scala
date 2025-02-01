package de.students.semantic

import de.students.Parser.*

import scala.annotation.tailrec

object UnionTypeFinder {

  /**
   * Retrieve the first shared type of two base types or throw an error, if they do not overlap
   *
   * @param typeA             The first type to find the union of
   * @param typeB             The second type to find the union of
   * @param classAccessHelper The class access helper from your context
   * @return
   */
  def getUnion(typeA: Type, typeB: Type, classAccessHelper: ClassAccessHelper): Type = {
    // if types are equal or B is of NoneType, return A
    if (typeA == typeB || typeB == NoneType) {
      return typeA
    }

    typeA match {
      case NoneType => typeB
      case UserType(typeAName) =>
        typeB match {
          case UserType(typeBName) =>
            val typeAParent = classAccessHelper.getClassParentOrNone(typeAName)
            val typeBParent = classAccessHelper.getClassParentOrNone(typeBName)

            if (typeAParent.isEmpty || typeBParent.isEmpty) {
              // if at least one parent is java.lang.Object and both are class types, then this is the union
              UserType("java.lang.Object")
            } else if (this.isASubtypeOfB(UserType(typeAParent.get), typeB, classAccessHelper)) {
              // - check if typeA < typeB
              typeB
            } else if (this.isASubtypeOfB(UserType(typeBParent.get), typeA, classAccessHelper)) {
              // - check if typeA > typeB
              typeA
            } else {
              // - check if typeA and typeB have a common ancestor
              try {
                this.getUnion(UserType(typeAParent.get), UserType(typeBParent.get), classAccessHelper)
              } catch {
                case _: Throwable =>
                  // this branch should not even be possible, as both types must have the shared ancestor java/lang/Object
                  throw SemanticException(
                    s"[Achievement unlocked: How did we get here?] Types $typeA and $typeB do not overlap"
                  )
              }
            }

          case _ => throw SemanticException(s"Types $typeA and $typeB do not overlap")
        }
      case _ => throw SemanticException(s"Types $typeA and $typeB do not overlap")
    }
  }

  /**
   * Check if type A is in any relation a subtype of type B
   *
   * @param typeA             Check if this is the subtype
   * @param typeB             Check if this is the supertype
   * @param classAccessHelper The class access helper from your context
   * @return
   */
  @tailrec
  def isASubtypeOfB(typeA: Type, typeB: Type, classAccessHelper: ClassAccessHelper): Boolean = {
    if (typeA.equals(typeB)) {
      return true
    }

    typeA match {
      case UserType("java.lang.Object") => false // We arrived at java/lang/Object and B is still not equal
      case UserType(className) =>
        val typeAParent = classAccessHelper.getClassParent(className)
        this.isASubtypeOfB(UserType(typeAParent), typeB, classAccessHelper) // check if A's parent is a subtype of B
      case NoneType => typeB.equals(VoidType) || typeB.equals(NoneType)
      case _        => typeA == typeB // the trivial case: two primitive types are either equal or not

    }
  }

  /**
   * Get the larger primitive type out of a combination of two types
   *
   * @param primitiveA First primitive
   * @param primitiveB Second primitive
   * @return
   */
  def getLargerPrimitive(primitiveA: Type, primitiveB: Type): Type = {

    if (primitiveA.equals(primitiveB)) {
      return primitiveA
    }

    // Primitive data types ordered by their size from smallest to largest
    val typeWidening: Seq[Type] = Seq(
      ByteType,
      ShortType,
      IntType,
      LongType,
      FloatType,
      DoubleType
    )

    if (typeWidening.indexOf(primitiveA) > typeWidening.indexOf(primitiveB)) {
      primitiveA
    } else {
      primitiveB
    }
  }
}
