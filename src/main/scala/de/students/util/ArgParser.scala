package de.students.util

import java.util.InputMismatchException

/**
 * The ArgParser reads the command line arguments and is used as a storage for the parsed values.
 * Other methods should read the arguments from this object
 */
object ArgParser {

  // Values of the command line arguments (or their defaults) that can be read from any part of the code
  // Please do not change these from the outside of the ArgParser object
  var verbosityLevel: Verbosity = Verbosity.ERROR
  var outputDirectory: String = "./out"
  var filesToCompile: List[String] = List()

  // internal references to the input arguments
  private var args: Array[String] = Array()
  private var argIndex: Int = 0

  /**
   * Reset all values to their default to reuse the ArgParser in multiple test cases per run
   */
  private def cleanInit(): Unit = {
    this.verbosityLevel = Verbosity.ERROR
    this.outputDirectory = "./out"
    this.filesToCompile = List()
    this.args = Array()
    this.argIndex = 0
  }

  /**
   * This method should be run at the start of the program to gather the data from the command line arguments
   *
   * @param args Array of command line arguments
   */
  def parseCommandLineArgs(args: Array[String]): Unit = {
    this.cleanInit()
    // setup arguments for use in this singleton
    this.args = args;
    this.argIndex = 0


    // loop over all arguments to extract the information we need
    while (this.argIndex < this.args.length) {

      val currentArgument = this.getNextArgument()

      currentArgument match {
        case "--help" =>
          this.printHelpOutput()
          this.argIndex = this.args.length // the help output will end the program execution
        case "-v" => this.verbosityLevel = Verbosity.NOTICE
        case "-vv" => this.verbosityLevel = Verbosity.INFO
        case "-vvv" => this.verbosityLevel = Verbosity.DEBUG
        case "-o" => this.outputDirectory = this.getNextArgument()
        case "-i" => this.filesToCompile = this.getNextArgument() :: this.filesToCompile
        case "--" =>
          // after the delimiter: treat the rest of the arguments as input files
          args.slice(this.argIndex, this.args.length).foreach(arg =>
            this.filesToCompile = this.getNextArgument() :: this.filesToCompile
          )
          this.argIndex = this.args.length
        case _ => throw new RuntimeException(s"Encountered unknown or unexpected argument \"$currentArgument\"")
      }
    }
  }

  /**
   * Read one more argument and then increment the index or throw an error if none is present
   *
   * @return The next argument in the input sequence
   */
  private def getNextArgument(): String = {
    if (this.argIndex >= this.args.length) {
      throw InputMismatchException("Expected more command line arguments, but got encountered end of input")
    }
    val arg = this.args(this.argIndex)
    this.argIndex += 1
    arg
  }


  /**
   * Print the help text to the console and set the values to prevent the program from doing any further work
   */
  private def printHelpOutput(): Unit = {
    // make sure nothing is executed after this method was run
    this.filesToCompile = List()
    this.verbosityLevel = Verbosity.INFO

    val options: List[(String, String)] = List(
      ("--help", "Show this help and exit the program"),
      ("-v, -vv, -vvv", "Set the verbosity to print more outputs (-vvv for DEBUG level)"),
      ("-o <path>", "Set the output directory for the generated files"),
      ("-i <path>", "Add a single file into the list of files to compile"),
    )

    val examples: List[(String, String)] = List(
      ("sbt \"run -o ~/out -vv -- a.java b.java\"", "Compile the files ./a.java and ./b.java with verbosity level INFO and write the output into the directory ~/out"),
      ("sbt \"run -i a.java\"", "Compile the file ./a.java with default verbosity level ERROR and write the output into the default directory ./out")
    )

    // calculate width of first columns to align second column
    val columnPadding: Int = 5
    val getColumnWidth = (l: List[(String, String)]) => l.map(a => a._1.length).reduce((a: Int, b: Int) => a.max(b))
    val optionColWidth = getColumnWidth(options) + columnPadding
    val exampleColWidth = getColumnWidth(examples) + columnPadding


    // print the program description
    Logger.info("")
    Logger.info("Usage: sbt \"run [OPTIONS] [-- FILES...]\" ")
    Logger.info("Compile FILES as Java Code into .class files with bytecode for the JVM")
    Logger.info("")
    Logger.info("Options:")
    options.foreach(option =>
      val paddedArg = option._1.padTo(optionColWidth, ' ') // stretch the argument to the width of the widest option
      Logger.info(s"\t$paddedArg ${option._2}")
    )
    Logger.info("")
    Logger.info("Examples:")
    examples.foreach(example =>
      val paddedExample = example._1.padTo(exampleColWidth, ' ')
      Logger.info(s"\t$paddedExample ${example._2}")
    )
    Logger.info("")
  }

}