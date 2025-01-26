import de.students.MiniJavaCompiler
import munit.FunSuite

import scala.sys.process.*
import java.io.File
import scala.concurrent.duration.{Duration, FiniteDuration}

class CompilerTest extends FunSuite {

  val inputDir = new File("input") // Directory containing test Java files
  val javaFiles: Array[File] = inputDir.listFiles().filter(_.getName.endsWith(".java"))

  override val munitTimeout: FiniteDuration = Duration(300, "s") // timeout after 5 minutes instead of default 30s
  test("All input Java files should compile successfully") {
    assume(javaFiles.nonEmpty, "No Java test files found in input/")

    javaFiles.foreach { file =>
      val relPath = file.getAbsolutePath.stripPrefix(inputDir.getAbsolutePath)

      try {
        // println(s"> Running mini java compiler with file ${file.getAbsolutePath}")
        MiniJavaCompiler.main(Array("--", file.getAbsolutePath))
      } catch case e @ _ => fail(s"Compilation failed for $relPath with error: " + e)
    }
  }
}
