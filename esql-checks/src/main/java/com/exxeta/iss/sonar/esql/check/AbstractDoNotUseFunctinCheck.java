package com.exxeta.iss.sonar.esql.check;

import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

public abstract class AbstractDoNotUseFunctinCheck extends DoubleDispatchVisitorCheck{

	public abstract String getMessage();
	public abstract String getFunctionName();
	
	@Override
	public void visitCallExpression(CallExpressionTree tree) {
		if (tree.functionName() instanceof IdentifierTree 
			&& getFunctionName().equalsIgnoreCase(((IdentifierTree)tree.functionName()).name())){
			addIssue(tree.functionName(), getMessage());
		}
		super.visitCallExpression(tree);
	}
	
}
