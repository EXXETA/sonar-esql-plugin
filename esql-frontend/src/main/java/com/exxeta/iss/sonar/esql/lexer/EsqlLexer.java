/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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

package com.exxeta.iss.sonar.esql.lexer;

public final class EsqlLexer {

  private EsqlLexer() {
  }

  private static final String EXP = "([Ee][+-]?+[0-9_]++)";
  private static final String BINARY_EXP = "([Pp][+-]?+[0-9_]++)";

  private static final String FLOAT_SUFFIX = "[fFdD]";
  private static final String INT_SUFFIX = "[lL]";

  public static final String NUMERIC_LITERAL = "(?:"
      // Decimal
      + "[0-9]++\\.([0-9]++)?+" + EXP + "?+" + FLOAT_SUFFIX + "?+"
      // Decimal
      + "|\\.[0-9]++" + EXP + "?+" + FLOAT_SUFFIX + "?+"
      // Decimal
      + "|[0-9]++" + FLOAT_SUFFIX
      + "|[0-9]++" + EXP + FLOAT_SUFFIX + "?+"
      // Hexadecimal
      + "|0[xX][0-9a-fA-F]++\\.[0-9a-fA-F_]*+" + BINARY_EXP + "?+" + FLOAT_SUFFIX + "?+"
      // Hexadecimal
      + "|0[xX][0-9a-fA-F]++" + BINARY_EXP + FLOAT_SUFFIX + "?+"

      // Integer Literals
      // Hexadecimal
      + "|0[xX][0-9a-fA-F]++" + INT_SUFFIX + "?+"
      // Binary (Java 7)
      + "|0[bB][01]++" + INT_SUFFIX + "?+"
      // Decimal and Octal
      + "|[0-9]++" + INT_SUFFIX + "?+"
      + ")";

  public static final String LITERAL = "(?:"
	      + "'([^']*+('')?+)*+'"
	      + ")";
  public static final String ORG_LITERAL = "(?:"
	      + "\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""
	      + "|'([^'\\\\]*+(\\\\[\\s\\S])?+)*+'"
	      + ")";

  public static final String SINGLE_LINE_COMMENT = "--[^\\n\\r]*+";
  
  public static final String MULTI_LINE_COMMENT = "/\\*[\\s\\S]*?\\*/";
  public static final String MULTI_LINE_COMMENT_NO_LB = "/\\*[^\\n\\r]*?\\*/";

  public static final String COMMENT = "(?:" + SINGLE_LINE_COMMENT + "|" + MULTI_LINE_COMMENT + ")"; 


  /**
   * LF, CR, LS, PS
   */
  public static final String LINE_TERMINATOR = "\\n\\r\\u2028\\u2029";
  public static final String LINE_TERMINATOR_SEQUENCE = "(?:\\n|\\r\\n|\\r|\\u2028|\\u2029)";


  private static final String HEX_DIGIT = "[0-9a-fA-F]";
  private static final String UNICODE_ESCAPE_SEQUENCE = "u" + HEX_DIGIT + HEX_DIGIT + HEX_DIGIT + HEX_DIGIT;
  public static final String HEX_LITERAL = "(?:[Xx]'"+HEX_DIGIT+"*+')";

  private static final String UNICODE_LETTER = "\\p{Lu}\\p{Ll}\\p{Lt}\\p{Lm}\\p{Lo}\\p{Nl}";
  private static final String UNICODE_COMBINING_MARK = "\\p{Mn}\\p{Mc}";
  private static final String UNICODE_DIGIT = "\\p{Nd}";
  private static final String UNICODE_CONNECTOR_PUNCTUATION = "\\p{Pc}";

  private static final String IDENTIFIER_START = "(?:[$_" + UNICODE_LETTER + "]|\\\\" + UNICODE_ESCAPE_SEQUENCE + ")";
  private static final String IDENTIFIER_PART = "(?:" + IDENTIFIER_START + "|[" + UNICODE_COMBINING_MARK + UNICODE_DIGIT + UNICODE_CONNECTOR_PUNCTUATION + "])";
  private static final String IDENTIFIER_PART_ESCAPED = "(?:" + IDENTIFIER_START + "|[" + UNICODE_COMBINING_MARK + UNICODE_DIGIT + UNICODE_CONNECTOR_PUNCTUATION+ "-" + "]|\"\")";

  public static final String IDENTIFIER = IDENTIFIER_START + IDENTIFIER_PART + "*+";
  public static final String IDENTIFIER_WITH_QUOTES ="\"(" +IDENTIFIER_PART_ESCAPED + "*+)"+"\"";

  

  /**
   * Tab, Vertical Tab, Form Feed, Space, No-break space, Byte Order Mark, Any other Unicode "space separator"
   */
  public static final String WHITESPACE = "[\\n\\r\\t\\u000B\\f\\u0020\\u00A0\\uFEFF\\p{Zs}]";

  //TODO needs to be case insensitive?
  public static final String TIME_LITERAL = "TIME"+WHITESPACE+"+'[0-9]{2}:[0-9]{2}:[0-9]{2}'";
  public static final String DATE_LITERAL = "DATE"+WHITESPACE+"+'[0-9]{4}-[0-9]{2}-[0-9]{2}'";
  public static final String TIMESTAMP_LITERAL = "TIMESTAMP"+WHITESPACE+"+'[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}'";

  
/*  public static Lexer create(EsqlConfiguration conf) {
	  LexerState lexerState = new LexerState();
    return Lexer.builder()
        .withCharset(conf.getCharset())

        .withFailIfNoChannelToConsumeOneCharacter(true)

        // Channels, which consumes more frequently should come first.
        // Whitespace character occurs more frequently than any other, and thus come first:
        .withChannel(new BlackHoleChannel(WHITESPACE + "++"))

        .withChannel(new NewLineChannel(lexerState))
        
        // Comments
        .withChannel(commentRegexp(COMMENT))

        // String Literals
        
        .withChannel(regexp(EsqlTokenType.STRING, LITERAL))

        .withChannel(regexp(EsqlTokenType.NUMBER, NUMERIC_LITERAL))
        .withChannel(regexp(EsqlTokenType.HEX, HEX_LITERAL))
        .withChannel(regexp(EsqlTokenType.TIME, TIME_LITERAL))
        .withChannel(regexp(EsqlTokenType.DATE, DATE_LITERAL))

        .withChannel(new IdentifierAndKeywordChannel("[a-zA-Z0-9_]+|\"(?:[^\"]|\"\")+\"", false))
        .withChannel(new PunctuatorChannel(EsqlPunctuator.values()))

        .withChannel(new UnknownCharacterChannel())

        .build();
  }*/
}
