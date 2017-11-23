/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.function.CaseFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

/**
 * This java class is created to check the case statement, if CASE statement contains single when , then it should be replaced with If statement.
 * @author sapna singh
 *
 */
@Rule(key = "CaseStatementWithSingleWhen")
public class CaseStatementWithSingleWhenCheck extends DoubleDispatchVisitorCheck {
	
	private static final String MESSAGE = "Replace this \"CASE\" statement by \"if\" statements to increase readability";
	
	@Override
	public void visitCaseStatement(CaseStatementTree tree) {
		if ((tree.whenClauses().size())==1){
			
			addIssue(new PreciseIssue(this, new IssueLocation(tree,   MESSAGE )));
		}
	}
}
	
	







