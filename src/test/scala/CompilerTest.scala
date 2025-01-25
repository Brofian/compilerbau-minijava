import munit.FunSuite
import scala.sys.process._
import java.io.File

class CompilerTest extends FunSuite {

  val inputDir = new File("input") // Directory containing test Java files
  val javaFiles = inputDir.listFiles().filter(_.getName.endsWith(".java"))

  test("All input Java files should compile successfully") {
    assume(javaFiles.nonEmpty, "No Java test files found in input/")

    javaFiles.foreach { file =>
      val cmd = s"sbt \" run -- ${file.getAbsolutePath} \" "
      println(cmd)
      val exitCode = cmd.!

      assertEquals(exitCode, 0, s"Compilation failed for ${file.getName}")
    }
  }
}
