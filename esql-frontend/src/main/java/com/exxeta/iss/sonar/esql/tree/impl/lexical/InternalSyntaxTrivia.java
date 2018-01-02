/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.tree.impl.lexical;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;

public class InternalSyntaxTrivia extends EsqlTree implements SyntaxTrivia {

  private final String comment;
  private final int column;
  private int startLine;
  private int endLine;
  private int endColumn;


  public InternalSyntaxTrivia(String comment, int startLine, int column) {
    this.comment = comment;
    this.startLine = startLine;
    this.column = column;
    calculateEndOffsets();
  }

  private void calculateEndOffsets() {
    String[] lines = comment.split("\r\n|\n|\r", -1);
    endColumn = column + comment.length();
    endLine = startLine + lines.length - 1;

    if (endLine != startLine) {
      endColumn = lines[lines.length - 1].length();
    }
  }

  @Override
  public int endLine() {
    return endLine;
  }

  @Override
  public int endColumn() {
    return endColumn;
  }

  @Override
  public String text() {
    return comment;
  }

  @Override
  public List<SyntaxTrivia> trivias() {
    return Collections.emptyList();
  }

  @Override
  public int line() {
    return startLine;
  }

  @Override
  public int column() {
    return column;
  }

  @Override
  public Kind getKind() {
    return Kind.TRIVIA;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    throw new UnsupportedOperationException();
  }

  public static SyntaxTrivia create(String comment, int startLine, int column) {
    return new InternalSyntaxTrivia(comment, startLine, column);
  }

  @Override
  public int getLine() {
    return startLine;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitComment(this);
  }

  @Override
  public SyntaxToken firstToken() {
    return this;
  }

  @Override
  public SyntaxToken lastToken() {
    return this;
  }
}
