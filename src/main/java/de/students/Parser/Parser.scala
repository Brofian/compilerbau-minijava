package de.students.Parser
import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree._
import de.students.antlr.*

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
    println(tree.toStringTree(parser))

    val astBuilder = new ASTGenerator.ASTBuilder()
    val ast = astBuilder.generateAST(tree)
    println(ast)
   
  }
}
