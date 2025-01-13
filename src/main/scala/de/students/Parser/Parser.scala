package de.students.Parser
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.*
import de.students.antlr.*
import de.students.util.Logger

object Parser {
  def main(inputString : String): Unit = {
    // convert to CharStream
    val input = CharStreams.fromString(inputString)
    // generate Lexer
    val lexer = new JavaLexer(input)

    // tokenize
    val tokens = new CommonTokenStream(lexer)

    // generate parser
    val parser = new JavaParser(tokens)

    // generate parsetree
    val tree = parser.package_()

    // print parsetree
    Logger.debug(tree.toStringTree(parser))

    val astBuilder = new ASTBuilder.ASTGenerator()
    val ast = astBuilder.generateAST(tree)
    Logger.debug(ast)
   
  }
}
