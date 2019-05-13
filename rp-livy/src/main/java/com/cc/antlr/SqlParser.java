// Generated from F:/code/core/spark-core/spark-shell/src/main/resources\Sql.g4 by ANTLR 4.7.2
package com.cc.antlr;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SqlParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, LOAD=22, SAVE=23, SELECT=24, EXPLAIN=25, 
		SHOW=26, INSERT=27, CREATE=28, DROP=29, STRING=30, LETTER=31, DIGIT=32, 
		QUOTATION_STRING=33, WS=34, SIMPLE_COMMENT=35, UNRECOGNIZED=36, IDENTIFIER=37;
	public static final int
		RULE_statement = 0, RULE_code = 1, RULE_scala = 2, RULE_numPartition = 3, 
		RULE_overwrite = 4, RULE_append = 5, RULE_errorIfExists = 6, RULE_ignore = 7, 
		RULE_update = 8, RULE_booleanExpression = 9, RULE_format = 10, RULE_path = 11, 
		RULE_expression = 12, RULE_tableName = 13, RULE_expr = 14, RULE_ender = 15, 
		RULE_strictIdentifier = 16, RULE_seperate = 17, RULE_col = 18;
	private static String[] makeRuleNames() {
		return new String[] {
			"statement", "code", "scala", "numPartition", "overwrite", "append", 
			"errorIfExists", "ignore", "update", "booleanExpression", "format", "path", 
			"expression", "tableName", "expr", "ender", "strictIdentifier", "seperate", 
			"col"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'where'", "'WHERE'", "'as'", "'partitionBy'", "'coalesce'", 
			"';'", "' '", "'='", "'overwrite'", "'OVERWRITE'", "'append'", "'APPEND'", 
			"'errorIfExists'", "'ignore'", "'IGNORE'", "'update'", "'UPDATE'", "'and'", 
			"'AND'", "'_'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, "LOAD", "SAVE", 
			"SELECT", "EXPLAIN", "SHOW", "INSERT", "CREATE", "DROP", "STRING", "LETTER", 
			"DIGIT", "QUOTATION_STRING", "WS", "SIMPLE_COMMENT", "UNRECOGNIZED", 
			"IDENTIFIER"
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
	public String getGrammarFileName() { return "Sql.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SqlParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class StatementContext extends ParserRuleContext {
		public List<CodeContext> code() {
			return getRuleContexts(CodeContext.class);
		}
		public CodeContext code(int i) {
			return getRuleContext(CodeContext.class,i);
		}
		public List<EnderContext> ender() {
			return getRuleContexts(EnderContext.class);
		}
		public EnderContext ender(int i) {
			return getRuleContext(EnderContext.class,i);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__7) | (1L << T__8) | (1L << LOAD) | (1L << SAVE) | (1L << SELECT) | (1L << EXPLAIN) | (1L << SHOW) | (1L << INSERT) | (1L << CREATE) | (1L << DROP) | (1L << LETTER) | (1L << DIGIT) | (1L << SIMPLE_COMMENT))) != 0)) {
				{
				{
				setState(38);
				code();
				setState(39);
				ender();
				}
				}
				setState(45);
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

	public static class CodeContext extends ParserRuleContext {
		public CodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_code; }
	 
		public CodeContext() { }
		public void copyFrom(CodeContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class OtherContext extends CodeContext {
		public ScalaContext scala() {
			return getRuleContext(ScalaContext.class,0);
		}
		public OtherContext(CodeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterOther(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitOther(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitOther(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CommentContext extends CodeContext {
		public TerminalNode SIMPLE_COMMENT() { return getToken(SqlParser.SIMPLE_COMMENT, 0); }
		public CommentContext(CodeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitComment(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SqlContext extends CodeContext {
		public Token option;
		public FormatContext format() {
			return getRuleContext(FormatContext.class,0);
		}
		public PathContext path() {
			return getRuleContext(PathContext.class,0);
		}
		public TableNameContext tableName() {
			return getRuleContext(TableNameContext.class,0);
		}
		public TerminalNode LOAD() { return getToken(SqlParser.LOAD, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public List<BooleanExpressionContext> booleanExpression() {
			return getRuleContexts(BooleanExpressionContext.class);
		}
		public BooleanExpressionContext booleanExpression(int i) {
			return getRuleContext(BooleanExpressionContext.class,i);
		}
		public TerminalNode SAVE() { return getToken(SqlParser.SAVE, 0); }
		public OverwriteContext overwrite() {
			return getRuleContext(OverwriteContext.class,0);
		}
		public AppendContext append() {
			return getRuleContext(AppendContext.class,0);
		}
		public ErrorIfExistsContext errorIfExists() {
			return getRuleContext(ErrorIfExistsContext.class,0);
		}
		public IgnoreContext ignore() {
			return getRuleContext(IgnoreContext.class,0);
		}
		public UpdateContext update() {
			return getRuleContext(UpdateContext.class,0);
		}
		public ColContext col() {
			return getRuleContext(ColContext.class,0);
		}
		public NumPartitionContext numPartition() {
			return getRuleContext(NumPartitionContext.class,0);
		}
		public TerminalNode SELECT() { return getToken(SqlParser.SELECT, 0); }
		public TerminalNode INSERT() { return getToken(SqlParser.INSERT, 0); }
		public TerminalNode CREATE() { return getToken(SqlParser.CREATE, 0); }
		public TerminalNode DROP() { return getToken(SqlParser.DROP, 0); }
		public TerminalNode SHOW() { return getToken(SqlParser.SHOW, 0); }
		public TerminalNode EXPLAIN() { return getToken(SqlParser.EXPLAIN, 0); }
		public SqlContext(CodeContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterSql(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitSql(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitSql(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CodeContext code() throws RecognitionException {
		CodeContext _localctx = new CodeContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_code);
		int _la;
		try {
			int _alt;
			setState(152);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LOAD:
				_localctx = new SqlContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(46);
				((SqlContext)_localctx).option = match(LOAD);
				setState(47);
				format();
				setState(49);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__0) {
					{
					setState(48);
					match(T__0);
					}
				}

				setState(51);
				path();
				setState(60);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1 || _la==T__2) {
					{
					setState(52);
					_la = _input.LA(1);
					if ( !(_la==T__1 || _la==T__2) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(53);
					expression();
					setState(57);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==T__18 || _la==T__19) {
						{
						{
						setState(54);
						booleanExpression();
						}
						}
						setState(59);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				setState(62);
				match(T__3);
				setState(63);
				tableName();
				}
				break;
			case SAVE:
				_localctx = new SqlContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(65);
				((SqlContext)_localctx).option = match(SAVE);
				setState(71);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case T__9:
				case T__10:
					{
					setState(66);
					overwrite();
					}
					break;
				case T__11:
				case T__12:
					{
					setState(67);
					append();
					}
					break;
				case T__13:
					{
					setState(68);
					errorIfExists();
					}
					break;
				case T__14:
				case T__15:
					{
					setState(69);
					ignore();
					}
					break;
				case T__16:
				case T__17:
					{
					setState(70);
					update();
					}
					break;
				case T__20:
				case LETTER:
				case DIGIT:
					break;
				default:
					break;
				}
				setState(73);
				tableName();
				setState(74);
				match(T__3);
				setState(75);
				format();
				setState(76);
				match(T__0);
				setState(77);
				path();
				setState(79);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1 || _la==T__2) {
					{
					setState(78);
					_la = _input.LA(1);
					if ( !(_la==T__1 || _la==T__2) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
				}

				setState(82);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__20) | (1L << LETTER) | (1L << DIGIT))) != 0)) {
					{
					setState(81);
					expression();
					}
				}

				setState(87);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__18 || _la==T__19) {
					{
					{
					setState(84);
					booleanExpression();
					}
					}
					setState(89);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(92);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__4) {
					{
					setState(90);
					match(T__4);
					setState(91);
					col();
					}
				}

				setState(96);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__5) {
					{
					setState(94);
					match(T__5);
					setState(95);
					numPartition();
					}
				}

				}
				break;
			case SELECT:
				_localctx = new SqlContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(98);
				((SqlContext)_localctx).option = match(SELECT);
				setState(102);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(99);
						_la = _input.LA(1);
						if ( _la <= 0 || (_la==T__6) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						} 
					}
					setState(104);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				}
				setState(106);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(105);
					match(T__3);
					}
				}

				setState(108);
				tableName();
				}
				break;
			case INSERT:
				_localctx = new SqlContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(109);
				((SqlContext)_localctx).option = match(INSERT);
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << LOAD) | (1L << SAVE) | (1L << SELECT) | (1L << EXPLAIN) | (1L << SHOW) | (1L << INSERT) | (1L << CREATE) | (1L << DROP) | (1L << STRING) | (1L << LETTER) | (1L << DIGIT) | (1L << QUOTATION_STRING) | (1L << WS) | (1L << SIMPLE_COMMENT) | (1L << UNRECOGNIZED) | (1L << IDENTIFIER))) != 0)) {
					{
					{
					setState(110);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__6) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(115);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case CREATE:
				_localctx = new SqlContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(116);
				((SqlContext)_localctx).option = match(CREATE);
				setState(120);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << LOAD) | (1L << SAVE) | (1L << SELECT) | (1L << EXPLAIN) | (1L << SHOW) | (1L << INSERT) | (1L << CREATE) | (1L << DROP) | (1L << STRING) | (1L << LETTER) | (1L << DIGIT) | (1L << QUOTATION_STRING) | (1L << WS) | (1L << SIMPLE_COMMENT) | (1L << UNRECOGNIZED) | (1L << IDENTIFIER))) != 0)) {
					{
					{
					setState(117);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__6) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(122);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case DROP:
				_localctx = new SqlContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(123);
				((SqlContext)_localctx).option = match(DROP);
				setState(127);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << LOAD) | (1L << SAVE) | (1L << SELECT) | (1L << EXPLAIN) | (1L << SHOW) | (1L << INSERT) | (1L << CREATE) | (1L << DROP) | (1L << STRING) | (1L << LETTER) | (1L << DIGIT) | (1L << QUOTATION_STRING) | (1L << WS) | (1L << SIMPLE_COMMENT) | (1L << UNRECOGNIZED) | (1L << IDENTIFIER))) != 0)) {
					{
					{
					setState(124);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__6) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(129);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case SHOW:
				_localctx = new SqlContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(130);
				((SqlContext)_localctx).option = match(SHOW);
				setState(134);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << LOAD) | (1L << SAVE) | (1L << SELECT) | (1L << EXPLAIN) | (1L << SHOW) | (1L << INSERT) | (1L << CREATE) | (1L << DROP) | (1L << STRING) | (1L << LETTER) | (1L << DIGIT) | (1L << QUOTATION_STRING) | (1L << WS) | (1L << SIMPLE_COMMENT) | (1L << UNRECOGNIZED) | (1L << IDENTIFIER))) != 0)) {
					{
					{
					setState(131);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__6) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(136);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case EXPLAIN:
				_localctx = new SqlContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(137);
				((SqlContext)_localctx).option = match(EXPLAIN);
				setState(141);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << LOAD) | (1L << SAVE) | (1L << SELECT) | (1L << EXPLAIN) | (1L << SHOW) | (1L << INSERT) | (1L << CREATE) | (1L << DROP) | (1L << STRING) | (1L << LETTER) | (1L << DIGIT) | (1L << QUOTATION_STRING) | (1L << WS) | (1L << SIMPLE_COMMENT) | (1L << UNRECOGNIZED) | (1L << IDENTIFIER))) != 0)) {
					{
					{
					setState(138);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__6) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(143);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			case SIMPLE_COMMENT:
				_localctx = new CommentContext(_localctx);
				enterOuterAlt(_localctx, 9);
				{
				setState(144);
				match(SIMPLE_COMMENT);
				}
				break;
			case T__0:
			case T__7:
			case T__8:
			case LETTER:
			case DIGIT:
				_localctx = new OtherContext(_localctx);
				enterOuterAlt(_localctx, 10);
				{
				setState(145);
				scala();
				setState(149);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__1) | (1L << T__2) | (1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10) | (1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << T__14) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18) | (1L << T__19) | (1L << T__20) | (1L << LOAD) | (1L << SAVE) | (1L << SELECT) | (1L << EXPLAIN) | (1L << SHOW) | (1L << INSERT) | (1L << CREATE) | (1L << DROP) | (1L << STRING) | (1L << LETTER) | (1L << DIGIT) | (1L << QUOTATION_STRING) | (1L << WS) | (1L << SIMPLE_COMMENT) | (1L << UNRECOGNIZED) | (1L << IDENTIFIER))) != 0)) {
					{
					{
					setState(146);
					_la = _input.LA(1);
					if ( _la <= 0 || (_la==T__6) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(151);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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

	public static class ScalaContext extends ParserRuleContext {
		public List<TerminalNode> LETTER() { return getTokens(SqlParser.LETTER); }
		public TerminalNode LETTER(int i) {
			return getToken(SqlParser.LETTER, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(SqlParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(SqlParser.DIGIT, i);
		}
		public ScalaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scala; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterScala(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitScala(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitScala(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScalaContext scala() throws RecognitionException {
		ScalaContext _localctx = new ScalaContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_scala);
		int _la;
		try {
			setState(168);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(157);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << LETTER) | (1L << DIGIT))) != 0)) {
					{
					{
					setState(154);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << LETTER) | (1L << DIGIT))) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					}
					}
					setState(159);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(160);
				match(T__8);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(164);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==LETTER) {
					{
					{
					setState(161);
					match(LETTER);
					}
					}
					setState(166);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(167);
				match(T__0);
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

	public static class NumPartitionContext extends ParserRuleContext {
		public List<TerminalNode> DIGIT() { return getTokens(SqlParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(SqlParser.DIGIT, i);
		}
		public NumPartitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numPartition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterNumPartition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitNumPartition(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitNumPartition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumPartitionContext numPartition() throws RecognitionException {
		NumPartitionContext _localctx = new NumPartitionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_numPartition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(170);
				match(DIGIT);
				}
				}
				setState(173); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==DIGIT );
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

	public static class OverwriteContext extends ParserRuleContext {
		public OverwriteContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_overwrite; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterOverwrite(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitOverwrite(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitOverwrite(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OverwriteContext overwrite() throws RecognitionException {
		OverwriteContext _localctx = new OverwriteContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_overwrite);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(175);
			_la = _input.LA(1);
			if ( !(_la==T__9 || _la==T__10) ) {
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

	public static class AppendContext extends ParserRuleContext {
		public AppendContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_append; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterAppend(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitAppend(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitAppend(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AppendContext append() throws RecognitionException {
		AppendContext _localctx = new AppendContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_append);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			_la = _input.LA(1);
			if ( !(_la==T__11 || _la==T__12) ) {
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

	public static class ErrorIfExistsContext extends ParserRuleContext {
		public ErrorIfExistsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_errorIfExists; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterErrorIfExists(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitErrorIfExists(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitErrorIfExists(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ErrorIfExistsContext errorIfExists() throws RecognitionException {
		ErrorIfExistsContext _localctx = new ErrorIfExistsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_errorIfExists);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			match(T__13);
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

	public static class IgnoreContext extends ParserRuleContext {
		public IgnoreContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ignore; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterIgnore(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitIgnore(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitIgnore(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IgnoreContext ignore() throws RecognitionException {
		IgnoreContext _localctx = new IgnoreContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_ignore);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			_la = _input.LA(1);
			if ( !(_la==T__14 || _la==T__15) ) {
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

	public static class UpdateContext extends ParserRuleContext {
		public UpdateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_update; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterUpdate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitUpdate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitUpdate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UpdateContext update() throws RecognitionException {
		UpdateContext _localctx = new UpdateContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_update);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			_la = _input.LA(1);
			if ( !(_la==T__16 || _la==T__17) ) {
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

	public static class BooleanExpressionContext extends ParserRuleContext {
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public BooleanExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booleanExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterBooleanExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitBooleanExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitBooleanExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BooleanExpressionContext booleanExpression() throws RecognitionException {
		BooleanExpressionContext _localctx = new BooleanExpressionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_booleanExpression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(185);
			_la = _input.LA(1);
			if ( !(_la==T__18 || _la==T__19) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(186);
			expression();
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

	public static class FormatContext extends ParserRuleContext {
		public List<TerminalNode> LETTER() { return getTokens(SqlParser.LETTER); }
		public TerminalNode LETTER(int i) {
			return getToken(SqlParser.LETTER, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(SqlParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(SqlParser.DIGIT, i);
		}
		public FormatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_format; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterFormat(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitFormat(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitFormat(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FormatContext format() throws RecognitionException {
		FormatContext _localctx = new FormatContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_format);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(189); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(188);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__20) | (1L << LETTER) | (1L << DIGIT))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(191); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__20) | (1L << LETTER) | (1L << DIGIT))) != 0) );
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

	public static class PathContext extends ParserRuleContext {
		public TerminalNode QUOTATION_STRING() { return getToken(SqlParser.QUOTATION_STRING, 0); }
		public PathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_path; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitPath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathContext path() throws RecognitionException {
		PathContext _localctx = new PathContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_path);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			match(QUOTATION_STRING);
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

	public static class ExpressionContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode STRING() { return getToken(SqlParser.STRING, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			expr();
			setState(196);
			match(T__8);
			setState(197);
			match(STRING);
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

	public static class TableNameContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TableNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tableName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterTableName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitTableName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitTableName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TableNameContext tableName() throws RecognitionException {
		TableNameContext _localctx = new TableNameContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_tableName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			expr();
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

	public static class ExprContext extends ParserRuleContext {
		public List<TerminalNode> LETTER() { return getTokens(SqlParser.LETTER); }
		public TerminalNode LETTER(int i) {
			return getToken(SqlParser.LETTER, i);
		}
		public List<TerminalNode> DIGIT() { return getTokens(SqlParser.DIGIT); }
		public TerminalNode DIGIT(int i) {
			return getToken(SqlParser.DIGIT, i);
		}
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		ExprContext _localctx = new ExprContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_expr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(202); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(201);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__20) | (1L << LETTER) | (1L << DIGIT))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
				}
				setState(204); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__20) | (1L << LETTER) | (1L << DIGIT))) != 0) );
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

	public static class EnderContext extends ParserRuleContext {
		public EnderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ender; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterEnder(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitEnder(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitEnder(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnderContext ender() throws RecognitionException {
		EnderContext _localctx = new EnderContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_ender);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			match(T__6);
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

	public static class StrictIdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(SqlParser.IDENTIFIER, 0); }
		public TerminalNode QUOTATION_STRING() { return getToken(SqlParser.QUOTATION_STRING, 0); }
		public StrictIdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_strictIdentifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterStrictIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitStrictIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitStrictIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StrictIdentifierContext strictIdentifier() throws RecognitionException {
		StrictIdentifierContext _localctx = new StrictIdentifierContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_strictIdentifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(208);
			_la = _input.LA(1);
			if ( !(_la==QUOTATION_STRING || _la==IDENTIFIER) ) {
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

	public static class SeperateContext extends ParserRuleContext {
		public SeperateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_seperate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterSeperate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitSeperate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitSeperate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SeperateContext seperate() throws RecognitionException {
		SeperateContext _localctx = new SeperateContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_seperate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			match(T__7);
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

	public static class ColContext extends ParserRuleContext {
		public StrictIdentifierContext strictIdentifier() {
			return getRuleContext(StrictIdentifierContext.class,0);
		}
		public ColContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_col; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).enterCol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SqlListener ) ((SqlListener)listener).exitCol(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SqlVisitor ) return ((SqlVisitor<? extends T>)visitor).visitCol(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ColContext col() throws RecognitionException {
		ColContext _localctx = new ColContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_col);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(212);
			strictIdentifier();
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\'\u00d9\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\3\2\3\2\3\2\7\2,\n\2\f\2\16\2/\13\2\3\3\3\3\3\3\5"+
		"\3\64\n\3\3\3\3\3\3\3\3\3\7\3:\n\3\f\3\16\3=\13\3\5\3?\n\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\5\3J\n\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3R\n\3\3\3"+
		"\5\3U\n\3\3\3\7\3X\n\3\f\3\16\3[\13\3\3\3\3\3\5\3_\n\3\3\3\3\3\5\3c\n"+
		"\3\3\3\3\3\7\3g\n\3\f\3\16\3j\13\3\3\3\5\3m\n\3\3\3\3\3\3\3\7\3r\n\3\f"+
		"\3\16\3u\13\3\3\3\3\3\7\3y\n\3\f\3\16\3|\13\3\3\3\3\3\7\3\u0080\n\3\f"+
		"\3\16\3\u0083\13\3\3\3\3\3\7\3\u0087\n\3\f\3\16\3\u008a\13\3\3\3\3\3\7"+
		"\3\u008e\n\3\f\3\16\3\u0091\13\3\3\3\3\3\3\3\7\3\u0096\n\3\f\3\16\3\u0099"+
		"\13\3\5\3\u009b\n\3\3\4\7\4\u009e\n\4\f\4\16\4\u00a1\13\4\3\4\3\4\7\4"+
		"\u00a5\n\4\f\4\16\4\u00a8\13\4\3\4\5\4\u00ab\n\4\3\5\6\5\u00ae\n\5\r\5"+
		"\16\5\u00af\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\f"+
		"\6\f\u00c0\n\f\r\f\16\f\u00c1\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3"+
		"\20\6\20\u00cd\n\20\r\20\16\20\u00ce\3\21\3\21\3\22\3\22\3\23\3\23\3\24"+
		"\3\24\3\24\2\2\25\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&\2\f\3\2"+
		"\4\5\3\2\t\t\4\2\n\n!\"\3\2\f\r\3\2\16\17\3\2\21\22\3\2\23\24\3\2\25\26"+
		"\4\2\27\27!\"\4\2##\'\'\2\u00ea\2-\3\2\2\2\4\u009a\3\2\2\2\6\u00aa\3\2"+
		"\2\2\b\u00ad\3\2\2\2\n\u00b1\3\2\2\2\f\u00b3\3\2\2\2\16\u00b5\3\2\2\2"+
		"\20\u00b7\3\2\2\2\22\u00b9\3\2\2\2\24\u00bb\3\2\2\2\26\u00bf\3\2\2\2\30"+
		"\u00c3\3\2\2\2\32\u00c5\3\2\2\2\34\u00c9\3\2\2\2\36\u00cc\3\2\2\2 \u00d0"+
		"\3\2\2\2\"\u00d2\3\2\2\2$\u00d4\3\2\2\2&\u00d6\3\2\2\2()\5\4\3\2)*\5 "+
		"\21\2*,\3\2\2\2+(\3\2\2\2,/\3\2\2\2-+\3\2\2\2-.\3\2\2\2.\3\3\2\2\2/-\3"+
		"\2\2\2\60\61\7\30\2\2\61\63\5\26\f\2\62\64\7\3\2\2\63\62\3\2\2\2\63\64"+
		"\3\2\2\2\64\65\3\2\2\2\65>\5\30\r\2\66\67\t\2\2\2\67;\5\32\16\28:\5\24"+
		"\13\298\3\2\2\2:=\3\2\2\2;9\3\2\2\2;<\3\2\2\2<?\3\2\2\2=;\3\2\2\2>\66"+
		"\3\2\2\2>?\3\2\2\2?@\3\2\2\2@A\7\6\2\2AB\5\34\17\2B\u009b\3\2\2\2CI\7"+
		"\31\2\2DJ\5\n\6\2EJ\5\f\7\2FJ\5\16\b\2GJ\5\20\t\2HJ\5\22\n\2ID\3\2\2\2"+
		"IE\3\2\2\2IF\3\2\2\2IG\3\2\2\2IH\3\2\2\2IJ\3\2\2\2JK\3\2\2\2KL\5\34\17"+
		"\2LM\7\6\2\2MN\5\26\f\2NO\7\3\2\2OQ\5\30\r\2PR\t\2\2\2QP\3\2\2\2QR\3\2"+
		"\2\2RT\3\2\2\2SU\5\32\16\2TS\3\2\2\2TU\3\2\2\2UY\3\2\2\2VX\5\24\13\2W"+
		"V\3\2\2\2X[\3\2\2\2YW\3\2\2\2YZ\3\2\2\2Z^\3\2\2\2[Y\3\2\2\2\\]\7\7\2\2"+
		"]_\5&\24\2^\\\3\2\2\2^_\3\2\2\2_b\3\2\2\2`a\7\b\2\2ac\5\b\5\2b`\3\2\2"+
		"\2bc\3\2\2\2c\u009b\3\2\2\2dh\7\32\2\2eg\n\3\2\2fe\3\2\2\2gj\3\2\2\2h"+
		"f\3\2\2\2hi\3\2\2\2il\3\2\2\2jh\3\2\2\2km\7\6\2\2lk\3\2\2\2lm\3\2\2\2"+
		"mn\3\2\2\2n\u009b\5\34\17\2os\7\35\2\2pr\n\3\2\2qp\3\2\2\2ru\3\2\2\2s"+
		"q\3\2\2\2st\3\2\2\2t\u009b\3\2\2\2us\3\2\2\2vz\7\36\2\2wy\n\3\2\2xw\3"+
		"\2\2\2y|\3\2\2\2zx\3\2\2\2z{\3\2\2\2{\u009b\3\2\2\2|z\3\2\2\2}\u0081\7"+
		"\37\2\2~\u0080\n\3\2\2\177~\3\2\2\2\u0080\u0083\3\2\2\2\u0081\177\3\2"+
		"\2\2\u0081\u0082\3\2\2\2\u0082\u009b\3\2\2\2\u0083\u0081\3\2\2\2\u0084"+
		"\u0088\7\34\2\2\u0085\u0087\n\3\2\2\u0086\u0085\3\2\2\2\u0087\u008a\3"+
		"\2\2\2\u0088\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089\u009b\3\2\2\2\u008a"+
		"\u0088\3\2\2\2\u008b\u008f\7\33\2\2\u008c\u008e\n\3\2\2\u008d\u008c\3"+
		"\2\2\2\u008e\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090"+
		"\u009b\3\2\2\2\u0091\u008f\3\2\2\2\u0092\u009b\7%\2\2\u0093\u0097\5\6"+
		"\4\2\u0094\u0096\n\3\2\2\u0095\u0094\3\2\2\2\u0096\u0099\3\2\2\2\u0097"+
		"\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u009b\3\2\2\2\u0099\u0097\3\2"+
		"\2\2\u009a\60\3\2\2\2\u009aC\3\2\2\2\u009ad\3\2\2\2\u009ao\3\2\2\2\u009a"+
		"v\3\2\2\2\u009a}\3\2\2\2\u009a\u0084\3\2\2\2\u009a\u008b\3\2\2\2\u009a"+
		"\u0092\3\2\2\2\u009a\u0093\3\2\2\2\u009b\5\3\2\2\2\u009c\u009e\t\4\2\2"+
		"\u009d\u009c\3\2\2\2\u009e\u00a1\3\2\2\2\u009f\u009d\3\2\2\2\u009f\u00a0"+
		"\3\2\2\2\u00a0\u00a2\3\2\2\2\u00a1\u009f\3\2\2\2\u00a2\u00ab\7\13\2\2"+
		"\u00a3\u00a5\7!\2\2\u00a4\u00a3\3\2\2\2\u00a5\u00a8\3\2\2\2\u00a6\u00a4"+
		"\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a9\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a9"+
		"\u00ab\7\3\2\2\u00aa\u009f\3\2\2\2\u00aa\u00a6\3\2\2\2\u00ab\7\3\2\2\2"+
		"\u00ac\u00ae\7\"\2\2\u00ad\u00ac\3\2\2\2\u00ae\u00af\3\2\2\2\u00af\u00ad"+
		"\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\t\3\2\2\2\u00b1\u00b2\t\5\2\2\u00b2"+
		"\13\3\2\2\2\u00b3\u00b4\t\6\2\2\u00b4\r\3\2\2\2\u00b5\u00b6\7\20\2\2\u00b6"+
		"\17\3\2\2\2\u00b7\u00b8\t\7\2\2\u00b8\21\3\2\2\2\u00b9\u00ba\t\b\2\2\u00ba"+
		"\23\3\2\2\2\u00bb\u00bc\t\t\2\2\u00bc\u00bd\5\32\16\2\u00bd\25\3\2\2\2"+
		"\u00be\u00c0\t\n\2\2\u00bf\u00be\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00bf"+
		"\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\27\3\2\2\2\u00c3\u00c4\7#\2\2\u00c4"+
		"\31\3\2\2\2\u00c5\u00c6\5\36\20\2\u00c6\u00c7\7\13\2\2\u00c7\u00c8\7 "+
		"\2\2\u00c8\33\3\2\2\2\u00c9\u00ca\5\36\20\2\u00ca\35\3\2\2\2\u00cb\u00cd"+
		"\t\n\2\2\u00cc\u00cb\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce"+
		"\u00cf\3\2\2\2\u00cf\37\3\2\2\2\u00d0\u00d1\7\t\2\2\u00d1!\3\2\2\2\u00d2"+
		"\u00d3\t\13\2\2\u00d3#\3\2\2\2\u00d4\u00d5\7\n\2\2\u00d5%\3\2\2\2\u00d6"+
		"\u00d7\5\"\22\2\u00d7\'\3\2\2\2\33-\63;>IQTY^bhlsz\u0081\u0088\u008f\u0097"+
		"\u009a\u009f\u00a6\u00aa\u00af\u00c1\u00ce";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}