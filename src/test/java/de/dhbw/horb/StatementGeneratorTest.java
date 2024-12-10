package de.dhbw.horb;

import de.dhbw.horb.ast.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for AST Generation of Expressions.
 */
public class StatementGeneratorTest
{
    @Test
    public void whileTest()
    {
        Statement whileStmt = generateStatement("while(true){}");
        assertTrue(whileStmt instanceof While);
        assertTrue( ((While) whileStmt).block().stmts().size() == 0);
    }

    private Statement generateStatement(String from){
        String inputString = from;
        CharStream input = CharStreams.fromString(inputString);
        DecafLexer lexer = new DecafLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DecafParser parser = new DecafParser(tokens);
        return parser.stmt().accept(new StatementGenerator());
    }
}