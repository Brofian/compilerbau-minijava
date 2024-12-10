package de.dhbw.horb;

import static org.junit.Assert.assertTrue;

import de.dhbw.horb.ast.Program;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class CompilerTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void generateASTTest()
    {
        String inputString = "def int add(int x, int y)\n" +
                "{\n" +
                "return x + y;\n" +
                "}\n" +
                "def int main()\n" +
                "{\n" +
                "int a;\n" +
                "a = 3;\n" +
                "return add(a, 2);\n" +
                "}";
        Program ast = Compiler.generateAST(inputString);
        assertTrue( ast.methods().size() == 2 );
    }
}
