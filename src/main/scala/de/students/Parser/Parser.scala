package de.students.Parser

import de.students.antlr.*
import de.students.util.Logger
import org.antlr.v4.runtime.*

object Parser {

  def main(inputStrings: List[String]): Project = {
    Project(
      inputStrings.map(inputString => this.parseTreeFromCode(inputString))
    )
  }

  private def parseTreeFromCode(inputString: String): Package = {
    // convert to CharStream
    val input = CharStreams.fromString (inputString)
    // generate Lexer
    val lexer = new JavaLexer (input)

    // tokenize
    val tokens = new CommonTokenStream (lexer)

    // generate parser
    val parser = new JavaParser (tokens)

    // generate parsetree
    val tree = parser.package_ ()

    // print parsetree
    Logger.debug (tree.toStringTree (parser) )

    val astBuilder = new ASTBuilder.ASTGenerator ()
    val ast = astBuilder.generateAST (tree)
    Logger.debug (ast)

    ast
  }

}