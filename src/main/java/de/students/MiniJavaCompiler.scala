package de.students

import de.dhbw.horb.Compiler
import scala.io.Source
import scala.util.Using
import java.io.File

object MiniJavaCompiler {

  def main(args: Array[String]): Unit = {

    // Check if arguments are provided
    if (args.isEmpty) {
      println("No input folder or files were specified in the arguments.")
      return
    }

    val input = loadFiles(args)

    input match {
      case Some(content) => println(content)
      case None => println("No content could be loaded from the specified paths.")
    }
  }

  val inputString =
    "def int add(int x, int y)\n" +
      "{\n" + "return x + y;\n" +
      "}\n" +
      "def int main()\n" +
      "{\n" +
      "int a;\n" +
      "a = 3;\n" +
      "return add(a, 2);\n" +
      "}"
  // val ast = Compiler.generateAST(inputString)

  // TODO do something useful with the ast

  //System.out.println("This should output the number two: " + ast.methods.size)



  /**
   * Load files from the specified paths. Supports multiple files and folders.
   *
   * @param paths Array of paths to files or folders.
   * @return Option containing the combined file contents as a string, or None if an error occurs.
   */
  def loadFiles(paths: Array[String]): Option[String] = {
    val files = paths.flatMap { path =>
      val file = new File("input/"+ path) // the files should be located in root/input/

      if (!file.exists()) {
        throw new IllegalArgumentException(s"The specified path does not exist: $path")
      } else if (file.isDirectory) {
        file.listFiles().filter(_.isFile).toList
      } else {
        List(file)
      }
    }

    val fileContents = files.flatMap { f =>
      Using(Source.fromFile(f)) { source =>
        source.mkString
      }.recover {
        case e: Exception =>
          throw new RuntimeException(s"Failed to read file: ${f.getAbsolutePath}", e)
      }.toOption
    }

    if (fileContents.nonEmpty) Some(fileContents.mkString("\n")) else None
  }
}