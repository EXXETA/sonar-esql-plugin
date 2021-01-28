/**
 * This java class is created to implement the logic for checking if braces for conditions are used or not, 
 * if it is not used then it should be inserted.
 * 
 * 
 * @author Prerana Agarkar
 *
 */

package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhileStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key ="ConditionParenthesis")
public class ConditionParenthesisCheck extends DoubleDispatchVisitorCheck  {
	public static final String MESSAGE = "Use parenthesis for conditions as it gives more readability to code.";

	@Override
	public void visitIfStatement(IfStatementTree tree) 
	{
		checkTree(tree.condition()); 
	    super.visitIfStatement(tree);
	}
	@Override
	public void visitElseifClause(ElseifClauseTree tree) {
		checkTree(tree.condition()); 
	    super.visitElseifClause(tree);
	}
	@Override
	public void visitWhileStatement(WhileStatementTree tree) {
		checkTree(tree.condition()); 
	    super.visitWhileStatement(tree);
	}
	
	private void checkTree(Tree tree)
	{
		if (!(tree.is(Kind.PARENTHESISED_EXPRESSION))) 
		{
			addIssue(tree, MESSAGE);
		}
	}		                	
}		

			


	
		

	

