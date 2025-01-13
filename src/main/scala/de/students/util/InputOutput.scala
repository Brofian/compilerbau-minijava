package de.students.util

import java.io.File
import scala.io.Source
import scala.util.Using

object InputOutput {

  def getFileContents(filePaths: List[String]): List[String] = {

    val files = filePaths.map { path =>
      val file = new File(path)

      // process Args into a List of file(paths)
      if (!file.exists()) {
        throw new IllegalArgumentException(s"The specified path does not exist: $path")
      } else if (file.isDirectory) {
        file.listFiles().filter(_.isFile).toList
      } else {
        List(file)
      }
    }.flatten()

    
    // get file contents and concat
    val fileContents: List[String] = files.map { f =>
      val source = Source.fromFile(f)
      val lines = try source.mkString catch {
        case e: Exception => throw new RuntimeException(s"Failed to read file: ${f.getAbsolutePath}", e)
      } finally source.close()

      lines
    }

    fileContents
  }

}