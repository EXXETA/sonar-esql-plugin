package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key = "TooManyParameters")
public class TooManyParametersCheck extends DoubleDispatchVisitorCheck {

	private static final int DEFAULT_MAXIMUM = 7;

	@RuleProperty(key = "max", description = "Maximum authorized number of parameters", defaultValue = ""
			+ DEFAULT_MAXIMUM)
	public int maximum = DEFAULT_MAXIMUM;

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {

		int size = tree.parameterList().size();
		if (size > maximum) {
			addIssue(tree, "Function has " + size + " parameters, which is greater than " + maximum + " authorized.");
		}
	}

	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		int size = tree.parameterList().size();
		if (size > maximum) {
			addIssue(tree, "Procedure has " + size + " parameters, which is greater than " + maximum + " authorized.");
		}
	}

}
