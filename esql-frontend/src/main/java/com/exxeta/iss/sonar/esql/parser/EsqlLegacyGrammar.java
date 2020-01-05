/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.parser;

import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.COLON;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.COMMA;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.CONCAT;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.DIV;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.DOT;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.EQUAL;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.GE;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.GT;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.LBRACKET;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.LCURLYBRACE;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.LE;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.LPARENTHESIS;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.LT;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.MINUS;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.NOTEQUAL;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.PLUS;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.RBRACKET;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.RCURLYBRACE;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.RPARENTHESIS;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.SEMI;
import static com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator.STAR;

import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;
import org.sonar.sslr.parser.LexerlessGrammar;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.EsqlTokenType;
import com.exxeta.iss.sonar.esql.lexer.EsqlLexer;
import com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator;
import com.exxeta.iss.sonar.esql.lexer.EsqlReservedKeyword;
import com.sonar.sslr.api.GenericTokenType;
import static com.exxeta.iss.sonar.esql.lexer.EsqlTokenType.IDENTIFIER;

public enum EsqlLegacyGrammar implements GrammarRuleKey {
	EOF, PROGRAM, EOS, LITERAL, BOOLEAN_LITERAL, NULL_LITERAL, NUMERIC_LITERAL, HEX_LITERAL, 
	STRING_LITERAL, SPACING, COMMENT, SINGLE_LINE_COMMENT, MULTI_LINE_COMMENT, IDENTIFIER_NAME, IDENTIFIER_NAME_WO_QUOTES, IDENTIFIER_NAME_WITH_QUOTES
	, DATE_LITERAL, TIME_LITERAL, TIMESTAMP_LITERAL, 
	 SPACING_NO_LINE_BREAK_NOT_FOLLOWED_BY_LINE_BREAK, SPACING_NO_LB, NEXT_NOT_LB, 
	SPACING_NOT_SKIPPED, LINE_TERMINATOR_SEQUENCE, EOS_NO_LB, LETTER_OR_DIGIT, reservedKeyword, 
	intervalLiteral, intervalQualifier, keyword, nonReservedKeyword, dataType, 
	leftHandSideExpression, unaryExpression, multiplicativeExpression, additiveExpression, 
	relationalExpression, equalityExpression, primaryExpression, IDENTIFIER_REFERENCE, BINDING_IDENTIFIER;

	private final String internalName;

	EsqlLegacyGrammar() {
		String name = name();
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while (i < name.length()) {
			if (name.charAt(i) == '_' && i + 1 < name.length()) {
				i++;
				sb.append(name.charAt(i));
			} else {
				sb.append(Character.toLowerCase(name.charAt(i)));
			}
			i++;
		}
		internalName = sb.toString();
	}

