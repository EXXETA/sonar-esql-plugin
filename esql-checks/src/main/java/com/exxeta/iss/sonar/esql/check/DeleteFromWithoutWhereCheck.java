/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteFromStatementTree;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;

@Rule(key = "DeleteFromWithoutWhere")
public class DeleteFromWithoutWhereCheck extends AbstractPassthruCheck {

	private static final String MESSAGE = "Add a where caluse to this DELETE FROM statement.";

	@Override
	public void visitDeleteFromStatement(DeleteFromStatementTree tree) {
		if (tree.whereExpression() == null) {
			addIssue(tree, MESSAGE);
		}
	}

	@Override
	protected void checkLiterals(Tree tree, List<LiteralTree> literals) {
		if (!literals.isEmpty() && literals.get(0).value().toUpperCase().matches("'.*DELETE\\s+FROM.*")) {
			boolean hasWhereClause = false;
			for (LiteralTree literal : literals) {
				if (literal.value().toUpperCase().matches(".*WHERE.*")) {
					hasWhereClause = true;
				}

			}
			if (!hasWhereClause) {
				addIssue(tree, MESSAGE);
			}
		}
	}

}
