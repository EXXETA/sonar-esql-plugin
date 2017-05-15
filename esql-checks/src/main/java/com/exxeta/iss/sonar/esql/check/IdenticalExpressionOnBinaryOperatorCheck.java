package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.SyntacticEquivalence;

@Rule(key="IdenticalExpressionOnBinaryOperator")
public class IdenticalExpressionOnBinaryOperatorCheck extends DoubleDispatchVisitorCheck{

	 private static final String MESSAGE = "Correct one of the identical sub-expressions on both sides of operator \"%s\"";
	 
	 @Override
	  public void visitBinaryExpression(BinaryExpressionTree tree) {
	    if (!tree.is(Kind.MULTIPLY, Kind.PLUS)
	      && SyntacticEquivalence.areEquivalent(tree.leftOperand(), tree.rightOperand()) ) {

	      String message = String.format(MESSAGE, tree.operator().text());
	      addIssue(tree.rightOperand(), message)
	        .secondary(tree.leftOperand());
	    }

	    super.visitBinaryExpression(tree);
	  }
	
}
