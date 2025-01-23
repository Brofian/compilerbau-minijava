package de.students.util

import de.students.ByteCodeGenerator.ClassBytecode

import java.io.{DataOutputStream, File, FileOutputStream}
import java.nio.file.{Files, Paths}
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

  private val OUT_DIR = Paths.get(".", "out")
  private val CLASS_FILE_ENDING = ".class"

  def writeToBinFile(bytecode: Array[Byte], fullFilepath: String): Unit = {
    val outStream = DataOutputStream(new FileOutputStream(fullFilepath))
    bytecode.foreach(byte => outStream.writeByte(byte.asInstanceOf[Int]))
    outStream.close()
  }

  def writeClassFile(bytecode: ClassBytecode): Unit = {
    val folders = bytecode.className.split('.')
    val fullFolderPath = folders.reverse.tail.reverse.foldLeft(OUT_DIR)((a,b) => Paths.get(a.toString, b))
    Files.createDirectories(fullFolderPath)
    writeToBinFile(bytecode.bytecode, Paths.get(fullFolderPath.toString, folders.last + CLASS_FILE_ENDING).toString)
  }
}