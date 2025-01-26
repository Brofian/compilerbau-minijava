package de.students.util

import de.students.ByteCodeGenerator.ClassBytecode

import java.io.{DataOutputStream, File, FileOutputStream}
import java.nio.file.{Files, Paths}
import scala.io.Source
import scala.util.Using

object InputOutput {

  def getFileContents(filePaths: List[String]): List[(String, String)] = {

    val files: List[(File, String)] = filePaths
      .map { path =>
        val file = new File(path)

        // process Args into a List of file(paths)
        if (!file.exists()) {
          throw new IllegalArgumentException(s"The specified path does not exist: $path")
        } else if (file.isDirectory) {
          file.listFiles().filter(_.isFile).toList.map(f => (f, f.getAbsolutePath.stripPrefix(file.getAbsolutePath)))
        } else {
          List((file, path))
        }
      }
      .flatten()

    // get file contents and concat
    val fileContents: List[(String, String)] = files.map { f =>
      val source = Source.fromFile(f._1)
      val lines =
        try source.mkString
        catch {
          case e: Exception => throw new RuntimeException(s"Failed to read file: ${f._1.getAbsolutePath}", e)
        } finally source.close()

      (lines, f._2)
    }

    fileContents
  }

  private val OUT_DIR = Paths.get(".", "out")
  private val CLASS_FILE_ENDING = ".class"

  private def writeToBinFile(bytecode: Array[Byte], fullFilepath: String): Unit = {
    val outStream = DataOutputStream(new FileOutputStream(fullFilepath))
    bytecode.foreach(byte => outStream.writeByte(byte.asInstanceOf[Int]))
    outStream.close()
  }

  def writeClassFile(bytecode: ClassBytecode): Unit = {
    val folders = bytecode.className.split('.')
    val fullFolderPath = folders.reverse.tail.reverse.foldLeft(OUT_DIR)((a, b) => Paths.get(a.toString, b))
    Files.createDirectories(fullFolderPath)
    writeToBinFile(bytecode.bytecode, Paths.get(fullFolderPath.toString, folders.last + CLASS_FILE_ENDING).toString)
  }
}
