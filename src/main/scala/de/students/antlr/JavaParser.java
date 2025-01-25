// Generated from src/main/antlr4/de/students/antlr/Java.g4 by ANTLR 4.13.2
package de.students.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class JavaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, T__30=31, 
		T__31=32, T__32=33, T__33=34, T__34=35, T__35=36, T__36=37, T__37=38, 
		T__38=39, T__39=40, T__40=41, T__41=42, CLASS=43, EXTENDS=44, PACKAGE=45, 
		PUBLIC=46, PRIVATE=47, PROTECTED=48, STATIC=49, FINAL=50, ABSTRACT=51, 
		VOID=52, RETURN=53, IMPORT=54, PRIMITIVE_TYPE=55, INTEGER_LITERAL=56, 
		BYTE_LITERAL=57, SHORT_LITERAL=58, LONG_LITERAL=59, DOUBLE_LITERAL=60, 
		FLOAT_LITERAL=61, CHAR_LITERAL=62, STRING_LITERAL=63, BOOLEAN_LITERAL=64, 
		NULL_LITERAL=65, IDENTIFIER=66, SC=67, WS=68, COMMENT=69, MULTILINE_COMMENT=70;
	public static final int
		RULE_package = 0, RULE_imports = 1, RULE_class = 2, RULE_classbody = 3, 
		RULE_method = 4, RULE_attribute = 5, RULE_constructor = 6, RULE_accessModifier = 7, 
		RULE_classType = 8, RULE_returntype = 9, RULE_type = 10, RULE_parameterList = 11, 
		RULE_parameter = 12, RULE_block = 13, RULE_statement = 14, RULE_variableDeclaration = 15, 
		RULE_expressionStatement = 16, RULE_returnStatement = 17, RULE_ifStatement = 18, 
		RULE_elseifStatement = 19, RULE_elseStatement = 20, RULE_whileStatement = 21, 
		RULE_doWhileStatement = 22, RULE_forStatement = 23, RULE_switchStatement = 24, 
		RULE_switchCase = 25, RULE_breakStatement = 26, RULE_continueStatement = 27, 
		RULE_expression = 28, RULE_objectCreation = 29, RULE_arrayCreation = 30, 
		RULE_arrayAccess = 31, RULE_primary = 32, RULE_methodCall = 33, RULE_thisAccess = 34, 
		RULE_classAccess = 35, RULE_argumentList = 36, RULE_operator = 37, RULE_literal = 38, 
		RULE_id = 39, RULE_packageId = 40;
	private static String[] makeRuleNames() {
		return new String[] {
			"package", "imports", "class", "classbody", "method", "attribute", "constructor", 
			"accessModifier", "classType", "returntype", "type", "parameterList", 
			"parameter", "block", "statement", "variableDeclaration", "expressionStatement", 
			"returnStatement", "ifStatement", "elseifStatement", "elseStatement", 
			"whileStatement", "doWhileStatement", "forStatement", "switchStatement", 
			"switchCase", "breakStatement", "continueStatement", "expression", "objectCreation", 
			"arrayCreation", "arrayAccess", "primary", "methodCall", "thisAccess", 
			"classAccess", "argumentList", "operator", "literal", "id", "packageId"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "'}'", "'('", "')'", "'='", "'[]'", "','", "'if'", "'else if'", 
			"'else'", "'while'", "'do'", "'for'", "'switch'", "'case'", "':'", "'default'", 
			"'break'", "'continue'", "'new'", "'['", "']'", "'.'", "'this'", "'+'", 
			"'-'", "'*'", "'/'", "'%'", "'=='", "'!='", "'<'", "'<='", "'>'", "'>='", 
			"'&&'", "'||'", "'+='", "'-='", "'*='", "'/='", "'%='", "'class'", "'extends'", 
			"'package'", "'public'", "'private'", "'protected'", "'static'", "'final'", 
			"'abstract'", "'void'", "'return'", "'import'", null, null, null, null, 
			null, null, null, null, null, null, "'null'", null, "';'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "CLASS", "EXTENDS", "PACKAGE", 
			"PUBLIC", "PRIVATE", "PROTECTED", "STATIC", "FINAL", "ABSTRACT", "VOID", 
			"RETURN", "IMPORT", "PRIMITIVE_TYPE", "INTEGER_LITERAL", "BYTE_LITERAL", 
			"SHORT_LITERAL", "LONG_LITERAL", "DOUBLE_LITERAL", "FLOAT_LITERAL", "CHAR_LITERAL", 
			"STRING_LITERAL", "BOOLEAN_LITERAL", "NULL_LITERAL", "IDENTIFIER", "SC", 
			"WS", "COMMENT", "MULTILINE_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Java.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public JavaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PackageContext extends ParserRuleContext {
		public TerminalNode PACKAGE() { return getToken(JavaParser.PACKAGE, 0); }
		public PackageIdContext packageId() {
			return getRuleContext(PackageIdContext.class,0);
		}
		public TerminalNode SC() { return getToken(JavaParser.SC, 0); }
		public ImportsContext imports() {
			return getRuleContext(ImportsContext.class,0);
		}
		public List<ClassContext> class_() {
			return getRuleContexts(ClassContext.class);
		}
		public ClassContext class_(int i) {
			return getRuleContext(ClassContext.class,i);
		}
		public PackageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_package; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterPackage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitPackage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitPackage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageContext package_() throws RecognitionException {
		PackageContext _localctx = new PackageContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_package);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			match(PACKAGE);
			setState(83);
			packageId();
			setState(84);
			match(SC);
			setState(85);
			imports();
			setState(87); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(86);
				class_();
				}
				}
				setState(89); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 2753177115951104L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImportsContext extends ParserRuleContext {
		public List<TerminalNode> IMPORT() { return getTokens(JavaParser.IMPORT); }
		public TerminalNode IMPORT(int i) {
			return getToken(JavaParser.IMPORT, i);
		}
		public List<PackageIdContext> packageId() {
			return getRuleContexts(PackageIdContext.class);
		}
		public PackageIdContext packageId(int i) {
			return getRuleContext(PackageIdContext.class,i);
		}
		public List<TerminalNode> SC() { return getTokens(JavaParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(JavaParser.SC, i);
		}
		public ImportsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_imports; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterImports(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitImports(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitImports(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportsContext imports() throws RecognitionException {
		ImportsContext _localctx = new ImportsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_imports);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(91);
				match(IMPORT);
				setState(92);
				packageId();
				setState(93);
				match(SC);
				}
				}
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassContext extends ParserRuleContext {
		public ClassTypeContext classType() {
			return getRuleContext(ClassTypeContext.class,0);
		}
		public List<IdContext> id() {
			return getRuleContexts(IdContext.class);
		}
		public IdContext id(int i) {
			return getRuleContext(IdContext.class,i);
		}
		public ClassbodyContext classbody() {
			return getRuleContext(ClassbodyContext.class,0);
		}
		public AccessModifierContext accessModifier() {
			return getRuleContext(AccessModifierContext.class,0);
		}
		public TerminalNode EXTENDS() { return getToken(JavaParser.EXTENDS, 0); }
		public ClassContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_class; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterClass(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitClass(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitClass(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassContext class_() throws RecognitionException {
		ClassContext _localctx = new ClassContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_class);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 492581209243648L) != 0)) {
				{
				setState(100);
				accessModifier();
				}
			}

			setState(103);
			classType();
			setState(104);
			id();
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EXTENDS) {
				{
				setState(105);
				match(EXTENDS);
				setState(106);
				id();
				}
			}

			setState(109);
			classbody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassbodyContext extends ParserRuleContext {
		public List<MethodContext> method() {
			return getRuleContexts(MethodContext.class);
		}
		public MethodContext method(int i) {
			return getRuleContext(MethodContext.class,i);
		}
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public List<ConstructorContext> constructor() {
			return getRuleContexts(ConstructorContext.class);
		}
		public ConstructorContext constructor(int i) {
			return getRuleContext(ConstructorContext.class,i);
		}
		public List<ClassContext> class_() {
			return getRuleContexts(ClassContext.class);
		}
		public ClassContext class_(int i) {
			return getRuleContext(ClassContext.class,i);
		}
		public ClassbodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classbody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterClassbody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitClassbody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitClassbody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassbodyContext classbody() throws RecognitionException {
		ClassbodyContext _localctx = new ClassbodyContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_classbody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			match(T__0);
			setState(118);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 43)) & ~0x3f) == 0 && ((1L << (_la - 43)) & 8393721L) != 0)) {
				{
				setState(116);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(112);
					method();
					}
					break;
				case 2:
					{
					setState(113);
					attribute();
					}
					break;
				case 3:
					{
					setState(114);
					constructor();
					}
					break;
				case 4:
					{
					setState(115);
					class_();
					}
					break;
				}
				}
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(121);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MethodContext extends ParserRuleContext {
		public ReturntypeContext returntype() {
			return getRuleContext(ReturntypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(JavaParser.IDENTIFIER, 0); }
		public AccessModifierContext accessModifier() {
			return getRuleContext(AccessModifierContext.class,0);
		}
		public TerminalNode STATIC() { return getToken(JavaParser.STATIC, 0); }
		public TerminalNode FINAL() { return getToken(JavaParser.FINAL, 0); }
		public TerminalNode ABSTRACT() { return getToken(JavaParser.ABSTRACT, 0); }
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public MethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_method; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitMethod(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitMethod(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodContext method() throws RecognitionException {
		MethodContext _localctx = new MethodContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_method);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 492581209243648L) != 0)) {
				{
				setState(123);
				accessModifier();
				}
			}

			setState(127);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STATIC) {
				{
				setState(126);
				match(STATIC);
				}
			}

			setState(130);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FINAL) {
				{
				setState(129);
				match(FINAL);
				}
			}

			setState(133);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ABSTRACT) {
				{
				setState(132);
				match(ABSTRACT);
				}
			}

			setState(135);
			returntype();
			setState(136);
			match(IDENTIFIER);
			setState(137);
			match(T__2);
			setState(139);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PRIMITIVE_TYPE || _la==IDENTIFIER) {
				{
				setState(138);
				parameterList();
				}
			}

			setState(141);
			match(T__3);
			setState(143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(142);
				block();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AttributeContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(JavaParser.IDENTIFIER, 0); }
		public TerminalNode SC() { return getToken(JavaParser.SC, 0); }
		public AccessModifierContext accessModifier() {
			return getRuleContext(AccessModifierContext.class,0);
		}
		public TerminalNode FINAL() { return getToken(JavaParser.FINAL, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitAttribute(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitAttribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_attribute);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 492581209243648L) != 0)) {
				{
				setState(145);
				accessModifier();
				}
			}

			setState(149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==FINAL) {
				{
				setState(148);
				match(FINAL);
				}
			}

			setState(151);
			type(0);
			setState(152);
			match(IDENTIFIER);
			setState(155);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(153);
				match(T__4);
				setState(154);
				expression(0);
				}
			}

			setState(157);
			match(SC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ConstructorContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public AccessModifierContext accessModifier() {
			return getRuleContext(AccessModifierContext.class,0);
		}
		public ParameterListContext parameterList() {
			return getRuleContext(ParameterListContext.class,0);
		}
		public ConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitConstructor(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitConstructor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConstructorContext constructor() throws RecognitionException {
		ConstructorContext _localctx = new ConstructorContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_constructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 492581209243648L) != 0)) {
				{
				setState(159);
				accessModifier();
				}
			}

			setState(162);
			id();
			setState(163);
			match(T__2);
			setState(165);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PRIMITIVE_TYPE || _la==IDENTIFIER) {
				{
				setState(164);
				parameterList();
				}
			}

			setState(167);
			match(T__3);
			setState(168);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class AccessModifierContext extends ParserRuleContext {
		public TerminalNode PRIVATE() { return getToken(JavaParser.PRIVATE, 0); }
		public TerminalNode PUBLIC() { return getToken(JavaParser.PUBLIC, 0); }
		public TerminalNode PROTECTED() { return getToken(JavaParser.PROTECTED, 0); }
		public AccessModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_accessModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterAccessModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitAccessModifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitAccessModifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AccessModifierContext accessModifier() throws RecognitionException {
		AccessModifierContext _localctx = new AccessModifierContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_accessModifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 492581209243648L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassTypeContext extends ParserRuleContext {
		public TerminalNode CLASS() { return getToken(JavaParser.CLASS, 0); }
		public TerminalNode ABSTRACT() { return getToken(JavaParser.ABSTRACT, 0); }
		public ClassTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterClassType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitClassType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitClassType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassTypeContext classType() throws RecognitionException {
		ClassTypeContext _localctx = new ClassTypeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_classType);
		try {
			setState(175);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CLASS:
				enterOuterAlt(_localctx, 1);
				{
				setState(172);
				match(CLASS);
				}
				break;
			case ABSTRACT:
				enterOuterAlt(_localctx, 2);
				{
				setState(173);
				match(ABSTRACT);
				setState(174);
				match(CLASS);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturntypeContext extends ParserRuleContext {
		public TerminalNode VOID() { return getToken(JavaParser.VOID, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ReturntypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returntype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterReturntype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitReturntype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitReturntype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturntypeContext returntype() throws RecognitionException {
		ReturntypeContext _localctx = new ReturntypeContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_returntype);
		try {
			setState(179);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(177);
				match(VOID);
				}
				break;
			case PRIMITIVE_TYPE:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(178);
				type(0);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeContext extends ParserRuleContext {
		public TerminalNode PRIMITIVE_TYPE() { return getToken(JavaParser.PRIMITIVE_TYPE, 0); }
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		return type(0);
	}

	private TypeContext type(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		TypeContext _localctx = new TypeContext(_ctx, _parentState);
		TypeContext _prevctx = _localctx;
		int _startState = 20;
		enterRecursionRule(_localctx, 20, RULE_type, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(184);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case PRIMITIVE_TYPE:
				{
				setState(182);
				match(PRIMITIVE_TYPE);
				}
				break;
			case IDENTIFIER:
				{
				setState(183);
				id();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(190);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new TypeContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_type);
					setState(186);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(187);
					match(T__5);
					}
					} 
				}
				setState(192);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterListContext extends ParserRuleContext {
		public List<ParameterContext> parameter() {
			return getRuleContexts(ParameterContext.class);
		}
		public ParameterContext parameter(int i) {
			return getRuleContext(ParameterContext.class,i);
		}
		public ParameterListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterParameterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitParameterList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitParameterList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterListContext parameterList() throws RecognitionException {
		ParameterListContext _localctx = new ParameterListContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_parameterList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			parameter();
			setState(198);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(194);
				match(T__6);
				setState(195);
				parameter();
				}
				}
				setState(200);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParameterContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(JavaParser.IDENTIFIER, 0); }
		public ParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitParameter(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitParameter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParameterContext parameter() throws RecognitionException {
		ParameterContext _localctx = new ParameterContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_parameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			type(0);
			setState(202);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204);
			match(T__0);
			setState(208);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & -3377699718197471L) != 0)) {
				{
				{
				setState(205);
				statement();
				}
				}
				setState(210);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(211);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public ExpressionStatementContext expressionStatement() {
			return getRuleContext(ExpressionStatementContext.class,0);
		}
		public ReturnStatementContext returnStatement() {
			return getRuleContext(ReturnStatementContext.class,0);
		}
		public IfStatementContext ifStatement() {
			return getRuleContext(IfStatementContext.class,0);
		}
		public WhileStatementContext whileStatement() {
			return getRuleContext(WhileStatementContext.class,0);
		}
		public ForStatementContext forStatement() {
			return getRuleContext(ForStatementContext.class,0);
		}
		public DoWhileStatementContext doWhileStatement() {
			return getRuleContext(DoWhileStatementContext.class,0);
		}
		public SwitchStatementContext switchStatement() {
			return getRuleContext(SwitchStatementContext.class,0);
		}
		public BreakStatementContext breakStatement() {
			return getRuleContext(BreakStatementContext.class,0);
		}
		public ContinueStatementContext continueStatement() {
			return getRuleContext(ContinueStatementContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_statement);
		try {
			setState(223);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(213);
				variableDeclaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(214);
				expressionStatement();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(215);
				returnStatement();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(216);
				ifStatement();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(217);
				whileStatement();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(218);
				forStatement();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(219);
				doWhileStatement();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(220);
				switchStatement();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(221);
				breakStatement();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(222);
				continueStatement();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class VariableDeclarationContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(JavaParser.IDENTIFIER, 0); }
		public TerminalNode SC() { return getToken(JavaParser.SC, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public VariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_variableDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterVariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitVariableDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VariableDeclarationContext variableDeclaration() throws RecognitionException {
		VariableDeclarationContext _localctx = new VariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_variableDeclaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225);
			type(0);
			setState(226);
			match(IDENTIFIER);
			setState(229);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(227);
				match(T__4);
				setState(228);
				expression(0);
				}
			}

			setState(231);
			match(SC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SC() { return getToken(JavaParser.SC, 0); }
		public ExpressionStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterExpressionStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitExpressionStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitExpressionStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionStatementContext expressionStatement() throws RecognitionException {
		ExpressionStatementContext _localctx = new ExpressionStatementContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_expressionStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(233);
			expression(0);
			setState(234);
			match(SC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnStatementContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(JavaParser.RETURN, 0); }
		public TerminalNode SC() { return getToken(JavaParser.SC, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ReturnStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterReturnStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitReturnStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitReturnStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnStatementContext returnStatement() throws RecognitionException {
		ReturnStatementContext _localctx = new ReturnStatementContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_returnStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(236);
			match(RETURN);
			setState(238);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & -9007199252512767L) != 0)) {
				{
				setState(237);
				expression(0);
				}
			}

			setState(240);
			match(SC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IfStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public List<ElseifStatementContext> elseifStatement() {
			return getRuleContexts(ElseifStatementContext.class);
		}
		public ElseifStatementContext elseifStatement(int i) {
			return getRuleContext(ElseifStatementContext.class,i);
		}
		public ElseStatementContext elseStatement() {
			return getRuleContext(ElseStatementContext.class,0);
		}
		public IfStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ifStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterIfStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitIfStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitIfStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IfStatementContext ifStatement() throws RecognitionException {
		IfStatementContext _localctx = new IfStatementContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_ifStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			match(T__7);
			setState(243);
			match(T__2);
			setState(244);
			expression(0);
			setState(245);
			match(T__3);
			setState(246);
			block();
			setState(250);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__8) {
				{
				{
				setState(247);
				elseifStatement();
				}
				}
				setState(252);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(254);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(253);
				elseStatement();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseifStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ElseifStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseifStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterElseifStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitElseifStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitElseifStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseifStatementContext elseifStatement() throws RecognitionException {
		ElseifStatementContext _localctx = new ElseifStatementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_elseifStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			match(T__8);
			setState(257);
			match(T__2);
			setState(258);
			expression(0);
			setState(259);
			match(T__3);
			setState(260);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ElseStatementContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ElseStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_elseStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterElseStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitElseStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitElseStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElseStatementContext elseStatement() throws RecognitionException {
		ElseStatementContext _localctx = new ElseStatementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_elseStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			match(T__9);
			setState(263);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WhileStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public WhileStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_whileStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterWhileStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitWhileStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitWhileStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WhileStatementContext whileStatement() throws RecognitionException {
		WhileStatementContext _localctx = new WhileStatementContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_whileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(265);
			match(T__10);
			setState(266);
			match(T__2);
			setState(267);
			expression(0);
			setState(268);
			match(T__3);
			setState(269);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DoWhileStatementContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode SC() { return getToken(JavaParser.SC, 0); }
		public DoWhileStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doWhileStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterDoWhileStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitDoWhileStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitDoWhileStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DoWhileStatementContext doWhileStatement() throws RecognitionException {
		DoWhileStatementContext _localctx = new DoWhileStatementContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_doWhileStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			match(T__11);
			setState(272);
			block();
			setState(273);
			match(T__10);
			setState(274);
			match(T__2);
			setState(275);
			expression(0);
			setState(276);
			match(T__3);
			setState(277);
			match(SC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForStatementContext extends ParserRuleContext {
		public List<TerminalNode> SC() { return getTokens(JavaParser.SC); }
		public TerminalNode SC(int i) {
			return getToken(JavaParser.SC, i);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public VariableDeclarationContext variableDeclaration() {
			return getRuleContext(VariableDeclarationContext.class,0);
		}
		public ExpressionStatementContext expressionStatement() {
			return getRuleContext(ExpressionStatementContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ForStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterForStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitForStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitForStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForStatementContext forStatement() throws RecognitionException {
		ForStatementContext _localctx = new ForStatementContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_forStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			match(T__12);
			setState(280);
			match(T__2);
			setState(284);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,28,_ctx) ) {
			case 1:
				{
				setState(281);
				variableDeclaration();
				}
				break;
			case 2:
				{
				setState(282);
				expressionStatement();
				}
				break;
			case 3:
				{
				setState(283);
				match(SC);
				}
				break;
			}
			setState(287);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & -9007199252512767L) != 0)) {
				{
				setState(286);
				expression(0);
				}
			}

			setState(289);
			match(SC);
			setState(291);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & -9007199252512767L) != 0)) {
				{
				setState(290);
				expression(0);
				}
			}

			setState(293);
			match(T__3);
			setState(294);
			block();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchStatementContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<SwitchCaseContext> switchCase() {
			return getRuleContexts(SwitchCaseContext.class);
		}
		public SwitchCaseContext switchCase(int i) {
			return getRuleContext(SwitchCaseContext.class,i);
		}
		public SwitchStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterSwitchStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitSwitchStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitSwitchStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchStatementContext switchStatement() throws RecognitionException {
		SwitchStatementContext _localctx = new SwitchStatementContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_switchStatement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(296);
			match(T__13);
			setState(297);
			match(T__2);
			setState(298);
			expression(0);
			setState(299);
			match(T__3);
			setState(300);
			match(T__0);
			setState(304);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14 || _la==T__16) {
				{
				{
				setState(301);
				switchCase();
				}
				}
				setState(306);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(307);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SwitchCaseContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public SwitchCaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_switchCase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterSwitchCase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitSwitchCase(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitSwitchCase(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SwitchCaseContext switchCase() throws RecognitionException {
		SwitchCaseContext _localctx = new SwitchCaseContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_switchCase);
		try {
			setState(317);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__14:
				enterOuterAlt(_localctx, 1);
				{
				setState(309);
				match(T__14);
				setState(310);
				literal();
				setState(311);
				match(T__15);
				setState(312);
				block();
				}
				break;
			case T__16:
				enterOuterAlt(_localctx, 2);
				{
				setState(314);
				match(T__16);
				setState(315);
				match(T__15);
				setState(316);
				block();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BreakStatementContext extends ParserRuleContext {
		public TerminalNode SC() { return getToken(JavaParser.SC, 0); }
		public BreakStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_breakStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterBreakStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitBreakStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitBreakStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BreakStatementContext breakStatement() throws RecognitionException {
		BreakStatementContext _localctx = new BreakStatementContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_breakStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(319);
			match(T__17);
			setState(320);
			match(SC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ContinueStatementContext extends ParserRuleContext {
		public TerminalNode SC() { return getToken(JavaParser.SC, 0); }
		public ContinueStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_continueStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterContinueStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitContinueStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitContinueStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ContinueStatementContext continueStatement() throws RecognitionException {
		ContinueStatementContext _localctx = new ContinueStatementContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_continueStatement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(322);
			match(T__18);
			setState(323);
			match(SC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public MethodCallContext methodCall() {
			return getRuleContext(MethodCallContext.class,0);
		}
		public ThisAccessContext thisAccess() {
			return getRuleContext(ThisAccessContext.class,0);
		}
		public ArrayAccessContext arrayAccess() {
			return getRuleContext(ArrayAccessContext.class,0);
		}
		public ObjectCreationContext objectCreation() {
			return getRuleContext(ObjectCreationContext.class,0);
		}
		public ArrayCreationContext arrayCreation() {
			return getRuleContext(ArrayCreationContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 56;
		enterRecursionRule(_localctx, 56, RULE_expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(337);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				{
				setState(326);
				literal();
				}
				break;
			case 2:
				{
				setState(327);
				primary();
				}
				break;
			case 3:
				{
				setState(328);
				methodCall();
				}
				break;
			case 4:
				{
				setState(329);
				thisAccess();
				}
				break;
			case 5:
				{
				setState(330);
				arrayAccess();
				}
				break;
			case 6:
				{
				setState(331);
				objectCreation();
				}
				break;
			case 7:
				{
				setState(332);
				arrayCreation();
				}
				break;
			case 8:
				{
				setState(333);
				match(T__2);
				setState(334);
				expression(0);
				setState(335);
				match(T__3);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(345);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new ExpressionContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_expression);
					setState(339);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(340);
					operator();
					setState(341);
					expression(2);
					}
					} 
				}
				setState(347);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ObjectCreationContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ObjectCreationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectCreation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterObjectCreation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitObjectCreation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitObjectCreation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectCreationContext objectCreation() throws RecognitionException {
		ObjectCreationContext _localctx = new ObjectCreationContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_objectCreation);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(348);
			match(T__19);
			setState(349);
			id();
			setState(350);
			match(T__2);
			setState(352);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & -9007199252512767L) != 0)) {
				{
				setState(351);
				argumentList();
				}
			}

			setState(354);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayCreationContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArrayCreationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayCreation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterArrayCreation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitArrayCreation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitArrayCreation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayCreationContext arrayCreation() throws RecognitionException {
		ArrayCreationContext _localctx = new ArrayCreationContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_arrayCreation);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(356);
			match(T__19);
			setState(357);
			type(0);
			setState(362); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(358);
					match(T__20);
					setState(359);
					expression(0);
					setState(360);
					match(T__21);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(364); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,36,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayAccessContext extends ParserRuleContext {
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ArrayAccessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayAccess; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterArrayAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitArrayAccess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitArrayAccess(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayAccessContext arrayAccess() throws RecognitionException {
		ArrayAccessContext _localctx = new ArrayAccessContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_arrayAccess);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(366);
			primary();
			setState(367);
			match(T__20);
			setState(368);
			expression(0);
			setState(369);
			match(T__21);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(JavaParser.IDENTIFIER, 0); }
		public ThisAccessContext thisAccess() {
			return getRuleContext(ThisAccessContext.class,0);
		}
		public ClassAccessContext classAccess() {
			return getRuleContext(ClassAccessContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ObjectCreationContext objectCreation() {
			return getRuleContext(ObjectCreationContext.class,0);
		}
		public ArrayCreationContext arrayCreation() {
			return getRuleContext(ArrayCreationContext.class,0);
		}
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterPrimary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitPrimary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitPrimary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_primary);
		try {
			setState(380);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(371);
				match(IDENTIFIER);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(372);
				thisAccess();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(373);
				classAccess();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(374);
				match(T__2);
				setState(375);
				expression(0);
				setState(376);
				match(T__3);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(378);
				objectCreation();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(379);
				arrayCreation();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class MethodCallContext extends ParserRuleContext {
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public List<TerminalNode> IDENTIFIER() { return getTokens(JavaParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(JavaParser.IDENTIFIER, i);
		}
		public List<ArgumentListContext> argumentList() {
			return getRuleContexts(ArgumentListContext.class);
		}
		public ArgumentListContext argumentList(int i) {
			return getRuleContext(ArgumentListContext.class,i);
		}
		public MethodCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterMethodCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitMethodCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitMethodCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MethodCallContext methodCall() throws RecognitionException {
		MethodCallContext _localctx = new MethodCallContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_methodCall);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(382);
			primary();
			setState(392);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(383);
					match(T__22);
					setState(384);
					match(IDENTIFIER);
					setState(385);
					match(T__2);
					setState(387);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (((((_la - 3)) & ~0x3f) == 0 && ((1L << (_la - 3)) & -9007199252512767L) != 0)) {
						{
						setState(386);
						argumentList();
						}
					}

					setState(389);
					match(T__3);
					}
					} 
				}
				setState(394);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,39,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ThisAccessContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(JavaParser.IDENTIFIER, 0); }
		public ThisAccessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_thisAccess; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterThisAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitThisAccess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitThisAccess(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ThisAccessContext thisAccess() throws RecognitionException {
		ThisAccessContext _localctx = new ThisAccessContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_thisAccess);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(395);
			match(T__23);
			setState(396);
			match(T__22);
			setState(397);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ClassAccessContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(JavaParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(JavaParser.IDENTIFIER, i);
		}
		public ClassAccessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_classAccess; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterClassAccess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitClassAccess(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitClassAccess(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ClassAccessContext classAccess() throws RecognitionException {
		ClassAccessContext _localctx = new ClassAccessContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_classAccess);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(399);
			match(IDENTIFIER);
			setState(400);
			match(T__22);
			setState(401);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArgumentListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(403);
			expression(0);
			setState(408);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(404);
				match(T__6);
				setState(405);
				expression(0);
				}
				}
				setState(410);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperatorContext extends ParserRuleContext {
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 8796059467808L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode INTEGER_LITERAL() { return getToken(JavaParser.INTEGER_LITERAL, 0); }
		public TerminalNode BYTE_LITERAL() { return getToken(JavaParser.BYTE_LITERAL, 0); }
		public TerminalNode SHORT_LITERAL() { return getToken(JavaParser.SHORT_LITERAL, 0); }
		public TerminalNode LONG_LITERAL() { return getToken(JavaParser.LONG_LITERAL, 0); }
		public TerminalNode DOUBLE_LITERAL() { return getToken(JavaParser.DOUBLE_LITERAL, 0); }
		public TerminalNode FLOAT_LITERAL() { return getToken(JavaParser.FLOAT_LITERAL, 0); }
		public TerminalNode CHAR_LITERAL() { return getToken(JavaParser.CHAR_LITERAL, 0); }
		public TerminalNode BOOLEAN_LITERAL() { return getToken(JavaParser.BOOLEAN_LITERAL, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(JavaParser.STRING_LITERAL, 0); }
		public TerminalNode NULL_LITERAL() { return getToken(JavaParser.NULL_LITERAL, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
			_la = _input.LA(1);
			if ( !(((((_la - 56)) & ~0x3f) == 0 && ((1L << (_la - 56)) & 1023L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(JavaParser.IDENTIFIER, 0); }
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_id);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(415);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PackageIdContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(JavaParser.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(JavaParser.IDENTIFIER, i);
		}
		public PackageIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_packageId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).enterPackageId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof JavaListener ) ((JavaListener)listener).exitPackageId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof JavaVisitor ) return ((JavaVisitor<? extends T>)visitor).visitPackageId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PackageIdContext packageId() throws RecognitionException {
		PackageIdContext _localctx = new PackageIdContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_packageId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(417);
			match(IDENTIFIER);
			setState(422);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__22) {
				{
				{
				setState(418);
				match(T__22);
				setState(419);
				match(IDENTIFIER);
				}
				}
				setState(424);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 10:
			return type_sempred((TypeContext)_localctx, predIndex);
		case 28:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean type_sempred(TypeContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001F\u01aa\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
		"(\u0007(\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0004"+
		"\u0000X\b\u0000\u000b\u0000\f\u0000Y\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0005\u0001`\b\u0001\n\u0001\f\u0001c\t\u0001\u0001\u0002"+
		"\u0003\u0002f\b\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0003\u0002l\b\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003u\b\u0003\n\u0003\f\u0003"+
		"x\t\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0003\u0004}\b\u0004\u0001"+
		"\u0004\u0003\u0004\u0080\b\u0004\u0001\u0004\u0003\u0004\u0083\b\u0004"+
		"\u0001\u0004\u0003\u0004\u0086\b\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0003\u0004\u008c\b\u0004\u0001\u0004\u0001\u0004\u0003\u0004"+
		"\u0090\b\u0004\u0001\u0005\u0003\u0005\u0093\b\u0005\u0001\u0005\u0003"+
		"\u0005\u0096\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003"+
		"\u0005\u009c\b\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0003\u0006\u00a1"+
		"\b\u0006\u0001\u0006\u0001\u0006\u0001\u0006\u0003\u0006\u00a6\b\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001"+
		"\b\u0001\b\u0003\b\u00b0\b\b\u0001\t\u0001\t\u0003\t\u00b4\b\t\u0001\n"+
		"\u0001\n\u0001\n\u0003\n\u00b9\b\n\u0001\n\u0001\n\u0005\n\u00bd\b\n\n"+
		"\n\f\n\u00c0\t\n\u0001\u000b\u0001\u000b\u0001\u000b\u0005\u000b\u00c5"+
		"\b\u000b\n\u000b\f\u000b\u00c8\t\u000b\u0001\f\u0001\f\u0001\f\u0001\r"+
		"\u0001\r\u0005\r\u00cf\b\r\n\r\f\r\u00d2\t\r\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0003\u000e\u00e0\b\u000e\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u00e6\b\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011"+
		"\u0003\u0011\u00ef\b\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012"+
		"\u0001\u0012\u0001\u0012\u0001\u0012\u0001\u0012\u0005\u0012\u00f9\b\u0012"+
		"\n\u0012\f\u0012\u00fc\t\u0012\u0001\u0012\u0003\u0012\u00ff\b\u0012\u0001"+
		"\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001\u0013\u0001"+
		"\u0014\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u011d\b\u0017\u0001"+
		"\u0017\u0003\u0017\u0120\b\u0017\u0001\u0017\u0001\u0017\u0003\u0017\u0124"+
		"\b\u0017\u0001\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0005\u0018\u012f\b\u0018\n"+
		"\u0018\f\u0018\u0132\t\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001"+
		"\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0003\u0019\u013e\b\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0003\u001c\u0152\b\u001c\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0005\u001c\u0158\b\u001c\n\u001c\f\u001c"+
		"\u015b\t\u001c\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0003\u001d"+
		"\u0161\b\u001d\u0001\u001d\u0001\u001d\u0001\u001e\u0001\u001e\u0001\u001e"+
		"\u0001\u001e\u0001\u001e\u0001\u001e\u0004\u001e\u016b\b\u001e\u000b\u001e"+
		"\f\u001e\u016c\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0001 \u0003"+
		" \u017d\b \u0001!\u0001!\u0001!\u0001!\u0001!\u0003!\u0184\b!\u0001!\u0005"+
		"!\u0187\b!\n!\f!\u018a\t!\u0001\"\u0001\"\u0001\"\u0001\"\u0001#\u0001"+
		"#\u0001#\u0001#\u0001$\u0001$\u0001$\u0005$\u0197\b$\n$\f$\u019a\t$\u0001"+
		"%\u0001%\u0001&\u0001&\u0001\'\u0001\'\u0001(\u0001(\u0001(\u0005(\u01a5"+
		"\b(\n(\f(\u01a8\t(\u0001(\u0000\u0002\u00148)\u0000\u0002\u0004\u0006"+
		"\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,."+
		"02468:<>@BDFHJLNP\u0000\u0003\u0001\u0000.0\u0002\u0000\u0005\u0005\u0019"+
		"*\u0001\u00008A\u01bf\u0000R\u0001\u0000\u0000\u0000\u0002a\u0001\u0000"+
		"\u0000\u0000\u0004e\u0001\u0000\u0000\u0000\u0006o\u0001\u0000\u0000\u0000"+
		"\b|\u0001\u0000\u0000\u0000\n\u0092\u0001\u0000\u0000\u0000\f\u00a0\u0001"+
		"\u0000\u0000\u0000\u000e\u00aa\u0001\u0000\u0000\u0000\u0010\u00af\u0001"+
		"\u0000\u0000\u0000\u0012\u00b3\u0001\u0000\u0000\u0000\u0014\u00b8\u0001"+
		"\u0000\u0000\u0000\u0016\u00c1\u0001\u0000\u0000\u0000\u0018\u00c9\u0001"+
		"\u0000\u0000\u0000\u001a\u00cc\u0001\u0000\u0000\u0000\u001c\u00df\u0001"+
		"\u0000\u0000\u0000\u001e\u00e1\u0001\u0000\u0000\u0000 \u00e9\u0001\u0000"+
		"\u0000\u0000\"\u00ec\u0001\u0000\u0000\u0000$\u00f2\u0001\u0000\u0000"+
		"\u0000&\u0100\u0001\u0000\u0000\u0000(\u0106\u0001\u0000\u0000\u0000*"+
		"\u0109\u0001\u0000\u0000\u0000,\u010f\u0001\u0000\u0000\u0000.\u0117\u0001"+
		"\u0000\u0000\u00000\u0128\u0001\u0000\u0000\u00002\u013d\u0001\u0000\u0000"+
		"\u00004\u013f\u0001\u0000\u0000\u00006\u0142\u0001\u0000\u0000\u00008"+
		"\u0151\u0001\u0000\u0000\u0000:\u015c\u0001\u0000\u0000\u0000<\u0164\u0001"+
		"\u0000\u0000\u0000>\u016e\u0001\u0000\u0000\u0000@\u017c\u0001\u0000\u0000"+
		"\u0000B\u017e\u0001\u0000\u0000\u0000D\u018b\u0001\u0000\u0000\u0000F"+
		"\u018f\u0001\u0000\u0000\u0000H\u0193\u0001\u0000\u0000\u0000J\u019b\u0001"+
		"\u0000\u0000\u0000L\u019d\u0001\u0000\u0000\u0000N\u019f\u0001\u0000\u0000"+
		"\u0000P\u01a1\u0001\u0000\u0000\u0000RS\u0005-\u0000\u0000ST\u0003P(\u0000"+
		"TU\u0005C\u0000\u0000UW\u0003\u0002\u0001\u0000VX\u0003\u0004\u0002\u0000"+
		"WV\u0001\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000YW\u0001\u0000\u0000"+
		"\u0000YZ\u0001\u0000\u0000\u0000Z\u0001\u0001\u0000\u0000\u0000[\\\u0005"+
		"6\u0000\u0000\\]\u0003P(\u0000]^\u0005C\u0000\u0000^`\u0001\u0000\u0000"+
		"\u0000_[\u0001\u0000\u0000\u0000`c\u0001\u0000\u0000\u0000a_\u0001\u0000"+
		"\u0000\u0000ab\u0001\u0000\u0000\u0000b\u0003\u0001\u0000\u0000\u0000"+
		"ca\u0001\u0000\u0000\u0000df\u0003\u000e\u0007\u0000ed\u0001\u0000\u0000"+
		"\u0000ef\u0001\u0000\u0000\u0000fg\u0001\u0000\u0000\u0000gh\u0003\u0010"+
		"\b\u0000hk\u0003N\'\u0000ij\u0005,\u0000\u0000jl\u0003N\'\u0000ki\u0001"+
		"\u0000\u0000\u0000kl\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000\u0000"+
		"mn\u0003\u0006\u0003\u0000n\u0005\u0001\u0000\u0000\u0000ov\u0005\u0001"+
		"\u0000\u0000pu\u0003\b\u0004\u0000qu\u0003\n\u0005\u0000ru\u0003\f\u0006"+
		"\u0000su\u0003\u0004\u0002\u0000tp\u0001\u0000\u0000\u0000tq\u0001\u0000"+
		"\u0000\u0000tr\u0001\u0000\u0000\u0000ts\u0001\u0000\u0000\u0000ux\u0001"+
		"\u0000\u0000\u0000vt\u0001\u0000\u0000\u0000vw\u0001\u0000\u0000\u0000"+
		"wy\u0001\u0000\u0000\u0000xv\u0001\u0000\u0000\u0000yz\u0005\u0002\u0000"+
		"\u0000z\u0007\u0001\u0000\u0000\u0000{}\u0003\u000e\u0007\u0000|{\u0001"+
		"\u0000\u0000\u0000|}\u0001\u0000\u0000\u0000}\u007f\u0001\u0000\u0000"+
		"\u0000~\u0080\u00051\u0000\u0000\u007f~\u0001\u0000\u0000\u0000\u007f"+
		"\u0080\u0001\u0000\u0000\u0000\u0080\u0082\u0001\u0000\u0000\u0000\u0081"+
		"\u0083\u00052\u0000\u0000\u0082\u0081\u0001\u0000\u0000\u0000\u0082\u0083"+
		"\u0001\u0000\u0000\u0000\u0083\u0085\u0001\u0000\u0000\u0000\u0084\u0086"+
		"\u00053\u0000\u0000\u0085\u0084\u0001\u0000\u0000\u0000\u0085\u0086\u0001"+
		"\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u0088\u0003"+
		"\u0012\t\u0000\u0088\u0089\u0005B\u0000\u0000\u0089\u008b\u0005\u0003"+
		"\u0000\u0000\u008a\u008c\u0003\u0016\u000b\u0000\u008b\u008a\u0001\u0000"+
		"\u0000\u0000\u008b\u008c\u0001\u0000\u0000\u0000\u008c\u008d\u0001\u0000"+
		"\u0000\u0000\u008d\u008f\u0005\u0004\u0000\u0000\u008e\u0090\u0003\u001a"+
		"\r\u0000\u008f\u008e\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000\u0000"+
		"\u0000\u0090\t\u0001\u0000\u0000\u0000\u0091\u0093\u0003\u000e\u0007\u0000"+
		"\u0092\u0091\u0001\u0000\u0000\u0000\u0092\u0093\u0001\u0000\u0000\u0000"+
		"\u0093\u0095\u0001\u0000\u0000\u0000\u0094\u0096\u00052\u0000\u0000\u0095"+
		"\u0094\u0001\u0000\u0000\u0000\u0095\u0096\u0001\u0000\u0000\u0000\u0096"+
		"\u0097\u0001\u0000\u0000\u0000\u0097\u0098\u0003\u0014\n\u0000\u0098\u009b"+
		"\u0005B\u0000\u0000\u0099\u009a\u0005\u0005\u0000\u0000\u009a\u009c\u0003"+
		"8\u001c\u0000\u009b\u0099\u0001\u0000\u0000\u0000\u009b\u009c\u0001\u0000"+
		"\u0000\u0000\u009c\u009d\u0001\u0000\u0000\u0000\u009d\u009e\u0005C\u0000"+
		"\u0000\u009e\u000b\u0001\u0000\u0000\u0000\u009f\u00a1\u0003\u000e\u0007"+
		"\u0000\u00a0\u009f\u0001\u0000\u0000\u0000\u00a0\u00a1\u0001\u0000\u0000"+
		"\u0000\u00a1\u00a2\u0001\u0000\u0000\u0000\u00a2\u00a3\u0003N\'\u0000"+
		"\u00a3\u00a5\u0005\u0003\u0000\u0000\u00a4\u00a6\u0003\u0016\u000b\u0000"+
		"\u00a5\u00a4\u0001\u0000\u0000\u0000\u00a5\u00a6\u0001\u0000\u0000\u0000"+
		"\u00a6\u00a7\u0001\u0000\u0000\u0000\u00a7\u00a8\u0005\u0004\u0000\u0000"+
		"\u00a8\u00a9\u0003\u001a\r\u0000\u00a9\r\u0001\u0000\u0000\u0000\u00aa"+
		"\u00ab\u0007\u0000\u0000\u0000\u00ab\u000f\u0001\u0000\u0000\u0000\u00ac"+
		"\u00b0\u0005+\u0000\u0000\u00ad\u00ae\u00053\u0000\u0000\u00ae\u00b0\u0005"+
		"+\u0000\u0000\u00af\u00ac\u0001\u0000\u0000\u0000\u00af\u00ad\u0001\u0000"+
		"\u0000\u0000\u00b0\u0011\u0001\u0000\u0000\u0000\u00b1\u00b4\u00054\u0000"+
		"\u0000\u00b2\u00b4\u0003\u0014\n\u0000\u00b3\u00b1\u0001\u0000\u0000\u0000"+
		"\u00b3\u00b2\u0001\u0000\u0000\u0000\u00b4\u0013\u0001\u0000\u0000\u0000"+
		"\u00b5\u00b6\u0006\n\uffff\uffff\u0000\u00b6\u00b9\u00057\u0000\u0000"+
		"\u00b7\u00b9\u0003N\'\u0000\u00b8\u00b5\u0001\u0000\u0000\u0000\u00b8"+
		"\u00b7\u0001\u0000\u0000\u0000\u00b9\u00be\u0001\u0000\u0000\u0000\u00ba"+
		"\u00bb\n\u0001\u0000\u0000\u00bb\u00bd\u0005\u0006\u0000\u0000\u00bc\u00ba"+
		"\u0001\u0000\u0000\u0000\u00bd\u00c0\u0001\u0000\u0000\u0000\u00be\u00bc"+
		"\u0001\u0000\u0000\u0000\u00be\u00bf\u0001\u0000\u0000\u0000\u00bf\u0015"+
		"\u0001\u0000\u0000\u0000\u00c0\u00be\u0001\u0000\u0000\u0000\u00c1\u00c6"+
		"\u0003\u0018\f\u0000\u00c2\u00c3\u0005\u0007\u0000\u0000\u00c3\u00c5\u0003"+
		"\u0018\f\u0000\u00c4\u00c2\u0001\u0000\u0000\u0000\u00c5\u00c8\u0001\u0000"+
		"\u0000\u0000\u00c6\u00c4\u0001\u0000\u0000\u0000\u00c6\u00c7\u0001\u0000"+
		"\u0000\u0000\u00c7\u0017\u0001\u0000\u0000\u0000\u00c8\u00c6\u0001\u0000"+
		"\u0000\u0000\u00c9\u00ca\u0003\u0014\n\u0000\u00ca\u00cb\u0005B\u0000"+
		"\u0000\u00cb\u0019\u0001\u0000\u0000\u0000\u00cc\u00d0\u0005\u0001\u0000"+
		"\u0000\u00cd\u00cf\u0003\u001c\u000e\u0000\u00ce\u00cd\u0001\u0000\u0000"+
		"\u0000\u00cf\u00d2\u0001\u0000\u0000\u0000\u00d0\u00ce\u0001\u0000\u0000"+
		"\u0000\u00d0\u00d1\u0001\u0000\u0000\u0000\u00d1\u00d3\u0001\u0000\u0000"+
		"\u0000\u00d2\u00d0\u0001\u0000\u0000\u0000\u00d3\u00d4\u0005\u0002\u0000"+
		"\u0000\u00d4\u001b\u0001\u0000\u0000\u0000\u00d5\u00e0\u0003\u001e\u000f"+
		"\u0000\u00d6\u00e0\u0003 \u0010\u0000\u00d7\u00e0\u0003\"\u0011\u0000"+
		"\u00d8\u00e0\u0003$\u0012\u0000\u00d9\u00e0\u0003*\u0015\u0000\u00da\u00e0"+
		"\u0003.\u0017\u0000\u00db\u00e0\u0003,\u0016\u0000\u00dc\u00e0\u00030"+
		"\u0018\u0000\u00dd\u00e0\u00034\u001a\u0000\u00de\u00e0\u00036\u001b\u0000"+
		"\u00df\u00d5\u0001\u0000\u0000\u0000\u00df\u00d6\u0001\u0000\u0000\u0000"+
		"\u00df\u00d7\u0001\u0000\u0000\u0000\u00df\u00d8\u0001\u0000\u0000\u0000"+
		"\u00df\u00d9\u0001\u0000\u0000\u0000\u00df\u00da\u0001\u0000\u0000\u0000"+
		"\u00df\u00db\u0001\u0000\u0000\u0000\u00df\u00dc\u0001\u0000\u0000\u0000"+
		"\u00df\u00dd\u0001\u0000\u0000\u0000\u00df\u00de\u0001\u0000\u0000\u0000"+
		"\u00e0\u001d\u0001\u0000\u0000\u0000\u00e1\u00e2\u0003\u0014\n\u0000\u00e2"+
		"\u00e5\u0005B\u0000\u0000\u00e3\u00e4\u0005\u0005\u0000\u0000\u00e4\u00e6"+
		"\u00038\u001c\u0000\u00e5\u00e3\u0001\u0000\u0000\u0000\u00e5\u00e6\u0001"+
		"\u0000\u0000\u0000\u00e6\u00e7\u0001\u0000\u0000\u0000\u00e7\u00e8\u0005"+
		"C\u0000\u0000\u00e8\u001f\u0001\u0000\u0000\u0000\u00e9\u00ea\u00038\u001c"+
		"\u0000\u00ea\u00eb\u0005C\u0000\u0000\u00eb!\u0001\u0000\u0000\u0000\u00ec"+
		"\u00ee\u00055\u0000\u0000\u00ed\u00ef\u00038\u001c\u0000\u00ee\u00ed\u0001"+
		"\u0000\u0000\u0000\u00ee\u00ef\u0001\u0000\u0000\u0000\u00ef\u00f0\u0001"+
		"\u0000\u0000\u0000\u00f0\u00f1\u0005C\u0000\u0000\u00f1#\u0001\u0000\u0000"+
		"\u0000\u00f2\u00f3\u0005\b\u0000\u0000\u00f3\u00f4\u0005\u0003\u0000\u0000"+
		"\u00f4\u00f5\u00038\u001c\u0000\u00f5\u00f6\u0005\u0004\u0000\u0000\u00f6"+
		"\u00fa\u0003\u001a\r\u0000\u00f7\u00f9\u0003&\u0013\u0000\u00f8\u00f7"+
		"\u0001\u0000\u0000\u0000\u00f9\u00fc\u0001\u0000\u0000\u0000\u00fa\u00f8"+
		"\u0001\u0000\u0000\u0000\u00fa\u00fb\u0001\u0000\u0000\u0000\u00fb\u00fe"+
		"\u0001\u0000\u0000\u0000\u00fc\u00fa\u0001\u0000\u0000\u0000\u00fd\u00ff"+
		"\u0003(\u0014\u0000\u00fe\u00fd\u0001\u0000\u0000\u0000\u00fe\u00ff\u0001"+
		"\u0000\u0000\u0000\u00ff%\u0001\u0000\u0000\u0000\u0100\u0101\u0005\t"+
		"\u0000\u0000\u0101\u0102\u0005\u0003\u0000\u0000\u0102\u0103\u00038\u001c"+
		"\u0000\u0103\u0104\u0005\u0004\u0000\u0000\u0104\u0105\u0003\u001a\r\u0000"+
		"\u0105\'\u0001\u0000\u0000\u0000\u0106\u0107\u0005\n\u0000\u0000\u0107"+
		"\u0108\u0003\u001a\r\u0000\u0108)\u0001\u0000\u0000\u0000\u0109\u010a"+
		"\u0005\u000b\u0000\u0000\u010a\u010b\u0005\u0003\u0000\u0000\u010b\u010c"+
		"\u00038\u001c\u0000\u010c\u010d\u0005\u0004\u0000\u0000\u010d\u010e\u0003"+
		"\u001a\r\u0000\u010e+\u0001\u0000\u0000\u0000\u010f\u0110\u0005\f\u0000"+
		"\u0000\u0110\u0111\u0003\u001a\r\u0000\u0111\u0112\u0005\u000b\u0000\u0000"+
		"\u0112\u0113\u0005\u0003\u0000\u0000\u0113\u0114\u00038\u001c\u0000\u0114"+
		"\u0115\u0005\u0004\u0000\u0000\u0115\u0116\u0005C\u0000\u0000\u0116-\u0001"+
		"\u0000\u0000\u0000\u0117\u0118\u0005\r\u0000\u0000\u0118\u011c\u0005\u0003"+
		"\u0000\u0000\u0119\u011d\u0003\u001e\u000f\u0000\u011a\u011d\u0003 \u0010"+
		"\u0000\u011b\u011d\u0005C\u0000\u0000\u011c\u0119\u0001\u0000\u0000\u0000"+
		"\u011c\u011a\u0001\u0000\u0000\u0000\u011c\u011b\u0001\u0000\u0000\u0000"+
		"\u011d\u011f\u0001\u0000\u0000\u0000\u011e\u0120\u00038\u001c\u0000\u011f"+
		"\u011e\u0001\u0000\u0000\u0000\u011f\u0120\u0001\u0000\u0000\u0000\u0120"+
		"\u0121\u0001\u0000\u0000\u0000\u0121\u0123\u0005C\u0000\u0000\u0122\u0124"+
		"\u00038\u001c\u0000\u0123\u0122\u0001\u0000\u0000\u0000\u0123\u0124\u0001"+
		"\u0000\u0000\u0000\u0124\u0125\u0001\u0000\u0000\u0000\u0125\u0126\u0005"+
		"\u0004\u0000\u0000\u0126\u0127\u0003\u001a\r\u0000\u0127/\u0001\u0000"+
		"\u0000\u0000\u0128\u0129\u0005\u000e\u0000\u0000\u0129\u012a\u0005\u0003"+
		"\u0000\u0000\u012a\u012b\u00038\u001c\u0000\u012b\u012c\u0005\u0004\u0000"+
		"\u0000\u012c\u0130\u0005\u0001\u0000\u0000\u012d\u012f\u00032\u0019\u0000"+
		"\u012e\u012d\u0001\u0000\u0000\u0000\u012f\u0132\u0001\u0000\u0000\u0000"+
		"\u0130\u012e\u0001\u0000\u0000\u0000\u0130\u0131\u0001\u0000\u0000\u0000"+
		"\u0131\u0133\u0001\u0000\u0000\u0000\u0132\u0130\u0001\u0000\u0000\u0000"+
		"\u0133\u0134\u0005\u0002\u0000\u0000\u01341\u0001\u0000\u0000\u0000\u0135"+
		"\u0136\u0005\u000f\u0000\u0000\u0136\u0137\u0003L&\u0000\u0137\u0138\u0005"+
		"\u0010\u0000\u0000\u0138\u0139\u0003\u001a\r\u0000\u0139\u013e\u0001\u0000"+
		"\u0000\u0000\u013a\u013b\u0005\u0011\u0000\u0000\u013b\u013c\u0005\u0010"+
		"\u0000\u0000\u013c\u013e\u0003\u001a\r\u0000\u013d\u0135\u0001\u0000\u0000"+
		"\u0000\u013d\u013a\u0001\u0000\u0000\u0000\u013e3\u0001\u0000\u0000\u0000"+
		"\u013f\u0140\u0005\u0012\u0000\u0000\u0140\u0141\u0005C\u0000\u0000\u0141"+
		"5\u0001\u0000\u0000\u0000\u0142\u0143\u0005\u0013\u0000\u0000\u0143\u0144"+
		"\u0005C\u0000\u0000\u01447\u0001\u0000\u0000\u0000\u0145\u0146\u0006\u001c"+
		"\uffff\uffff\u0000\u0146\u0152\u0003L&\u0000\u0147\u0152\u0003@ \u0000"+
		"\u0148\u0152\u0003B!\u0000\u0149\u0152\u0003D\"\u0000\u014a\u0152\u0003"+
		">\u001f\u0000\u014b\u0152\u0003:\u001d\u0000\u014c\u0152\u0003<\u001e"+
		"\u0000\u014d\u014e\u0005\u0003\u0000\u0000\u014e\u014f\u00038\u001c\u0000"+
		"\u014f\u0150\u0005\u0004\u0000\u0000\u0150\u0152\u0001\u0000\u0000\u0000"+
		"\u0151\u0145\u0001\u0000\u0000\u0000\u0151\u0147\u0001\u0000\u0000\u0000"+
		"\u0151\u0148\u0001\u0000\u0000\u0000\u0151\u0149\u0001\u0000\u0000\u0000"+
		"\u0151\u014a\u0001\u0000\u0000\u0000\u0151\u014b\u0001\u0000\u0000\u0000"+
		"\u0151\u014c\u0001\u0000\u0000\u0000\u0151\u014d\u0001\u0000\u0000\u0000"+
		"\u0152\u0159\u0001\u0000\u0000\u0000\u0153\u0154\n\u0001\u0000\u0000\u0154"+
		"\u0155\u0003J%\u0000\u0155\u0156\u00038\u001c\u0002\u0156\u0158\u0001"+
		"\u0000\u0000\u0000\u0157\u0153\u0001\u0000\u0000\u0000\u0158\u015b\u0001"+
		"\u0000\u0000\u0000\u0159\u0157\u0001\u0000\u0000\u0000\u0159\u015a\u0001"+
		"\u0000\u0000\u0000\u015a9\u0001\u0000\u0000\u0000\u015b\u0159\u0001\u0000"+
		"\u0000\u0000\u015c\u015d\u0005\u0014\u0000\u0000\u015d\u015e\u0003N\'"+
		"\u0000\u015e\u0160\u0005\u0003\u0000\u0000\u015f\u0161\u0003H$\u0000\u0160"+
		"\u015f\u0001\u0000\u0000\u0000\u0160\u0161\u0001\u0000\u0000\u0000\u0161"+
		"\u0162\u0001\u0000\u0000\u0000\u0162\u0163\u0005\u0004\u0000\u0000\u0163"+
		";\u0001\u0000\u0000\u0000\u0164\u0165\u0005\u0014\u0000\u0000\u0165\u016a"+
		"\u0003\u0014\n\u0000\u0166\u0167\u0005\u0015\u0000\u0000\u0167\u0168\u0003"+
		"8\u001c\u0000\u0168\u0169\u0005\u0016\u0000\u0000\u0169\u016b\u0001\u0000"+
		"\u0000\u0000\u016a\u0166\u0001\u0000\u0000\u0000\u016b\u016c\u0001\u0000"+
		"\u0000\u0000\u016c\u016a\u0001\u0000\u0000\u0000\u016c\u016d\u0001\u0000"+
		"\u0000\u0000\u016d=\u0001\u0000\u0000\u0000\u016e\u016f\u0003@ \u0000"+
		"\u016f\u0170\u0005\u0015\u0000\u0000\u0170\u0171\u00038\u001c\u0000\u0171"+
		"\u0172\u0005\u0016\u0000\u0000\u0172?\u0001\u0000\u0000\u0000\u0173\u017d"+
		"\u0005B\u0000\u0000\u0174\u017d\u0003D\"\u0000\u0175\u017d\u0003F#\u0000"+
		"\u0176\u0177\u0005\u0003\u0000\u0000\u0177\u0178\u00038\u001c\u0000\u0178"+
		"\u0179\u0005\u0004\u0000\u0000\u0179\u017d\u0001\u0000\u0000\u0000\u017a"+
		"\u017d\u0003:\u001d\u0000\u017b\u017d\u0003<\u001e\u0000\u017c\u0173\u0001"+
		"\u0000\u0000\u0000\u017c\u0174\u0001\u0000\u0000\u0000\u017c\u0175\u0001"+
		"\u0000\u0000\u0000\u017c\u0176\u0001\u0000\u0000\u0000\u017c\u017a\u0001"+
		"\u0000\u0000\u0000\u017c\u017b\u0001\u0000\u0000\u0000\u017dA\u0001\u0000"+
		"\u0000\u0000\u017e\u0188\u0003@ \u0000\u017f\u0180\u0005\u0017\u0000\u0000"+
		"\u0180\u0181\u0005B\u0000\u0000\u0181\u0183\u0005\u0003\u0000\u0000\u0182"+
		"\u0184\u0003H$\u0000\u0183\u0182\u0001\u0000\u0000\u0000\u0183\u0184\u0001"+
		"\u0000\u0000\u0000\u0184\u0185\u0001\u0000\u0000\u0000\u0185\u0187\u0005"+
		"\u0004\u0000\u0000\u0186\u017f\u0001\u0000\u0000\u0000\u0187\u018a\u0001"+
		"\u0000\u0000\u0000\u0188\u0186\u0001\u0000\u0000\u0000\u0188\u0189\u0001"+
		"\u0000\u0000\u0000\u0189C\u0001\u0000\u0000\u0000\u018a\u0188\u0001\u0000"+
		"\u0000\u0000\u018b\u018c\u0005\u0018\u0000\u0000\u018c\u018d\u0005\u0017"+
		"\u0000\u0000\u018d\u018e\u0005B\u0000\u0000\u018eE\u0001\u0000\u0000\u0000"+
		"\u018f\u0190\u0005B\u0000\u0000\u0190\u0191\u0005\u0017\u0000\u0000\u0191"+
		"\u0192\u0005B\u0000\u0000\u0192G\u0001\u0000\u0000\u0000\u0193\u0198\u0003"+
		"8\u001c\u0000\u0194\u0195\u0005\u0007\u0000\u0000\u0195\u0197\u00038\u001c"+
		"\u0000\u0196\u0194\u0001\u0000\u0000\u0000\u0197\u019a\u0001\u0000\u0000"+
		"\u0000\u0198\u0196\u0001\u0000\u0000\u0000\u0198\u0199\u0001\u0000\u0000"+
		"\u0000\u0199I\u0001\u0000\u0000\u0000\u019a\u0198\u0001\u0000\u0000\u0000"+
		"\u019b\u019c\u0007\u0001\u0000\u0000\u019cK\u0001\u0000\u0000\u0000\u019d"+
		"\u019e\u0007\u0002\u0000\u0000\u019eM\u0001\u0000\u0000\u0000\u019f\u01a0"+
		"\u0005B\u0000\u0000\u01a0O\u0001\u0000\u0000\u0000\u01a1\u01a6\u0005B"+
		"\u0000\u0000\u01a2\u01a3\u0005\u0017\u0000\u0000\u01a3\u01a5\u0005B\u0000"+
		"\u0000\u01a4\u01a2\u0001\u0000\u0000\u0000\u01a5\u01a8\u0001\u0000\u0000"+
		"\u0000\u01a6\u01a4\u0001\u0000\u0000\u0000\u01a6\u01a7\u0001\u0000\u0000"+
		"\u0000\u01a7Q\u0001\u0000\u0000\u0000\u01a8\u01a6\u0001\u0000\u0000\u0000"+
		"*Yaektv|\u007f\u0082\u0085\u008b\u008f\u0092\u0095\u009b\u00a0\u00a5\u00af"+
		"\u00b3\u00b8\u00be\u00c6\u00d0\u00df\u00e5\u00ee\u00fa\u00fe\u011c\u011f"+
		"\u0123\u0130\u013d\u0151\u0159\u0160\u016c\u017c\u0183\u0188\u0198\u01a6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}