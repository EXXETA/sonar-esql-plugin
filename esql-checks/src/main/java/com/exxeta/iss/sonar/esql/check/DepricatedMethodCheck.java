/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;


/**
 * This Java class is created to implement the logic to avoid the depricated methods.
 * @author Sapna Singh
 *
 */
@Rule(key = "DepricatedMethod")
public class DepricatedMethodCheck extends DoubleDispatchVisitorCheck {
	private static final String MESSAGE = "Depricated methods should not be used.";

	private List<String> Methods = new ArrayList<String>();


	private static final String DEFAULT_DEPRICATED_METHODS = "BITSTREAM";
	@RuleProperty(
			key = "DepricatedMethod",
			description = "Depricated methods should not be used.",
			defaultValue = "" + DEFAULT_DEPRICATED_METHODS)
	public String depricatedMethods = DEFAULT_DEPRICATED_METHODS;

	@Override
	public void visitProgram(ProgramTree tree) {
		super.visitProgram(tree);

		EsqlFile file = getContext().getEsqlFile();
		List<String> lines = CheckUtils.readLines(file);
		int i = 0;
		for (String line : lines) {
			i = i + 1;	
			String  thelines = line.toString();
			String upperCaseTheLine = thelines.toUpperCase();

			for (String depricatedMethod : Methods) {


				if(upperCaseTheLine.contains(depricatedMethod)  && splitByComma(depricatedMethods).contains(depricatedMethod)){

					addIssue(new LineIssue(this, i,   MESSAGE ));
				}

			}

		}
	}
	@Override
	public void visitCallExpression(CallExpressionTree tree) {
		if (tree.functionName() instanceof IdentifierTree) {
			Methods.add((((IdentifierTree)tree.functionName()).name()));
		}
		super.visitCallExpression(tree);
	}

	public static List splitByComma(String v){
		String[] functions = v.split("\\,");
		List<String> values = new ArrayList<String>(Arrays.asList(functions));
		return values;
	}


}

