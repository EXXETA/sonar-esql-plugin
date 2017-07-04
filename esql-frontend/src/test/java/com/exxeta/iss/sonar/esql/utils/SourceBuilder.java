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
package com.exxeta.iss.sonar.esql.utils;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitor;
import com.google.common.collect.ImmutableList;

public class SourceBuilder extends SubscriptionVisitor {

  private final StringBuilder stringBuilder = new StringBuilder();
  private int line = 1;
  private int column = 0;

  public static String build(Tree tree) {
    SourceBuilder writer = new SourceBuilder();
    writer.scanTree(tree);
    return writer.stringBuilder.toString();
  }

  @Override
  public List<Kind> nodesToVisit() {
    return ImmutableList.of(Tree.Kind.TOKEN);
  }

  @Override
  public void visitNode(Tree tree) {
    SyntaxToken token = (SyntaxToken) tree;
    int linesToInsert = token.line() - line;
    if (linesToInsert < 0) {
      throw new IllegalStateException("Illegal token line for " + token);
    } else if (linesToInsert > 0) {
      for (int i = 0; i < linesToInsert; i++) {
        stringBuilder.append("\n");
        line++;
      }
      column = 0;
    }
    int spacesToInsert = token.column() - column;
    for (int i = 0; i < spacesToInsert; i++) {
      stringBuilder.append(' ');
      column++;
    }
    String text = token.text();
    stringBuilder.append(text);
    column += text.length();
  }

}
