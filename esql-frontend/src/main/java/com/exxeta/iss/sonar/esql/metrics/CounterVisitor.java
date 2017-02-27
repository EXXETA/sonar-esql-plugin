package com.exxeta.iss.sonar.esql.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitor;
import com.exxeta.iss.sonar.esql.tree.KindSet;

public class CounterVisitor extends SubscriptionVisitor {

  private int functionCounter = 0;
  private int statementCounter = 0;
  private int classCounter = 0;

  private static final Kind[] STATEMENT_NODES = {
    Kind.VARIABLE_STATEMENT,
    Kind.EMPTY_STATEMENT,
    Kind.EXPRESSION_STATEMENT,
    Kind.IF_STATEMENT,
    Kind.DO_WHILE_STATEMENT,
    Kind.WHILE_STATEMENT,
    Kind.FOR_IN_STATEMENT,
    Kind.FOR_OF_STATEMENT,
    Kind.FOR_STATEMENT,
    Kind.CONTINUE_STATEMENT,
    Kind.BREAK_STATEMENT,
    Kind.RETURN_STATEMENT,
    Kind.WITH_STATEMENT,
    Kind.SWITCH_STATEMENT,
    Kind.THROW_STATEMENT,
    Kind.TRY_STATEMENT,
    Kind.DEBUGGER_STATEMENT
  };

  @Override
  public List<Kind> nodesToVisit() {
    List<Kind> result = new ArrayList<>(KindSet.FUNCTION_KINDS.getSubKinds());
    result.addAll(Arrays.asList(STATEMENT_NODES));
    result.addAll(Arrays.asList(MetricsVisitor.getClassNodes()));
    return result;
  }

  public CounterVisitor(Tree tree) {
    scanTree(tree);
  }

  public int getFunctionNumber() {
    return functionCounter;
  }

  public int getStatementsNumber() {
    return statementCounter;
  }

  public int getClassNumber() {
    return classCounter;
  }

  @Override
  public void visitNode(Tree tree) {
    if (tree.is(KindSet.FUNCTION_KINDS)) {
      functionCounter++;

    } else if (tree.is(STATEMENT_NODES)) {
      statementCounter++;

    } else if (tree.is(MetricsVisitor.getClassNodes())) {
      classCounter++;
    }
  }
}
