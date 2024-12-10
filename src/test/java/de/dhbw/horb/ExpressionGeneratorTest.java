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
public class ExpressionGeneratorTest
{
    @Test
    public void binaryTest()
    {
        Expression binary = generateExpression("x + 3");
        assertTrue(binary instanceof Binary);
        assertTrue( ((Binary) binary).left() instanceof Location);
        assertTrue( ((Binary) binary).right() instanceof IntConstant);
    }
    @Test
    public void locationTest()
    {
        Expression location = generateExpression("x");
        assertTrue(location instanceof Location);
        assertTrue(((Location) location).name().equals("x"));
    }

    @Test
    public void methodCallTest()
    {
        Expression mCall = generateExpression("m(x,y)");
        assertTrue(mCall instanceof FunctionCall);
        assertTrue(((FunctionCall) mCall).name().equals("m"));
        assertTrue(((FunctionCall) mCall).args().size() == 2);
    }

    @Test
    public void intConstantTest()
    {
        Expression intCons = generateExpression("3");
        assertTrue(intCons instanceof IntConstant);
        assertTrue(((IntConstant) intCons).value() == 3);
    }
    @Test
    public void boolConstantTest()
    {
        Expression boolCons = generateExpression("true");
        assertTrue(boolCons instanceof BoolConstant);
        assertTrue(((BoolConstant) boolCons).value() == true);
    }

    private Expression generateExpression(String from){
        String inputString = from;
        CharStream input = CharStreams.fromString(inputString);
        DecafLexer lexer = new DecafLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DecafParser parser = new DecafParser(tokens);
        return new ExpressionGenerator().visit(parser.expr());
    }
}