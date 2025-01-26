package de.students.Parser

import de.students.antlr.*
import de.students.util.Logger
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA

import java.util
import java.util.BitSet

object Parser {

  def main(inputStrings: List[(String, String)]): Project = {
    Project(
      inputStrings.map(inputString => this.parseTreeFromCode(inputString._1, inputString._2))
    )
  }

  private def parseTreeFromCode(inputString: String, filePath: String): Package = {
    // convert to CharStream
    val input = CharStreams.fromString (inputString)
    // generate Lexer
    val lexer = new JavaLexer (input)

    // tokenize
    val tokens = new CommonTokenStream (lexer)

    // generate parser
    val parser = new JavaParser (tokens)
    // use custom error handler
    parser.removeErrorListeners()
    parser.addErrorListener(new BaseErrorListener() {
      override def syntaxError(recognizer: Recognizer[?, ?], offendingSymbol: Object, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException): Unit = {
        throw new RuntimeException(s"Syntax Error in $filePath at $line:$charPositionInLine: $msg", e)
      }
    })

    // generate parsetree
    val tree = parser.package_ ()

    // print parsetree
    Logger.debug (tree.toStringTree (parser) )

    val astBuilder = new ASTBuilder.ASTGenerator ()
    astBuilder.generateAST (tree)
  }

}