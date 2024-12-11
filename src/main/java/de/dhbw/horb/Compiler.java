package de.dhbw.horb;

import de.dhbw.horb.ast.*;
import de.students.antlr.DecafLexer;
import de.students.antlr.DecafParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Decaf language Compiler
 */
public class Compiler
{

    public static Program generateAST(String fromSource){
        CharStream input = CharStreams.fromString(fromSource);
        DecafLexer lexer = new DecafLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DecafParser parser = new DecafParser(tokens);
        DecafParser.ProgramContext tree = parser.program(); //Parsen
        return ASTGenerator.generateAST(tree);
    }


}