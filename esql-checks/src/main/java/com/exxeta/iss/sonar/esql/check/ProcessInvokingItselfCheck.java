/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CallStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;

/**
 * This Java class is created to implement the logic to check whether process is
 * invoking itself.
 * 
 * @author C50679
 *
 */
@Rule(key = "ProcessInvokingItself")
public class ProcessInvokingItselfCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "process invoking itself.";

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {

		String functionName = tree.identifier().name();

		BeginEndStatementTree beginEnd = (BeginEndStatementTree) tree.routineBody().statement();
		StatementsTree statements = beginEnd.statements();
		int i = statements.statements().size();

		for (int j = 0; j < i; j++) {

			StatementTree stmt = statements.statements().get(j);
			Stream<Tree> tokens = stmt.childrenStream();
			List<Tree> tokenList = tokens.collect(Collectors.toList());

			for (Tree token : tokenList) {

				if (token != null && token.is(Kind.CALL_EXPRESSION)) {
					CallExpressionTree callexp = (CallExpressionTree) token;
					IdentifierTree identifier = (IdentifierTree) callexp.functionName();
					if (identifier != null) {
						String calledFunction = identifier.name();

						if (functionName.equalsIgnoreCase(calledFunction)) {
							addIssue(new LineIssue(this, stmt, MESSAGE));
						}
					}
				}
			}
		}
	}

	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {

		String ProcedureName = tree.identifier().name();

		BeginEndStatementTree beginEnd = (BeginEndStatementTree) tree.routineBody().statement();
		StatementsTree statements = beginEnd.statements();
		int i = statements.statements().size();

		for (int j = 0; j < i; j++) {

			StatementTree stmt = statements.statements().get(j);
			Stream<Tree> tokens = stmt.childrenStream();
			List<Tree> tokenList = tokens.collect(Collectors.toList());
			
			for (Tree token : tokenList) {

				if (token != null && token.is(Kind.CALL_STATEMENT)) {
					CallStatementTree callstmt = (CallStatementTree) token;
					String calledProcedure = callstmt.routineName().text();

					if (ProcedureName.equalsIgnoreCase(calledProcedure)) {
						addIssue(new LineIssue(this, stmt, MESSAGE));
					}
				} else if (token != null && token.is(Kind.CALL_EXPRESSION)) {
					CallExpressionTree callstmt = (CallExpressionTree) token;
					String calledProcedure = ((IdentifierTree) callstmt.functionName()).name();

					if (ProcedureName.equalsIgnoreCase(calledProcedure)) {
						addIssue(new LineIssue(this, stmt, MESSAGE));
					}

				}

			}
		}

	}
}
