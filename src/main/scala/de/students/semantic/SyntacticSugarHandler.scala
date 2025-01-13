package de.students.semantic

import de.students.Parser.*
import scala.collection.mutable;


object SyntacticSugarHandler {

  /**
   * If the same package name was used in multiple files, combine these into a single package and merge the class lists
   * @param project The project to handle
   * @return
   */
  def mergeClassesWithSamePackageName(project: Project): Project = {
    val combinedPackageMap = mutable.Map[String, Package]()

    project.packages.foreach((packageSpace: Package) => {
      if (combinedPackageMap.contains(packageSpace.name)) {
        // merge
        combinedPackageMap.put(packageSpace.name, Package(
          packageSpace.name,
          packageSpace.classes ++ combinedPackageMap.apply(packageSpace.name).classes
        ))
      }
      else {
        combinedPackageMap.addOne(packageSpace.name, packageSpace)
      }
    })

    Project(combinedPackageMap.toList.map(e => e._2))
  }


  /**
   * Move the initializer expressions from class fields to the start of every constructor
   * @param cls The class to check
   * @param classContext  The current context of the class
   * @return
   */
  def moveFieldInitializerToConstructor(cls: ClassDecl, classContext: SemanticContext): ClassDecl = {
    
    // TODO
    cls
  }
  
}