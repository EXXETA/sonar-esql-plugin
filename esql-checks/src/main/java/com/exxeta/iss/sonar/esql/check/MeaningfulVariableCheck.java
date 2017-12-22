/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.regex.Pattern;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

/**
 * This java class is created to implement the logic to check that variable names should be meaningful. 
 * Variable name should consist of atleast 3 charachters.
 * @author Sapna Singh
 *
 */
@Rule(key = MeaningfulVariableCheck.CHECK_KEY)
public class MeaningfulVariableCheck extends DoubleDispatchVisitorCheck {
	public static final String CHECK_KEY = "MeaningfulVariable";
	
	private static final String DEFAULT_FORMAT = "^[a-z][a-zA-Z0-9]{3,30}$";
	
	@RuleProperty(key = "format", 
			description="regular expression",
			defaultValue = "" + DEFAULT_FORMAT)
	public String format = DEFAULT_FORMAT;

	private Pattern pattern;

	public MeaningfulVariableCheck() {
		pattern = Pattern.compile(getFormat());
	}
	public String getFormat() {
		return format;
	}
	@Override
	public void visitDeclareStatement(DeclareStatementTree tree) {
		super.visitDeclareStatement(tree);

		boolean isConstant = (tree.constantKeyword() != null) || (tree.sharedExt()!=null && "EXTERNAL".equalsIgnoreCase(tree.sharedExt().text()));

		if (!isConstant) {
			for (int i = 0; i < tree.nameList().size(); i++) {
				if (!pattern.matcher(tree.nameList().get(i).name()).matches()) {
					addIssue(new PreciseIssue(this,
							new IssueLocation(tree.nameList().get(i), tree.nameList().get(i),
									"Rename variable \"" + tree.nameList().get(i).name()
											+ "\" to match the regular expression " + format + ".")));

				}
			}
		}
	}

}
