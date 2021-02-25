/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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

import java.util.Iterator;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.PassthruFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.SelectFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;

@Rule(key = "SelectAll")
public class SelectAllCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Specify the needed fields.";
	private PassthruFunctionTree passthruTree = null;

	@Override
	public void visitSelectFunction(SelectFunctionTree tree) {
		for (AliasedExpressionTree aliased : tree.selectClause().aliasedFieldReferenceList()) {
			if (aliased.expression() != null && aliased.expression().is(Kind.FIELD_REFERENCE)) {
				FieldReferenceTree fieldReference = (FieldReferenceTree) aliased.expression();
				if (fieldReference.pathElement().name().star()!=null) {
					addIssue(fieldReference, MESSAGE);
				}
				Iterator<PathElementTree> iter = fieldReference.pathElements().iterator();
				while (iter.hasNext()) {
					PathElementTree element = iter.next();
					if (element.name().star()!=null) {
						addIssue(fieldReference, MESSAGE);
					}
				}
			}
		}
	}

	@Override
	public void visitPassthruFunction(PassthruFunctionTree tree) {
		this.passthruTree =tree;
		super.visitPassthruFunction(tree);
		this.passthruTree=null;
	}
	
	@Override
	public void visitLiteral(LiteralTree tree) {
		if (this.passthruTree!=null && tree.is(Kind.STRING_LITERAL)) {
			checkSqlString(tree.value(),
					passthruTree);
		}

		super.visitLiteral(tree);
	}

	/*
	 * Very simple implementation to check if there are any * in the sql
	 * statement.
	 */
	private void checkSqlString(String sqlStatement, PassthruFunctionTree expression) {
		String unqotedStatement = CheckUtils.removeQuotedContent(sqlStatement.substring(1, sqlStatement.length() - 1))
				.trim();
		if (unqotedStatement.startsWith("SELECT") && unqotedStatement.contains("*")) {
			addIssue(expression, MESSAGE);

		}
	}

}
