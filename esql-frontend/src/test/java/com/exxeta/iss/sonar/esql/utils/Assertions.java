/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.utils;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.parser.EsqlGrammar;
import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;
import com.exxeta.iss.sonar.esql.parser.EsqlNodeBuilder;
import com.exxeta.iss.sonar.esql.parser.TreeFactory;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.api.Rule;
import com.sonar.sslr.api.typed.ActionParser;
import org.fest.assertions.GenericAssert;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;
import org.sonar.sslr.tests.ParsingResultComparisonFailure;
import org.sonar.sslr.tests.RuleAssert;

public class Assertions {

  public static RuleAssert assertThat(Rule actual) {
    return new RuleAssert(actual);
  }

  public static ParserAssert assertThat(GrammarRuleKey rule) {
    return assertThat(EsqlLegacyGrammar.createGrammarBuilder(), rule);
  }

  public static ParserAssert assertThat(LexerlessGrammarBuilder b, GrammarRuleKey rule) {
    return new ParserAssert(new ActionParser<Tree>(
      Charsets.UTF_8,
      b,
      EsqlGrammar.class,
      new TreeFactory(),
      new EsqlNodeBuilder(),
      rule));
  }

  public static class ParserAssert extends GenericAssert<ParserAssert, ActionParser<Tree>> {

    public ParserAssert(ActionParser<Tree> actual) {
      super(ParserAssert.class, actual);
    }

    private void parseTillEof(String input) {
      EsqlTree tree = (EsqlTree) actual.parse(input);
      InternalSyntaxToken lastToken = (InternalSyntaxToken) tree.lastToken();
      int eofIndex = input.length();
      if (lastToken.toIndex() != eofIndex) {
        throw new RecognitionException(
          0, "Did not match till EOF, but till line " + lastToken.line() + ": token \"" + lastToken.text() + "\"");
      }
    }

    public ParserAssert matches(String input) {
      isNotNull();
      Preconditions.checkArgument(!hasTrailingWhitespaces(input), "Trailing whitespaces in input are not supported");
      String expected = "Rule '" + getRuleName() + "' should match:\n" + input;
      try {
        parseTillEof(input);
      } catch (RecognitionException e) {
        String actual = e.getMessage();
        throw new ParsingResultComparisonFailure(expected, actual);
      }
      return this;
    }

    private static boolean hasTrailingWhitespaces(String input) {
      return input.endsWith(" ") || input.endsWith("\n") || input.endsWith("\r") || input.endsWith("\t");
    }

    public ParserAssert notMatches(String input) {
      isNotNull();
      try {
        parseTillEof(input);
      } catch (RecognitionException e) {
        // expected
        return this;
      }
      throw new AssertionError("Rule '" + getRuleName() + "' should not match:\n" + input);
    }

    /**
     * Verifies that the actual <code>{@link Rule}</code> partially matches a given input.
     *
     * @param prefixToBeMatched the prefix that must be fully matched
     * @param remainingInput    the remainder of the input, which is not to be matched
     * @return this assertion object.
     */
    public ParserAssert matchesPrefix(String prefixToBeMatched, String remainingInput) {
      isNotNull();
      try {
        EsqlTree tree = (EsqlTree) actual.parse(prefixToBeMatched + remainingInput);
        SyntaxToken lastToken = tree.lastToken();

        if (prefixToBeMatched.length() != lastToken.column() + lastToken.text().length()) {
          throw new RecognitionException(0,
            "Rule '" + getRuleName() + "' should match:\n" + prefixToBeMatched + "\nwhen followed by:\n" + remainingInput);
        }
      } catch (RecognitionException e) {
        throw new RecognitionException(0, e.getMessage() + "\n" +
          "Rule '" + getRuleName() + "' should match:\n" + prefixToBeMatched + "\nwhen followed by:\n" + remainingInput);
      }
      return this;
    }

    private String getRuleName() {
      return actual.rootRule().toString();
    }

  }

}
