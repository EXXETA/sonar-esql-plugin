/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;

/**
 * This java class is created to implement the logic to check the PassThru
 * Statement. When PASSTHRU statement is used in ESQL,Use parameter markers '?'
 * 
 * @author Sapna Singh
 *
 */

@Rule(key = "PassThruStatement")
public class PassThruStatementCheck extends AbstractPassthruCheck {

	private static final String MESSAGE = "Use parameter markers '?' when using the PASSTHRU statement in ESQL";
	
	@Override
	protected void checkLiterals(Tree tree, List<LiteralTree> literals) {
		
		StringBuilder query =new StringBuilder();
		for (LiteralTree literal:literals){
			query.append(literal.value());
		}

		boolean isQueryWhereComplient = false;
		String queryString = query.toString().toUpperCase();
		if (queryString.trim().contains("WHERE")) {
			String whereClause = queryString.substring(queryString.indexOf("WHERE"));
			whereClause = CheckUtils.removeQuotedContent(whereClause);
			whereClause = whereClause.replace(" ", "");

			if (whereClause.contains("GROUPBY")) {
				whereClause = whereClause.substring(0, whereClause.indexOf("GROUPBY"));
			} else if (whereClause.contains("ORDERBY")) {
				whereClause = whereClause.substring(0, whereClause.indexOf("ORDERBY"));
			}

			if (StringUtils.countMatches(whereClause, "=") != StringUtils.countMatches(whereClause, "?")) {
				isQueryWhereComplient = false;
			} else {
				isQueryWhereComplient = true;
			}

		} else {
			isQueryWhereComplient = true;
		}
		if (!isQueryWhereComplient){
			addIssue(tree, MESSAGE);
		}

		
	}

}
