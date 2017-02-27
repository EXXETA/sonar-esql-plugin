package com.exxeta.iss.sonar.esql.metrics;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.ImmutableList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExecutableLineVisitor extends SubscriptionVisitorCheck {

  private final Set<Integer> executableLines = new HashSet<>();

  public ExecutableLineVisitor(Tree tree) {
    scanTree(tree);
  }

  @Override
  public List<Kind> nodesToVisit() {
    return ImmutableList.of(
      Kind.DEBUGGER_STATEMENT,
      Kind.VARIABLE_STATEMENT,
      Kind.LABELLED_STATEMENT,
      Kind.RETURN_STATEMENT,
      Kind.CONTINUE_STATEMENT,
      Kind.BREAK_STATEMENT,
      Kind.THROW_STATEMENT,
      Kind.WITH_STATEMENT,
      Kind.TRY_STATEMENT,
      Kind.SWITCH_STATEMENT,
      Kind.IF_STATEMENT,
      Kind.WHILE_STATEMENT,
      Kind.DO_WHILE_STATEMENT,
      Kind.EXPRESSION_STATEMENT,
      Kind.FOR_OF_STATEMENT,
      Kind.FOR_STATEMENT,
      Kind.FOR_IN_STATEMENT);
  }

  @Override
  public void visitNode(Tree tree) {
    executableLines.add(((EsqlTree) tree).getLine());
  }

  public Set<Integer> getExecutableLines() {
    return executableLines;
  }

}
