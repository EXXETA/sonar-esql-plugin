package com.exxeta.iss.sonar.esql.colorizer;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.sonar.api.source.Highlightable;

import com.exxeta.iss.sonar.esql.EsqlConfiguration;
import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.EsqlReservedKeyword;
import com.exxeta.iss.sonar.esql.lexer.EsqlLexer;
import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.TokenType;
import com.sonar.sslr.api.Trivia;
import com.sonar.sslr.impl.Lexer;

public class EsqlHighlighter {

	private Lexer lexer;
	private Charset charset;

	public EsqlHighlighter(EsqlConfiguration conf) {
		this.lexer = EsqlLexer.create(conf);
		this.charset = conf.getCharset();
	}

	public void highlight(Highlightable highlightable, File file) {
		SourceFileOffsets offsets = new SourceFileOffsets(file, charset);
		List<Token> tokens = lexer.lex(file);
		doHighlight(highlightable, tokens, offsets);
	}

	public void highlight(Highlightable highlightable, String string) {
		SourceFileOffsets offsets = new SourceFileOffsets(string);
		List<Token> tokens = lexer.lex(string);
		doHighlight(highlightable, tokens, offsets);
	}

	private void doHighlight(Highlightable highlightable, List<Token> tokens, SourceFileOffsets offsets) {
		Highlightable.HighlightingBuilder highlighting = highlightable.newHighlighting();
		highlightStringsAndKeywords(highlighting, tokens, offsets);
		highlightComments(highlighting, tokens, offsets);
		highlighting.done();
	}

	private void highlightComments(Highlightable.HighlightingBuilder highlighting, List<Token> tokens,
			SourceFileOffsets offsets) {
		String code;
		for (Token token : tokens) {
			if (!token.getTrivia().isEmpty()) {
				for (Trivia trivia : token.getTrivia()) {
					if (trivia.getToken().getValue().startsWith("--")) {
						code = "j";
					} else {
						code = "cd";
					}
					highlight(highlighting, offsets.startOffset(trivia.getToken()),
							offsets.endOffset(trivia.getToken()), code);
				}
			}
		}
	}

	private void highlightStringsAndKeywords(Highlightable.HighlightingBuilder highlighting, List<Token> tokens,
			SourceFileOffsets offsets) {
		for (Token token : tokens) {
			if (GenericTokenType.LITERAL.equals(token.getType())) {
				highlight(highlighting, offsets.startOffset(token), offsets.endOffset(token), "s");
			}
			if (isKeyword(token)) {
				highlight(highlighting, offsets.startOffset(token), offsets.endOffset(token), "k");
			}
		}
	}

	private static void highlight(Highlightable.HighlightingBuilder highlighting, int startOffset, int endOffset,
			String code) {
		if (endOffset > startOffset) {
			highlighting.highlight(startOffset, endOffset, code);
		}
	}

	public boolean isKeyword(Token token) {
		for (TokenType keywordType : EsqlReservedKeyword.values()) {
			if (keywordType.getValue().equals(token.getValue())) {
				return true;
			}
		}
		for (TokenType keywordType : EsqlNonReservedKeyword.values()) {
			if (keywordType.getValue().equals(token.getValue())) {
				return true;
			}
		}
		return false;
	}
}
