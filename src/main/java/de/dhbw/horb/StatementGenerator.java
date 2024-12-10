package de.dhbw.horb;

import de.dhbw.horb.ast.*;

import java.util.ArrayList;
import java.util.List;

public class StatementGenerator extends DecafBaseVisitor<Statement> {
    @Override
    public Statement visitAssign(DecafParser.AssignContext ctx) {
        Location loc = new Location(ctx.loc().getText());
        Expression expr = ctx.expr().accept(new ExpressionGenerator());
        return new Assignment(loc, expr);
    }

    @Override
    public Statement visitIf(DecafParser.IfContext ctx) {
        Expression cond = ctx.expr().accept(new ExpressionGenerator());
        Block ifBlock = StatementGenerator.readBlockFromContext(ctx.block(0));
        Block elseBlock;
        if (ctx.block(1) != null) {
            elseBlock = StatementGenerator.readBlockFromContext(ctx.block(1));
        } else {
            elseBlock = new Block(new ArrayList<>(), new ArrayList<>());
        }
        return new IfElse(cond, ifBlock, elseBlock);
    }

    @Override
    public Statement visitWhile(DecafParser.WhileContext ctx) {
        Expression cond = ctx.expr().accept(new ExpressionGenerator());
        Block block = StatementGenerator.readBlockFromContext(ctx.block());
        return new While(cond, block);
    }

    @Override
    public Statement visitReturn(DecafParser.ReturnContext ctx) {
        Expression ret = ctx.expr().accept(new ExpressionGenerator());
        return new Return(ret);
    }

    @Override
    public Statement visitReturnVoid(DecafParser.ReturnVoidContext ctx) {
        return new ReturnVoid();
    }

    @Override
    public Statement visitBreak(DecafParser.BreakContext ctx) {
        return new Break();
    }

    @Override
    public Statement visitContinue(DecafParser.ContinueContext ctx) {
        return new Continue();
    }


    public static Block readBlockFromContext(DecafParser.BlockContext ctx) {
        List<Variable> vars = new ArrayList<>();
        for (DecafParser.VarContext varCtx : ctx.var()) {
            vars.add(new Variable(
                    varCtx.getText(),
                    ASTGenerator.getType(varCtx.type())
            ));
        }

        List<Statement> stmts = new ArrayList<>();
        StatementGenerator stmtGen = new StatementGenerator();
        for (DecafParser.StmtContext stmtCtx : ctx.stmt()) {
            stmts.add(stmtCtx.accept(stmtGen));
        }

        return new Block(vars, stmts);
    }

}