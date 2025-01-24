import scala.sys.process._

val scala3Version = "3.6.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "compilerbau-minijava",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "1.0.0" % Test,

    unmanagedJars in Compile += file(baseDirectory.value + "/lib/asm-9.7.jar")
  )


val generateAntlrTask = taskKey[Unit]("generateAntlr")
generateAntlrTask := {
  println("Generating grammar from g4 file")
  val antlrGeneratorLib = "lib/antlr-4.13.2-complete.jar"
  val outputDirectory = "src/main/scala/de/students/antlr"
  val packageName = "de.students.antlr"
  val inputGrammarFile = "src/main/antlr4/de/students/antlr/Java.g4"

  val cmd = s"java -jar ${antlrGeneratorLib} -o ${outputDirectory} -package ${packageName} -listener -visitor -Xexact-output-dir ${inputGrammarFile}"
  println(s" > Executing\n > ${cmd}")
  cmd.!
  println("Finish generation with return code")
}
// uncomment to prepend the antlr code generation to the scala run task
// (run in Compile) := ((run in Compile) dependsOn (generateAntlrTask)).evaluated