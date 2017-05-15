package com.exxeta.iss.sonar.esql.check;

import java.util.ArrayList;
import java.util.Iterator;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key = "IfConditionalAlwaysTrueOrFalse")
public class IfConditionalAlwaysTrueOrFalseCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Remove this \"IF\" statement.";

	@Override
	public void visitIfStatement(IfStatementTree tree) {
		checkTree(tree.condition());  

		super.visitIfStatement(tree);
	}

	private void checkTree(Tree tree) {
		if (tree.is(Kind.BOOLEAN_LITERAL)) {
			addIssue(tree, MESSAGE);
		} else {
			Iterator<Tree> iterator = tree.childrenIterator();
			ArrayList<Tree> list = new ArrayList<>();
			while (iterator.hasNext()){
				Tree child = iterator.next();
				if (child!=null)
					list.add(child);
			}
			if (list.size()==1){
				if (list.get(0).is(Kind.BOOLEAN_LITERAL)){
					addIssue(list.get(0), MESSAGE);
				} else {
					checkTree(list.get(0));
				}
			}
			
			
		}
	}

	@Override
	public void visitElseifClause(ElseifClauseTree tree) {
		checkTree (tree.condition()) ;

		super.visitElseifClause(tree);
	}

}