package com.exxeta.iss.sonar.esql.api.visitors;

import java.util.List;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.api.tree.Tree;

public abstract class DoubleDispatchVisitorCheck extends DoubleDispatchVisitor implements EsqlCheck {

  private Issues issues = new Issues(this);

  @Override
  public List<Issue> scanFile(TreeVisitorContext context){
    issues.reset();
    scanTree(context);
    return issues.getList();
  }

  /**
   * @deprecated see {@link EsqlCheck#addLineIssue(Tree, String)}
   */
  @Override
  @Deprecated
  public LineIssue addLineIssue(Tree tree, String message) {
    return issues.addLineIssue(tree, message);
  }

  @Override
  public PreciseIssue addIssue(Tree tree, String message) {
    return issues.addIssue(tree, message);
  }

  @Override
  public <T extends Issue> T addIssue(T issue) {
    return issues.addIssue(issue);
  }

}
