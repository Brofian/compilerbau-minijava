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


// enable and configure AntlrPlugin (compile g4 File with `sbt clean compile`)
enablePlugins(Antlr4Plugin)
Antlr4 / antlr4Version := "4.13.2"
Antlr4 / antlr4GenListener := true
Antlr4 / antlr4GenVisitor := true
Antlr4 / antlr4PackageName := Some("de.students.antlr")
// add the generated files to the compile path, as they are nested in an additional antlr4 directory
Compile / unmanagedSourceDirectories += baseDirectory.value / "src" / "main" / "antlr4"