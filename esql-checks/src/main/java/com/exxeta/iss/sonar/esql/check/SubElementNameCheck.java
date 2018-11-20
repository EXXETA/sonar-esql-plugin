/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *	 http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.check;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

/**
 * This java class is created to implement the logic to check sub-elements
 * should be in UpperCamel-case and elements containing simple value should be
 * in lowercase.
 * 
 * @author sapna singh
 *
 */
@Rule(key = "SubElementName")
public class SubElementNameCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "sub-elements should be in UpperCamel-case and elements containing simple value should be in lowercase.";

	private static final String UPPERCASE_FORMAT = "^[A-Z][a-zA-Z0-9]*$";
	private static final String LOWERCASE_FORMAT = "^[a-z][a-zA-Z0-9]*$";

	@Override
	public void visitSetStatement(SetStatementTree tree) {
		if (tree.variableReference() instanceof FieldReferenceTree) {
			FieldReferenceTree fieldRef = (FieldReferenceTree) tree.variableReference();
			if ("Environment".equalsIgnoreCase(fieldRef.pathElement().name().name().name())) {
				int subElementsSize = fieldRef.pathElements().size();
				if (subElementsSize > 0) {
					for (int i = 0; i < subElementsSize - 1; i++) {
						String subElement = fieldRef.pathElements().get(i).name().name().name();
						if (!subElement.matches(UPPERCASE_FORMAT)) {
							addIssue(tree, MESSAGE);
						}
					}
					String lastElement = fieldRef.pathElements().get(subElementsSize - 1).name().name().name();
					if (!lastElement.matches(LOWERCASE_FORMAT)) {
						addIssue(tree, MESSAGE);
					}
				}

			}
		}
	}
}