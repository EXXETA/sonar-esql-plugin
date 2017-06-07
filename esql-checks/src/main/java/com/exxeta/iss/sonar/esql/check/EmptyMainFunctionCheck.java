package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="EmptyMainFunction")
public class EmptyMainFunctionCheck extends DoubleDispatchVisitorCheck{

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		if ("Main".equalsIgnoreCase(tree.identifier().name())
				&& tree.routineBody().statement().is(Kind.BEGIN_END_STATEMENT)){
				BeginEndStatementTree beginEnd = (BeginEndStatementTree) tree.routineBody().statement();
				StatementsTree statements = beginEnd.statements();
				if (statements.statements().size()==1 && statements.statements().get(0).is(Kind.RETURN_STATEMENT) ){
					ReturnStatementTree returnStatement = (ReturnStatementTree) statements.statements().get(0);
					if (returnStatement.expression().is(Kind.BOOLEAN_LITERAL)){
						addIssue(tree, "Remove this empty Main function, or fill it with code.");
					}
				}
		}
	}
	
}
