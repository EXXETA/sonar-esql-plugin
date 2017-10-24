
package com.exxeta.iss.sonar.esql.check;



import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

@Rule(key = FunctionNameStartsWithUppercaseCheck.CHECK_KEY, priority = Priority.MAJOR, name = "Function name should start with uppercase", tags = Tags.CONVENTION)
public class FunctionNameStartsWithUppercaseCheck extends DoubleDispatchVisitorCheck {
	
	public static final String CHECK_KEY = "FunctionNameStartsWithUppercase";

	
	@RuleProperty(key="ignoreMain", description = "ignore Main function", defaultValue="TRUE", type="BOOLEAN")
	public boolean ignoreMain = true;

	public FunctionNameStartsWithUppercaseCheck() {
	}

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		super.visitCreateFunctionStatement(tree);
		if (!Character.isUpperCase(tree.identifier().name().charAt(0))) {
			if (!ignoreMain || !"Main".equalsIgnoreCase(tree.identifier().name()))
			addIssue(
					new PreciseIssue(this, new IssueLocation(tree.identifier(), tree.identifier(), "Rename function \""
							+ tree.identifier().name() + "\". Function name should start with Uppercase.")));

		}
	}
	
}

