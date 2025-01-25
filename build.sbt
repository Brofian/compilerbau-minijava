import scala.sys.process._

val scala3Version = "3.6.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "compilerbau-minijava",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "org.antlr" % "antlr4" % "4.13.2",               // ANTLR runtime
      "org.ow2.asm" % "asm" % "9.7",                   // ASM core library
      "org.ow2.asm" % "asm-tree" % "9.7",              // ASM tree API (optional)
      "org.antlr" % "antlr4-runtime" % "4.13.2"        // ANTLR runtime dependency
    )
  )

val generateAntlrTask = taskKey[Unit]("generateAntlr")
generateAntlrTask := {
  println("Generating grammar from g4 file")

  val outputDirectory = "src/main/scala/de/students/antlr"
  val packageName = "de.students.antlr"
  val inputGrammarFile = "src/main/antlr4/de/students/antlr/Java.g4"

  val antlr4Command = Seq(
    "java",
    "-cp", (dependencyClasspath in Compile).value.map(_.data).mkString(":"),
    "org.antlr.v4.Tool",
    "-o", outputDirectory,
    "-package", packageName,
    "-listener",
    "-visitor",
    "-Xexact-output-dir",
    inputGrammarFile
  )

  println(s" > Executing\n > ${antlr4Command.mkString(" ")}")
  val returnCode = antlr4Command.!
  println(s"Finished generation with return code: $returnCode")
}

// Uncomment to make ANTLR generation run before compilation
// (Compile / compile) := ((Compile / compile) dependsOn generateAntlrTask).value
