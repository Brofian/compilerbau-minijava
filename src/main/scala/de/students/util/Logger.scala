package de.students.util

enum Verbosity(val level: Int):
  case DEBUG extends Verbosity(0)
  case INFO extends Verbosity(1)
  case NOTICE extends Verbosity(2)
  case ERROR extends Verbosity(3)


object Logger {

  /**
   * Very specific and highly detailed information for debugging
   *
   * @param output The value to output
   */
  def debug(output: Any): Unit = {
    this.log(output, Verbosity.DEBUG)
  }

  /**
   * Informative but generalized information about the current state of the program
   *
   * @param output The value to output
   */
  def info(output: Any): Unit = {
    this.log(output, Verbosity.INFO)
  }

  /**
   * Warnings and output in case of an unexpected or maybe problematic input, that does still work
   *
   * @param output The value to output
   */
  def notice(output: Any): Unit = {
    this.log(output, Verbosity.NOTICE)
  }

  /**
   * Errors on the highest level, this should be used to output something that REALLY SHOULD NOT BE
   *
   * @param output The value to output
   */
  def error(output: Any): Unit = {
    this.log(output, Verbosity.ERROR)
  }


  /**
   * Universal logging method that respects the current verbosity level before outputting
   *
   * @param output    The value to output
   * @param verbosity The output level, that is required to output the message
   */
  private def log(output: Any, verbosity: Verbosity): Unit = {
    if (ArgParser.verbosityLevel.level <= verbosity.level) {
      print(s"[compiler][$verbosity] ")
      println(output)
    }
  }


}