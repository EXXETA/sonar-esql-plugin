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
package com.exxeta.iss.sonar.esql.parser;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxTrivia;
import com.google.common.collect.Lists;
import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Rule;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.TokenType;
import com.sonar.sslr.api.Trivia;
import com.sonar.sslr.api.typed.Input;
import com.sonar.sslr.api.typed.NodeBuilder;
import java.util.Iterator;
import java.util.List;
import org.sonar.sslr.grammar.GrammarRuleKey;

public class EsqlNodeBuilder implements NodeBuilder {

  public static final char BYTE_ORDER_MARK = '\uFEFF';

  @Override
  public Object createNonTerminal(GrammarRuleKey ruleKey, Rule rule, List<Object> children, int startIndex, int endIndex) {
    for (Object child : children) {
      if (child instanceof InternalSyntaxToken) {
        return child;
      }
    }
    return new InternalSyntaxSpacing();
  }

  @Override
  public Object createTerminal(Input input, int startIndex, int endIndex, List<Trivia> trivias, TokenType type) {
    char[] fileChars = input.input();
    boolean hasByteOrderMark = fileChars.length > 0 && fileChars[0] == BYTE_ORDER_MARK;
    boolean isEof = GenericTokenType.EOF.equals(type);
    LineColumnValue lineColumnValue = tokenPosition(input, startIndex, endIndex);
    return new InternalSyntaxToken(
      lineColumnValue.line,
      column(hasByteOrderMark, lineColumnValue.line, lineColumnValue.column),
      lineColumnValue.value,
      createTrivias(trivias, hasByteOrderMark),
      startIndex - (hasByteOrderMark ? 1 : 0),
      isEof
    );
  }

  private static int column(boolean hasByteOrderMark, int line, int column) {
    if (hasByteOrderMark && line == 1) {
      return column - 1;
    }
    return column;
  }

  private static List<SyntaxTrivia> createTrivias(List<Trivia> trivias, boolean hasByteOrderMark) {
    List<SyntaxTrivia> result = Lists.newArrayList();
    for (Trivia trivia : trivias) {
      Token trivialToken = trivia.getToken();
      int column = column(hasByteOrderMark, trivialToken.getLine(), trivialToken.getColumn());
      result.add(InternalSyntaxTrivia.create(trivialToken.getValue(), trivialToken.getLine(), column));
    }
    return result;
  }

  private static LineColumnValue tokenPosition(Input input, int startIndex, int endIndex) {
    int[] lineAndColumn = input.lineAndColumnAt(startIndex);
    String value = input.substring(startIndex, endIndex);
    return new LineColumnValue(lineAndColumn[0], lineAndColumn[1] - 1, value);
  }

  private static class LineColumnValue {
    final int line;
    final int column;
    final String value;

    private LineColumnValue(int line, int column, String value) {
      this.line = line;
      this.column = column;
      this.value = value;
    }
  }

  private static class InternalSyntaxSpacing extends EsqlTree {

    @Override
    public void accept(DoubleDispatchVisitor visitor) {
      // nothing to do
    }

    @Override
    public Kind getKind() {
      return Kind.TRIVIA;
    }

    @Override
    public Iterator<Tree> childrenIterator() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isLeaf() {
      return true;
    }

  }

}

