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
    this.firstToken = ((EsqlTree) firstTree).getFirstToken();
    this.lastToken = ((EsqlTree) lastTree).getLastToken();
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