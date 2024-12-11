package de.dhbw.horb;

import de.dhbw.horb.ast.*;
import de.students.antlr.DecafParser;

import java.util.ArrayList;
import java.util.List;

public class ASTGenerator {

    public static Program generateAST(DecafParser.ProgramContext parseTree){
        List<Variable> variables = new ArrayList<>();
        for(DecafParser.VarContext varCtx : parseTree.var()){
            variables.add(generateVariable(varCtx));
        }
        List<Function> funcs = new ArrayList<>();
        for(DecafParser.FuncContext fctx : parseTree.func()){
            funcs.add(generateFunc(fctx));
        }
        return new Program(variables, funcs);
    }

    public static Variable generateVariable(DecafParser.VarContext ctx) {
        return new Variable(ctx.id().getText(), getType(ctx.type()));
    }

    public static Variable generateVariable(DecafParser.ParamContext ctx) {
        return new Variable(ctx.id().getText(), getType(ctx.type()));
    }

    public static Function generateFunc(DecafParser.FuncContext ctx) {
        Type type = ASTGenerator.getType(ctx.type());
        String name = ctx.getText();
        ArrayList<Variable> params = new ArrayList<>();
        if (ctx.params() != null) {
            for (DecafParser.ParamContext paramCtx : ctx.params().param()) {
                Variable param = generateVariable(paramCtx);
                params.add(param);
            }
        }
        Block block = StatementGenerator.readBlockFromContext(ctx.block());
        return new Function(type, name, params, block);
    }

    public static Type getType(DecafParser.TypeContext ctx){
        if(ctx.INT() != null)
            return Type.INT;
        if(ctx.BOOL() != null)
            return Type.BOOL;
        if(ctx.VOID() != null)
            return Type.VOID;
        throw new RuntimeException();
    }
}