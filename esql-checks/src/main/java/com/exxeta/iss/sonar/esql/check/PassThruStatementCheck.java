package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PassthruStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="PassThruStatement")
public class PassThruStatementCheck extends  DoubleDispatchVisitorCheck {
	
	private static final String MESSAGE = "Use parameter markers '?' when using the PASSTHRU statement in ESQL";
	
	
	@Override
	public void visitPassthruStatement(PassthruStatementTree tree){
		if (!checkQuery(tree)){
			addIssue(tree,MESSAGE);
			
		}
	}
		
		
		public static boolean checkQuery(PassthruStatementTree exp){
			boolean isQueryWhereComplient = false;
			ExpressionTree query = exp.expression();
			String queryString = query.toString().toUpperCase();
			if (queryString.trim().contains("WHERE")){
				String whereClause = queryString.substring(queryString.indexOf("WHERE"));
				whereClause = CheckUtils.removeQuotedContent(whereClause);
				whereClause = whereClause.replaceAll(" ", "");
				
				if(whereClause.contains("GROUPBY")){
					whereClause = whereClause.substring(0,whereClause.indexOf("GROUPBY"));
				}else if(whereClause.contains("ORDERBY")){
					whereClause = whereClause.substring(0,whereClause.indexOf("ORDERBY"));
				}
				
				if(CheckUtils.countCharacters(whereClause, "=")!=CheckUtils.countCharacters(whereClause, "\\?")){
					isQueryWhereComplient = false;
				}else{
					isQueryWhereComplient = true;
				}
			}else{
				isQueryWhereComplient = true;
			}
			return isQueryWhereComplient;
		
	}
	

}
