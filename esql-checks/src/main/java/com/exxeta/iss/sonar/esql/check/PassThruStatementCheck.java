/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PassthruStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

/**
 * This java class is created to implement the logic to check the PassThru Statement.
 * When PASSTHRU statement is used in ESQL,Use parameter markers '?'
 * @author Sapna Singh
 *
 */

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
