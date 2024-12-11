package de.dhbw.horb;

import de.dhbw.horb.ast.*;
import de.students.antlr.DecafBaseVisitor;
import de.students.antlr.DecafParser;

import java.util.ArrayList;

public class ExpressionGenerator extends DecafBaseVisitor<Expression> {
    @Override
    public Expression visitBinaryOperation(DecafParser.BinaryOperationContext ctx) {
        return generateBinary(ctx);
    }

    @Override
    public Expression visitFunCallExpression(DecafParser.FunCallExpressionContext ctx) {
        String name = ctx.funcCall().id().getText();
        ArrayList<Expression> args = new ArrayList<>();
        for (DecafParser.ExprContext argCtx : ctx.funcCall().args().expr()) {
            Expression arg = argCtx.accept(this);
            args.add(arg);
        }
        return new FunctionCall(name, args);
    }

    @Override
    public Expression visitConstant(DecafParser.ConstantContext ctx) {
        return generateConstant(ctx.literal());
    }

    @Override
    public Expression visitExpression(DecafParser.ExpressionContext ctx) {
        //ParseTree for ( expr )
        //Just pass it down to the inner expr:
        return this.visit(ctx.expr());
    }

    @Override
    public Expression visitLocation(DecafParser.LocationContext ctx) {
        return generateLocation(ctx.loc());
    }

    public static Expression generateConstant(DecafParser.LiteralContext ctx){
        if(ctx.number() != null)
            return new IntConstant(Integer.valueOf(ctx.number().getText()));
        if(ctx.boolean_() != null)
            return new BoolConstant(Boolean.valueOf(ctx.boolean_().getText()));
        throw new RuntimeException();
    }
    public static Operator generateOperator(DecafParser.BinaryOpContext ctx){
        if(ctx.ADD() != null)return Operator.ADD;
        if(ctx.SUB() != null)return Operator.SUB;
        if(ctx.MUL() != null)return Operator.MUL;
        throw new RuntimeException();
    }
    public static Binary generateBinary(DecafParser.BinaryOperationContext ctx){
        ExpressionGenerator eGen = new ExpressionGenerator();
        return new Binary(eGen.visit(ctx.expr().get(0)) // left side
                , generateOperator(ctx.binaryOp()) //operator
                , eGen.visit(ctx.expr().get(1))); //right side
    }
    public static Location generateLocation(DecafParser.LocContext loc) {
        return new Location(loc.id().getText());
    }
}