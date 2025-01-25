// Generated from src/main/antlr4/de/students/antlr/Java.g4 by ANTLR 4.13.2
package de.students.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link JavaParser}.
 */
public interface JavaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link JavaParser#package}.
	 * @param ctx the parse tree
	 */
	void enterPackage(JavaParser.PackageContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#package}.
	 * @param ctx the parse tree
	 */
	void exitPackage(JavaParser.PackageContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#imports}.
	 * @param ctx the parse tree
	 */
	void enterImports(JavaParser.ImportsContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#imports}.
	 * @param ctx the parse tree
	 */
	void exitImports(JavaParser.ImportsContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#class}.
	 * @param ctx the parse tree
	 */
	void enterClass(JavaParser.ClassContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#class}.
	 * @param ctx the parse tree
	 */
	void exitClass(JavaParser.ClassContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#classbody}.
	 * @param ctx the parse tree
	 */
	void enterClassbody(JavaParser.ClassbodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#classbody}.
	 * @param ctx the parse tree
	 */
	void exitClassbody(JavaParser.ClassbodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#method}.
	 * @param ctx the parse tree
	 */
	void enterMethod(JavaParser.MethodContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#method}.
	 * @param ctx the parse tree
	 */
	void exitMethod(JavaParser.MethodContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(JavaParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(JavaParser.AttributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#constructor}.
	 * @param ctx the parse tree
	 */
	void enterConstructor(JavaParser.ConstructorContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#constructor}.
	 * @param ctx the parse tree
	 */
	void exitConstructor(JavaParser.ConstructorContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#accessModifier}.
	 * @param ctx the parse tree
	 */
	void enterAccessModifier(JavaParser.AccessModifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#accessModifier}.
	 * @param ctx the parse tree
	 */
	void exitAccessModifier(JavaParser.AccessModifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#classType}.
	 * @param ctx the parse tree
	 */
	void enterClassType(JavaParser.ClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#classType}.
	 * @param ctx the parse tree
	 */
	void exitClassType(JavaParser.ClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#returntype}.
	 * @param ctx the parse tree
	 */
	void enterReturntype(JavaParser.ReturntypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#returntype}.
	 * @param ctx the parse tree
	 */
	void exitReturntype(JavaParser.ReturntypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(JavaParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(JavaParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(JavaParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(JavaParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(JavaParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(JavaParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(JavaParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(JavaParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterStatement(JavaParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitStatement(JavaParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(JavaParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(JavaParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void enterExpressionStatement(JavaParser.ExpressionStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#expressionStatement}.
	 * @param ctx the parse tree
	 */
	void exitExpressionStatement(JavaParser.ExpressionStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(JavaParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#returnStatement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(JavaParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(JavaParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#ifStatement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(JavaParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#elseifStatement}.
	 * @param ctx the parse tree
	 */
	void enterElseifStatement(JavaParser.ElseifStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#elseifStatement}.
	 * @param ctx the parse tree
	 */
	void exitElseifStatement(JavaParser.ElseifStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void enterElseStatement(JavaParser.ElseStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#elseStatement}.
	 * @param ctx the parse tree
	 */
	void exitElseStatement(JavaParser.ElseStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(JavaParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#whileStatement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(JavaParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#doWhileStatement}.
	 * @param ctx the parse tree
	 */
	void enterDoWhileStatement(JavaParser.DoWhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#doWhileStatement}.
	 * @param ctx the parse tree
	 */
	void exitDoWhileStatement(JavaParser.DoWhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(JavaParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#forStatement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(JavaParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#switchStatement}.
	 * @param ctx the parse tree
	 */
	void enterSwitchStatement(JavaParser.SwitchStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#switchStatement}.
	 * @param ctx the parse tree
	 */
	void exitSwitchStatement(JavaParser.SwitchStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#switchCase}.
	 * @param ctx the parse tree
	 */
	void enterSwitchCase(JavaParser.SwitchCaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#switchCase}.
	 * @param ctx the parse tree
	 */
	void exitSwitchCase(JavaParser.SwitchCaseContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStatement(JavaParser.BreakStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#breakStatement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStatement(JavaParser.BreakStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStatement(JavaParser.ContinueStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#continueStatement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStatement(JavaParser.ContinueStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(JavaParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(JavaParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#objectCreation}.
	 * @param ctx the parse tree
	 */
	void enterObjectCreation(JavaParser.ObjectCreationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#objectCreation}.
	 * @param ctx the parse tree
	 */
	void exitObjectCreation(JavaParser.ObjectCreationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#arrayCreation}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreation(JavaParser.ArrayCreationContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#arrayCreation}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreation(JavaParser.ArrayCreationContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#arrayAccess}.
	 * @param ctx the parse tree
	 */
	void enterArrayAccess(JavaParser.ArrayAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#arrayAccess}.
	 * @param ctx the parse tree
	 */
	void exitArrayAccess(JavaParser.ArrayAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(JavaParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(JavaParser.PrimaryContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void enterMethodCall(JavaParser.MethodCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#methodCall}.
	 * @param ctx the parse tree
	 */
	void exitMethodCall(JavaParser.MethodCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#thisAccess}.
	 * @param ctx the parse tree
	 */
	void enterThisAccess(JavaParser.ThisAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#thisAccess}.
	 * @param ctx the parse tree
	 */
	void exitThisAccess(JavaParser.ThisAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#classAccess}.
	 * @param ctx the parse tree
	 */
	void enterClassAccess(JavaParser.ClassAccessContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#classAccess}.
	 * @param ctx the parse tree
	 */
	void exitClassAccess(JavaParser.ClassAccessContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(JavaParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(JavaParser.ArgumentListContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(JavaParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(JavaParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(JavaParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(JavaParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(JavaParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(JavaParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by {@link JavaParser#packageId}.
	 * @param ctx the parse tree
	 */
	void enterPackageId(JavaParser.PackageIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link JavaParser#packageId}.
	 * @param ctx the parse tree
	 */
	void exitPackageId(JavaParser.PackageIdContext ctx);
}