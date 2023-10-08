/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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

import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.google.common.collect.ImmutableSet;

@Rule(key = NonReservedKeywordCheck.CHECK_KEY)
public class NonReservedKeywordCheck extends DoubleDispatchVisitorCheck {
	public static final String CHECK_KEY = "NonReservedKeyword";
	private static final Set<String> nonReservedKeywords = ImmutableSet.copyOf(EsqlNonReservedKeyword.keywordValues());
	public static final String MESSAGE = "ESQL keyword \"%s\" should not be used as an identifier.";

	@Override
	public void visitDeclareStatement(DeclareStatementTree tree) {
		for (int i=0;i<tree.nameList().size();i++){
			IdentifierTree variableName = tree.nameList().get(i);
			if (nonReservedKeywords.contains(variableName.name())){
				addIssue(variableName, String.format(MESSAGE, variableName.name()));
			}
		}
		super.visitDeclareStatement(tree);
		
	}

}
