package de.students.semantic

import de.students.Parser._
import scala.collection.mutable

object UnionTypeFinder {

  def getUnion(typeA: Type, typeB: Type, context: SemanticContext): Type = {

    // special cases
    if (typeA == NoneType) {
      typeB
    }
    else if (typeB == NoneType) {
      typeA
    }
    else {
      // TODO: implement logic
      typeA
    }
  }

  def isASubtypeOfB(typeA: Type, typeB: Type, context: SemanticContext): Boolean  = {
    typeA match {
      case UserType(_) =>
        // TODO: check if typeA is a subtype of typeB
        true
      case _ => typeA == typeB // the trivial case: two primitive types are either equal or not

    }
  }

}