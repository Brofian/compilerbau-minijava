package de.students.Parser
import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree._
import de.students.antlr.*

object Parser {
  def main(): Unit = {
    // MiniJava-Quellcode einlesen
    val input = CharStreams.fromFileName("input/secondinput.java")

    // Lexer erzeugen
    val lexer = new JavaLexer(input)

    // Tokenstream erstellen
    val tokens = new CommonTokenStream(lexer)

    // Parser erzeugen
    val parser = new JavaParser(tokens)

    // Parsetree erzeugen
    val tree = parser.package_()

    // Parsetree debuggen 
    println(tree.toStringTree(parser))

   
  }
}
