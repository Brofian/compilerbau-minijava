package de.students

import java.io.File
import scala.io.Source
import scala.util.Using

class InputOutput {
  /**
   * Load files from the specified paths. Supports multiple files and folders.
   *
   * @param paths Array of paths to files or folders.
   * @return Option containing the combined file contents as a string, or None if an error occurs.
   */
  def loadFiles(paths: Array[String]): Option[String] = {
    val files = paths.flatMap { path =>
      val file = new File("input/" + path) // the files should be located in root/input/

      // process Args into a List of file(paths)
      if (!file.exists()) {
        throw new IllegalArgumentException(s"The specified path does not exist: $path")
      } else if (file.isDirectory) {
        file.listFiles().filter(_.isFile).toList
      } else {
        List(file)
      }
    }

    // get file contents and concat
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