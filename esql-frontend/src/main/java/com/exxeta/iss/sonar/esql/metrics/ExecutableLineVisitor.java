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
    		Kind.BEGIN_END_STATEMENT,
    		Kind.CALL_STATEMENT,
    		Kind.CASE_STATEMENT,
    		Kind.DECLARE_STATEMENT,
    		Kind.IF_STATEMENT,
    		Kind.ITERATE_STATEMENT,
    		Kind.LEAVE_STATEMENT,
    		Kind.LOOP_STATEMENT,
    		Kind.PROPAGATE_STATEMENT,
    		Kind.REPEAT_STATEMENT,
    		Kind.RETURN_STATEMENT,
    		Kind.SET_STATEMENT,
    		Kind.CREATE_FUNCTION_STATEMENT,
    		Kind.CREATE_MODULE_STATEMENT,
    		Kind.CREATE_PROCEDURE_STATEMENT);
  }

  @Override
  public void visitNode(Tree tree) {
    executableLines.add(((EsqlTree) tree).getLine());
  }

  public Set<Integer> getExecutableLines() {
    return executableLines;
  }

}
