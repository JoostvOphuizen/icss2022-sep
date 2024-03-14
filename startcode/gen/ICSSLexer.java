// Generated from C:/dev/icss2022-sep/startcode/src/main/antlr4/nl/han/ica/icss/parser/ICSS.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class ICSSLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		IF=1, ELSE=2, BOX_BRACKET_OPEN=3, BOX_BRACKET_CLOSE=4, TRUE=5, FALSE=6, 
		PIXELSIZE=7, PERCENTAGE=8, SCALAR=9, COLOR=10, ID_IDENT=11, CLASS_IDENT=12, 
		LOWER_IDENT=13, CAPITAL_IDENT=14, WS=15, COMMENT=16, OPEN_BRACE=17, CLOSE_BRACE=18, 
		OPEN_BRACKET=19, CLOSE_BRACKET=20, SEMICOLON=21, COLON=22, PLUS=23, MIN=24, 
		MUL=25, DIVIDE=26, ASSIGNMENT_OPERATOR=27;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"IF", "ELSE", "BOX_BRACKET_OPEN", "BOX_BRACKET_CLOSE", "TRUE", "FALSE", 
			"PIXELSIZE", "PERCENTAGE", "SCALAR", "COLOR", "ID_IDENT", "CLASS_IDENT", 
			"LOWER_IDENT", "CAPITAL_IDENT", "WS", "COMMENT", "OPEN_BRACE", "CLOSE_BRACE", 
			"OPEN_BRACKET", "CLOSE_BRACKET", "SEMICOLON", "COLON", "PLUS", "MIN", 
			"MUL", "DIVIDE", "ASSIGNMENT_OPERATOR"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'if'", "'else'", "'['", "']'", "'TRUE'", "'FALSE'", null, null, 
			null, null, null, null, null, null, null, null, "'{'", "'}'", "'('", 
			"')'", "';'", "':'", "'+'", "'-'", "'*'", "'/'", "':='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "IF", "ELSE", "BOX_BRACKET_OPEN", "BOX_BRACKET_CLOSE", "TRUE", 
			"FALSE", "PIXELSIZE", "PERCENTAGE", "SCALAR", "COLOR", "ID_IDENT", "CLASS_IDENT", 
			"LOWER_IDENT", "CAPITAL_IDENT", "WS", "COMMENT", "OPEN_BRACE", "CLOSE_BRACE", 
			"OPEN_BRACKET", "CLOSE_BRACKET", "SEMICOLON", "COLON", "PLUS", "MIN", 
			"MUL", "DIVIDE", "ASSIGNMENT_OPERATOR"
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


	public ICSSLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "ICSS.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u001b\u00b0\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014"+
		"\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017"+
		"\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006"+
		"\u0004\u0006P\b\u0006\u000b\u0006\f\u0006Q\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0004\u0007X\b\u0007\u000b\u0007\f\u0007Y\u0001\u0007"+
		"\u0001\u0007\u0001\b\u0004\b_\b\b\u000b\b\f\b`\u0001\t\u0001\t\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0004\nm\b\n"+
		"\u000b\n\f\nn\u0001\u000b\u0001\u000b\u0004\u000bs\b\u000b\u000b\u000b"+
		"\f\u000bt\u0001\f\u0001\f\u0005\fy\b\f\n\f\f\f|\t\f\u0001\r\u0001\r\u0005"+
		"\r\u0080\b\r\n\r\f\r\u0083\t\r\u0001\u000e\u0004\u000e\u0086\b\u000e\u000b"+
		"\u000e\f\u000e\u0087\u0001\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001"+
		"\u000f\u0001\u000f\u0005\u000f\u0090\b\u000f\n\u000f\f\u000f\u0093\t\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u0010"+
		"\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013"+
		"\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0015\u0001\u0015\u0001\u0016"+
		"\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0019"+
		"\u0001\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u0091\u0000\u001b"+
		"\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r"+
		"\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e"+
		"\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014)\u0015+\u0016-\u0017"+
		"/\u00181\u00193\u001a5\u001b\u0001\u0000\u0007\u0001\u000009\u0002\u0000"+
		"09af\u0003\u0000--09az\u0001\u0000az\u0001\u0000AZ\u0004\u000009AZ__a"+
		"z\u0003\u0000\t\n\r\r  \u00b8\u0000\u0001\u0001\u0000\u0000\u0000\u0000"+
		"\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000"+
		"\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b"+
		"\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001"+
		"\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001"+
		"\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001"+
		"\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001"+
		"\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001"+
		"\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000"+
		"\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000"+
		"\u0000)\u0001\u0000\u0000\u0000\u0000+\u0001\u0000\u0000\u0000\u0000-"+
		"\u0001\u0000\u0000\u0000\u0000/\u0001\u0000\u0000\u0000\u00001\u0001\u0000"+
		"\u0000\u0000\u00003\u0001\u0000\u0000\u0000\u00005\u0001\u0000\u0000\u0000"+
		"\u00017\u0001\u0000\u0000\u0000\u0003:\u0001\u0000\u0000\u0000\u0005?"+
		"\u0001\u0000\u0000\u0000\u0007A\u0001\u0000\u0000\u0000\tC\u0001\u0000"+
		"\u0000\u0000\u000bH\u0001\u0000\u0000\u0000\rO\u0001\u0000\u0000\u0000"+
		"\u000fW\u0001\u0000\u0000\u0000\u0011^\u0001\u0000\u0000\u0000\u0013b"+
		"\u0001\u0000\u0000\u0000\u0015j\u0001\u0000\u0000\u0000\u0017p\u0001\u0000"+
		"\u0000\u0000\u0019v\u0001\u0000\u0000\u0000\u001b}\u0001\u0000\u0000\u0000"+
		"\u001d\u0085\u0001\u0000\u0000\u0000\u001f\u008b\u0001\u0000\u0000\u0000"+
		"!\u0099\u0001\u0000\u0000\u0000#\u009b\u0001\u0000\u0000\u0000%\u009d"+
		"\u0001\u0000\u0000\u0000\'\u009f\u0001\u0000\u0000\u0000)\u00a1\u0001"+
		"\u0000\u0000\u0000+\u00a3\u0001\u0000\u0000\u0000-\u00a5\u0001\u0000\u0000"+
		"\u0000/\u00a7\u0001\u0000\u0000\u00001\u00a9\u0001\u0000\u0000\u00003"+
		"\u00ab\u0001\u0000\u0000\u00005\u00ad\u0001\u0000\u0000\u000078\u0005"+
		"i\u0000\u000089\u0005f\u0000\u00009\u0002\u0001\u0000\u0000\u0000:;\u0005"+
		"e\u0000\u0000;<\u0005l\u0000\u0000<=\u0005s\u0000\u0000=>\u0005e\u0000"+
		"\u0000>\u0004\u0001\u0000\u0000\u0000?@\u0005[\u0000\u0000@\u0006\u0001"+
		"\u0000\u0000\u0000AB\u0005]\u0000\u0000B\b\u0001\u0000\u0000\u0000CD\u0005"+
		"T\u0000\u0000DE\u0005R\u0000\u0000EF\u0005U\u0000\u0000FG\u0005E\u0000"+
		"\u0000G\n\u0001\u0000\u0000\u0000HI\u0005F\u0000\u0000IJ\u0005A\u0000"+
		"\u0000JK\u0005L\u0000\u0000KL\u0005S\u0000\u0000LM\u0005E\u0000\u0000"+
		"M\f\u0001\u0000\u0000\u0000NP\u0007\u0000\u0000\u0000ON\u0001\u0000\u0000"+
		"\u0000PQ\u0001\u0000\u0000\u0000QO\u0001\u0000\u0000\u0000QR\u0001\u0000"+
		"\u0000\u0000RS\u0001\u0000\u0000\u0000ST\u0005p\u0000\u0000TU\u0005x\u0000"+
		"\u0000U\u000e\u0001\u0000\u0000\u0000VX\u0007\u0000\u0000\u0000WV\u0001"+
		"\u0000\u0000\u0000XY\u0001\u0000\u0000\u0000YW\u0001\u0000\u0000\u0000"+
		"YZ\u0001\u0000\u0000\u0000Z[\u0001\u0000\u0000\u0000[\\\u0005%\u0000\u0000"+
		"\\\u0010\u0001\u0000\u0000\u0000]_\u0007\u0000\u0000\u0000^]\u0001\u0000"+
		"\u0000\u0000_`\u0001\u0000\u0000\u0000`^\u0001\u0000\u0000\u0000`a\u0001"+
		"\u0000\u0000\u0000a\u0012\u0001\u0000\u0000\u0000bc\u0005#\u0000\u0000"+
		"cd\u0007\u0001\u0000\u0000de\u0007\u0001\u0000\u0000ef\u0007\u0001\u0000"+
		"\u0000fg\u0007\u0001\u0000\u0000gh\u0007\u0001\u0000\u0000hi\u0007\u0001"+
		"\u0000\u0000i\u0014\u0001\u0000\u0000\u0000jl\u0005#\u0000\u0000km\u0007"+
		"\u0002\u0000\u0000lk\u0001\u0000\u0000\u0000mn\u0001\u0000\u0000\u0000"+
		"nl\u0001\u0000\u0000\u0000no\u0001\u0000\u0000\u0000o\u0016\u0001\u0000"+
		"\u0000\u0000pr\u0005.\u0000\u0000qs\u0007\u0002\u0000\u0000rq\u0001\u0000"+
		"\u0000\u0000st\u0001\u0000\u0000\u0000tr\u0001\u0000\u0000\u0000tu\u0001"+
		"\u0000\u0000\u0000u\u0018\u0001\u0000\u0000\u0000vz\u0007\u0003\u0000"+
		"\u0000wy\u0007\u0002\u0000\u0000xw\u0001\u0000\u0000\u0000y|\u0001\u0000"+
		"\u0000\u0000zx\u0001\u0000\u0000\u0000z{\u0001\u0000\u0000\u0000{\u001a"+
		"\u0001\u0000\u0000\u0000|z\u0001\u0000\u0000\u0000}\u0081\u0007\u0004"+
		"\u0000\u0000~\u0080\u0007\u0005\u0000\u0000\u007f~\u0001\u0000\u0000\u0000"+
		"\u0080\u0083\u0001\u0000\u0000\u0000\u0081\u007f\u0001\u0000\u0000\u0000"+
		"\u0081\u0082\u0001\u0000\u0000\u0000\u0082\u001c\u0001\u0000\u0000\u0000"+
		"\u0083\u0081\u0001\u0000\u0000\u0000\u0084\u0086\u0007\u0006\u0000\u0000"+
		"\u0085\u0084\u0001\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000"+
		"\u0087\u0085\u0001\u0000\u0000\u0000\u0087\u0088\u0001\u0000\u0000\u0000"+
		"\u0088\u0089\u0001\u0000\u0000\u0000\u0089\u008a\u0006\u000e\u0000\u0000"+
		"\u008a\u001e\u0001\u0000\u0000\u0000\u008b\u008c\u0005/\u0000\u0000\u008c"+
		"\u008d\u0005*\u0000\u0000\u008d\u0091\u0001\u0000\u0000\u0000\u008e\u0090"+
		"\t\u0000\u0000\u0000\u008f\u008e\u0001\u0000\u0000\u0000\u0090\u0093\u0001"+
		"\u0000\u0000\u0000\u0091\u0092\u0001\u0000\u0000\u0000\u0091\u008f\u0001"+
		"\u0000\u0000\u0000\u0092\u0094\u0001\u0000\u0000\u0000\u0093\u0091\u0001"+
		"\u0000\u0000\u0000\u0094\u0095\u0005*\u0000\u0000\u0095\u0096\u0005/\u0000"+
		"\u0000\u0096\u0097\u0001\u0000\u0000\u0000\u0097\u0098\u0006\u000f\u0000"+
		"\u0000\u0098 \u0001\u0000\u0000\u0000\u0099\u009a\u0005{\u0000\u0000\u009a"+
		"\"\u0001\u0000\u0000\u0000\u009b\u009c\u0005}\u0000\u0000\u009c$\u0001"+
		"\u0000\u0000\u0000\u009d\u009e\u0005(\u0000\u0000\u009e&\u0001\u0000\u0000"+
		"\u0000\u009f\u00a0\u0005)\u0000\u0000\u00a0(\u0001\u0000\u0000\u0000\u00a1"+
		"\u00a2\u0005;\u0000\u0000\u00a2*\u0001\u0000\u0000\u0000\u00a3\u00a4\u0005"+
		":\u0000\u0000\u00a4,\u0001\u0000\u0000\u0000\u00a5\u00a6\u0005+\u0000"+
		"\u0000\u00a6.\u0001\u0000\u0000\u0000\u00a7\u00a8\u0005-\u0000\u0000\u00a8"+
		"0\u0001\u0000\u0000\u0000\u00a9\u00aa\u0005*\u0000\u0000\u00aa2\u0001"+
		"\u0000\u0000\u0000\u00ab\u00ac\u0005/\u0000\u0000\u00ac4\u0001\u0000\u0000"+
		"\u0000\u00ad\u00ae\u0005:\u0000\u0000\u00ae\u00af\u0005=\u0000\u0000\u00af"+
		"6\u0001\u0000\u0000\u0000\n\u0000QY`ntz\u0081\u0087\u0091\u0001\u0006"+
		"\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}