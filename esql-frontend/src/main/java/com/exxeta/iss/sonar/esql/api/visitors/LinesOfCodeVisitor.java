package com.exxeta.iss.sonar.esql.api.visitors;

import java.util.List;
import java.util.Set;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

public class LinesOfCodeVisitor extends SubscriptionVisitor{

	  private Set<Integer> lines = Sets.newHashSet();

	  public int linesOfCode(Tree tree) {
	    lines.clear();
	    scanTree(tree);
	    return lines.size();
	  }

	  @Override
	  public List<Tree.Kind> nodesToVisit() {
		  return ImmutableList.of(Tree.Kind.TOKEN);
	  }

	  @Override
	  public void visitNode(Tree tree) {
	    if (!((InternalSyntaxToken) tree).isEOF()) {
	      lines.add(((InternalSyntaxToken)tree).line());
	    }
	  }
	}