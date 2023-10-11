/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.api.visitors;

import javax.annotation.Nullable;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public class IssueLocation {

  private final SyntaxToken firstToken;
  private final SyntaxToken lastToken;
  private final String message;

  public IssueLocation(Tree tree, @Nullable String message) {
    this(tree, tree, message);
  }

  public IssueLocation(Tree tree) {
    this(tree, null);
  }

  public IssueLocation(Tree firstTree, Tree lastTree, @Nullable String message) {
    this.firstToken = ((EsqlTree) firstTree).firstToken();
    this.lastToken = ((EsqlTree) lastTree).lastToken();
    this.message = message;
  }

  @Nullable
  public String message() {
    return message;
  }

  public int startLine() {
    return firstToken.line();
  }

  public int startLineOffset() {
    return firstToken.column();
  }

  public int endLine() {
    return lastToken.endLine();
  }

  public int endLineOffset() {
    return lastToken.endColumn();
  }

}
