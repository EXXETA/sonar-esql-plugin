package com.exxeta.iss.sonar.esql.check;



import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

@Rule(key="VariableNameStartsWithLowercaseCheck")

public class VariableNameStartsWithLowercaseCheck extends DoubleDispatchVisitorCheck {
public static final String CHECK_KEY = "VariableNameStartsWithLowercaseCheck";

	public VariableNameStartsWithLowercaseCheck() {
	}
	@Override
	public void visitDeclareStatement(DeclareStatementTree tree) {
		super.visitDeclareStatement(tree);

		boolean isConstant = (tree.constantKeyword() != null) || (tree.sharedExt()!=null && "EXTERNAL".equalsIgnoreCase(tree.sharedExt().text()));

		if (!isConstant) {
			for (int i = 0; i < tree.nameList().size(); i++) {
				if(!Character.isLowerCase(tree.nameList().get(i).name().charAt(0))){
					addIssue(new PreciseIssue(this,
							new IssueLocation(tree.nameList().get(i), tree.nameList().get(i),
									"Rename variable \"" + tree.nameList().get(i).name()
											+ "\". Variable name should start with lowercase.")));

				}
			}
		}
	}



	

}

 