package com.exxeta.iss.sonar.esql.check;

import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableList;

@Rule(key = "EmptyFile")
public class EmptyFileCheck extends SubscriptionVisitorCheck{

	public static final String MESSAGE = "File \"%s\" has 0 lines of code.";
	
	  @Override
	  public void visitNode(Tree tree) {
	    if (!((InternalSyntaxToken) tree).isEOF()) {
	      return;
	    }

	    SyntaxToken token = (SyntaxToken) tree;
	    int lines = token.line();

	    if (lines < 2) {
	      String fileName = getContext().getEsqlFile().fileName();
	      addIssue(new FileIssue(this, String.format(MESSAGE, fileName)));
	    }
	  }

	  @Override
	  public List<Tree.Kind> nodesToVisit() {
	    return ImmutableList.of(Tree.Kind.TOKEN);
	  }
}
