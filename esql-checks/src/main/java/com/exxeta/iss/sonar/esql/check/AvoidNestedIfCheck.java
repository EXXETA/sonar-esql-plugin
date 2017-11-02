/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.Issue;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;

/**
 * @author C50679 (sapna.singh@infosys.com)
 *
 */
@Rule(key="AvoidNestedIf")
public class AvoidNestedIfCheck extends  DoubleDispatchVisitorCheck {
	
	private static final String MESSAGE = "Avoid nested IF statements: use ELSEIF or CASE WHEN clauses to get quicker drop-out.";
	
	private Deque<SyntaxToken> ifNesting;

	private static final int MAXIMUM_NESTING_ALLOWED = 3;

	//public int maxNestingLevel = MAXIMUM_NESTING_ALLOWED;

	@Override
	public void visitIfStatement(IfStatementTree tree) {
		SyntaxToken ifKeyword = tree.ifKeyword();
		checkNestesdIf(ifKeyword);
		ifNesting.push(ifKeyword);
		super.visitIfStatement(tree);
		ifNesting.pop();
	}
	
	
	private void checkNestesdIf(Tree tree) {
	    int size = ifNesting.size();
	    if (size == MAXIMUM_NESTING_ALLOWED) {
    	PreciseIssue issue = new PreciseIssue(this, new IssueLocation(tree, tree, "This if has a nesting level of "
				+ (ifNesting.size()+1) + " which is higher than the maximum allowed " + MAXIMUM_NESTING_ALLOWED +  MESSAGE ));
    	
	      
	      for (SyntaxToken element : ifNesting) {
	    	  issue.secondary(element , "Nesting + 1");
	      }
	      addIssue(issue);
	    }
	  }

	@Override
	public List<Issue> scanFile(TreeVisitorContext context) {
		this.ifNesting = new ArrayDeque<>();
		return super.scanFile(context);
	}

}
	
	
	