	/**
	 * A.1 Lexical
	 */
	private static void lexical(LexerlessGrammarBuilder b) {
		b.rule(SPACING_NO_LINE_BREAK_NOT_FOLLOWED_BY_LINE_BREAK).is(SPACING_NO_LB, NEXT_NOT_LB);

		b.rule(SPACING).is(b.skippedTrivia(b.regexp("[" + EsqlLexer.LINE_TERMINATOR + EsqlLexer.WHITESPACE + "]*+")),
				b.zeroOrMore(COMMENT,
						b.skippedTrivia(b.regexp("[" + EsqlLexer.LINE_TERMINATOR + EsqlLexer.WHITESPACE + "]*+"))))
				.skip();
		b.rule(COMMENT).is(b.commentTrivia(b.firstOf(
				SINGLE_LINE_COMMENT,
				MULTI_LINE_COMMENT
		)));
		b.rule(SINGLE_LINE_COMMENT).is (b.regexp(EsqlLexer.SINGLE_LINE_COMMENT));
		b.rule(MULTI_LINE_COMMENT).is (b.regexp("/\\*"),
				b.optional(b.regexp(EsqlLexer.COMMENT_CONTENT)), b.optional(MULTI_LINE_COMMENT), b.optional(b.regexp(EsqlLexer.COMMENT_CONTENT)),
				b.regexp("\\*/"));
		b.rule(SPACING_NOT_SKIPPED).is(SPACING);

		b.rule(SPACING_NO_LB).is(b.zeroOrMore(b.firstOf(b.skippedTrivia(b.regexp("[" + EsqlLexer.WHITESPACE + "]++")),
				b.commentTrivia(b.regexp(
						"(?:" + EsqlLexer.SINGLE_LINE_COMMENT + "|" + EsqlLexer.MULTI_LINE_COMMENT_NO_LB + ")")))))
				.skip();
		b.rule(NEXT_NOT_LB)
				.is(b.nextNot(b.regexp("(?:" + EsqlLexer.MULTI_LINE_COMMENT + "|[" + EsqlLexer.LINE_TERMINATOR + "])")))
				.skip();

		b.rule(LINE_TERMINATOR_SEQUENCE).is(b.skippedTrivia(b.regexp(EsqlLexer.LINE_TERMINATOR_SEQUENCE))).skip();

		b.rule(EOS).is(b.firstOf(b.sequence(SPACING, SEMI), b.sequence(SPACING_NO_LB, LINE_TERMINATOR_SEQUENCE),
				b.sequence(SPACING_NO_LB, b.next("}")), b.sequence(SPACING, b.endOfInput())));

		b.rule(EOS_NO_LB)
				.is(b.firstOf(b.sequence(SPACING_NO_LB, NEXT_NOT_LB, SEMI),
						b.sequence(SPACING_NO_LB, LINE_TERMINATOR_SEQUENCE), b.sequence(SPACING_NO_LB, b.next("}")),
						b.sequence(SPACING_NO_LB, b.endOfInput())));

		b.rule(EOF).is(b.token(GenericTokenType.EOF, b.endOfInput())).skip();

		b.rule(IDENTIFIER).is(b.nextNot(LITERAL), SPACING, b.regexp(EsqlLexer.IDENTIFIER), b.nextNot(COLON), b.nextNot(DOT));
		 b.rule(IDENTIFIER_NAME).is(
			      SPACING,b.firstOf(IDENTIFIER_NAME_WO_QUOTES, IDENTIFIER_NAME_WITH_QUOTES));
		b.rule(IDENTIFIER_NAME_WO_QUOTES).is(b.nextNot(LITERAL), SPACING, b.regexp(EsqlLexer.IDENTIFIER));
		b.rule(IDENTIFIER_NAME_WITH_QUOTES).is(SPACING, b.regexp(EsqlLexer.IDENTIFIER_WITH_QUOTES));
		b.rule(NUMERIC_LITERAL).is(SPACING, b.token(EsqlTokenType.NUMBER, b.regexp(EsqlLexer.NUMERIC_LITERAL)),
				SPACING);
		b.rule(STRING_LITERAL).is(SPACING, b.token(EsqlTokenType.STRING, b.regexp(EsqlLexer.LITERAL)), SPACING);
		b.rule(HEX_LITERAL).is(SPACING, b.token(EsqlTokenType.HEX, b.regexp(EsqlLexer.HEX_LITERAL)), SPACING);
		b.rule(intervalLiteral).is(SPACING, "INTERVAL", LITERAL, intervalQualifier);
		b.rule(DATE_LITERAL).is(SPACING, b.regexp(EsqlLexer.DATE_LITERAL));
		b.rule(TIME_LITERAL).is(SPACING, b.regexp(EsqlLexer.TIME_LITERAL));
		b.rule(TIMESTAMP_LITERAL).is(SPACING, b.regexp(EsqlLexer.TIMESTAMP_LITERAL));
		punctuators(b);
		keywords(b);
	}

