package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhileStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.expression.CallExpressionTreeImpl;

@Rule(key="CardinalityInLoop")
public class CardinalityInLoopCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Avoid using CARDINALITY in loops.";

	@Override
	public void visitWhileStatement(WhileStatementTree tree) {
		checkCardinalityInDecendants(tree);
		super.visitWhileStatement(tree);
	}

	@Override
	public void visitRepeatStatement(RepeatStatementTree tree) {
		checkCardinalityInDecendants(tree);
		super.visitRepeatStatement(tree);
	}

	@Override
	public void visitLoopStatement(LoopStatementTree tree) {
		checkCardinalityInDecendants(tree);
		super.visitLoopStatement(tree);
	}

	
	private void checkCardinalityInDecendants(Tree tree) {
		 tree.descendants().filter(d -> d.is(Kind.CALL_EXPRESSION))
				.filter(d -> (((CallExpressionTreeImpl) d).functionName().pathElement().name().name().text())
						.equalsIgnoreCase("CARDINALITY")).forEach(d->addIssue(d, MESSAGE));
	}


}
