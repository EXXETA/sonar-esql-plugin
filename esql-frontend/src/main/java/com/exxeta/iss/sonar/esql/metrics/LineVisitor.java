package com.exxeta.iss.sonar.esql.metrics;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;

/**
 * Visitor that computes the number of lines of code of a file.
 */
public class LineVisitor extends SubscriptionVisitor {

  private Set<Integer> lines = Sets.newHashSet();
  private int lastLine = 0;

  public LineVisitor(Tree tree) {
    scanTree(tree);
  }

  @Override
  public List<Kind> nodesToVisit() {
    return ImmutableList.of(Kind.TOKEN);
  }

  @Override
  public void visitNode(Tree tree) {
    SyntaxToken token = (SyntaxToken) tree;
    if (!((InternalSyntaxToken) token).isEOF()) {
      lines.add(token.line());

    } else {
      lastLine = token.line();
    }
  }

  public int getLinesOfCodeNumber() {
    return lines.size();
  }

  public Set<Integer> getLinesOfCode() {
    return lines;
  }

  public int getLinesNumber() {
    return lastLine;
  }
}