	private static void punctuators(LexerlessGrammarBuilder b) {
		punctuator(b, SEMI, ";");
		punctuator(b, COMMA, ",");
		punctuator(b, DOT, ".");
		punctuator(b, LPARENTHESIS, "(");
		punctuator(b, RPARENTHESIS, ")");
		punctuator(b, COLON, ":");
		punctuator(b, EQUAL, "=");
		punctuator(b, NOTEQUAL, "<>");
		punctuator(b, LT, "<", b.nextNot(b.firstOf("=", ">")));
		punctuator(b, GT, ">", b.nextNot("="));
		punctuator(b, STAR, "*");
		punctuator(b, DIV, "/");
		punctuator(b, PLUS, "+");
		punctuator(b, MINUS, "-");
		punctuator(b, LE, "<=");
		punctuator(b, GE, ">=");
		punctuator(b, LBRACKET, "[");
		punctuator(b, RBRACKET, "]");
		punctuator(b, LCURLYBRACE, "{");
		punctuator(b, RCURLYBRACE, "}");
		punctuator(b, CONCAT, "||");
	}

	private static void punctuator(LexerlessGrammarBuilder b, GrammarRuleKey ruleKey, String value) {
		for (EsqlPunctuator tokenType : EsqlPunctuator.values()) {
			if (value.equals(tokenType.getValue())) {
				b.rule(tokenType).is(SPACING, value);
				return;
			}
		}
		throw new IllegalStateException(value);
	}

	private static void punctuator(LexerlessGrammarBuilder b, GrammarRuleKey ruleKey, String value, Object element) {
		for (EsqlPunctuator tokenType : EsqlPunctuator.values()) {
			if (value.equals(tokenType.getValue())) {
				b.rule(tokenType).is(SPACING, value, element);
				return;
			}
		}
		throw new IllegalStateException(value);
	}

	private static void keywords(LexerlessGrammarBuilder b) {
		b.rule(LETTER_OR_DIGIT).is(b.regexp("\\p{javaJavaIdentifierPart}"));
		b.rule(keyword).is(b.firstOf(nonReservedKeyword, reservedKeyword));
		reservedKeywords(b);
		nonReservedKeywords(b);

	}

	private static void nonReservedKeywords(LexerlessGrammarBuilder b) {
		Object[] rest = new Object[EsqlNonReservedKeyword.values().length];
		int i = 0;
		for (EsqlNonReservedKeyword tokenType : EsqlNonReservedKeyword.values()) {
			b.rule(tokenType).is(SPACING, b.regexp("(?i)" + tokenType.getValue()), b.nextNot(LETTER_OR_DIGIT));
			rest[i++] = b.regexp("(?i)" + tokenType.getValue());
		}
		Object[] rest2 = new Object[rest.length - 2];
		System.arraycopy(rest, 2, rest2, 0, rest2.length);
		b.rule(nonReservedKeyword).is(b.firstOf(EsqlNonReservedKeyword.keywordValues()[0],
				EsqlNonReservedKeyword.keywordValues()[1], rest), b.nextNot(LETTER_OR_DIGIT));
	}

	private static void reservedKeywords(LexerlessGrammarBuilder b) {
		Object[] rest = new Object[EsqlReservedKeyword.values().length];
		int i = 0;
		for (EsqlReservedKeyword tokenType : EsqlReservedKeyword.values()) {
			b.rule(tokenType).is(SPACING, b.regexp("(?i)" + tokenType.getValue()), b.nextNot(LETTER_OR_DIGIT));
			rest[i++] = b.regexp("(?i)" + tokenType.getValue());
		}

		Object[] rest2 = new Object[rest.length - 2];
		System.arraycopy(rest, 2, rest2, 0, rest2.length);
		b.rule(reservedKeyword).is(
				b.firstOf(EsqlReservedKeyword.keywordValues()[0], EsqlReservedKeyword.keywordValues()[1], rest),
				b.nextNot(LETTER_OR_DIGIT));
	}

	public static LexerlessGrammar createGrammar() {
		return createGrammarBuilder().build();
	}

	public static LexerlessGrammarBuilder createGrammarBuilder() {
		LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();

		
		lexical(b);
		b.setRootRule(PROGRAM);
		return b;
	}

	@Override
	public String toString() {
		// This allows to keep compatibility with old XPath expressions
		return internalName;
	}

}
