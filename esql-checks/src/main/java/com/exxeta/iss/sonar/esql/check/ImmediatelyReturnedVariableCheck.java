package com.exxeta.iss.sonar.esql.check;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import org.sonar.check.Rule;

import javax.annotation.Nullable;
import java.util.List;

@Rule(key = "ImmediatelyReturnedVariable")
public class ImmediatelyReturnedVariableCheck extends DoubleDispatchVisitorCheck {
    private static final String MESSAGE = "Immediately return this expression instead of assigning it to the temporary variable \"%s\".";

    @Override
    public void visitStatements(StatementsTree tree) {
        List<StatementTree> statements = tree.statements();

        if (statements.size() > 1) {

            StatementTree lastButOneStatement = statements.get(statements.size() - 2);
            StatementTree lastStatement = statements.get(statements.size() - 1);

            if (lastButOneStatement.is(Kind.DECLARE_STATEMENT)) {
                checkStatements(((DeclareStatementTree) lastButOneStatement), lastStatement);
            }
        }

        super.visitStatements(tree);
    }

    private void checkStatements(DeclareStatementTree variableDeclaration, StatementTree lastStatement) {
        SeparatedList<IdentifierTree> variables = variableDeclaration.nameList();

        if (variables.size() == 1 && variableDeclaration.initialValueExpression() != null) {
            IdentifierTree identifier = variables.get(0);

            String name = identifier.name();

            if (returnsVariableInLastStatement(lastStatement, name)) {
                addIssue(variableDeclaration.initialValueExpression(), String.format(MESSAGE, name));

            }
        }
    }


    private static boolean returnsVariableInLastStatement(StatementTree lastStatement, String variableName) {
        if (lastStatement.is(Kind.RETURN_STATEMENT)) {
            ReturnStatementTree returnStatement = (ReturnStatementTree) lastStatement;

            return isVariable(returnStatement.expression(), variableName);
        }

        return false;
    }


    private static boolean isVariable(@Nullable ExpressionTree expressionTree, String variableName) {
        if (expressionTree != null && expressionTree.is(Kind.IDENTIFIER_REFERENCE)) {
            String thrownName = ((IdentifierTree) expressionTree).name();
            return thrownName.equals(variableName);
        }

        return false;
    }


}
