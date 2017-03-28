package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

@Rule(key = "ElseIfWithoutElse")
public class ElseIfWithoutElseCheck extends DoubleDispatchVisitorCheck {

  private static final String MESSAGE = "Add the missing \"ELSE\" clause.";

  

@Override
public void visitIfStatement(IfStatementTree tree) {
    if (tree.elseClause()==null&&tree.elseifClauses().size()>0) {
    	ElseifClauseTree lastEleseIf = tree.elseifClauses().get(tree.elseifClauses().size()-1);
          addIssue(new PreciseIssue(this, new IssueLocation(lastEleseIf.elseifKeyword(), tree.ifKeyword(), MESSAGE)));

      }
	super.visitIfStatement(tree);
}

}
