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

import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.SyntacticEquivalence;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.google.common.collect.ImmutableList;

@Rule(key = "XmlnscDomain")
public class XmlnscDomainCheck extends DoubleDispatchVisitorCheck {

	private static final ImmutableList<String> ROOT_ELEMENTS = ImmutableList.of("Root", "InputRoot", "OutputRoot");
	private static final ImmutableList<String> WRONG_DOMAINS = ImmutableList.of("XML", "XMLNS");
	private static final String MESSAGE = "Use the XMLNSC domain instead of %s.";

	@Override
	public void visitFieldReference(FieldReferenceTree tree) {
		super.visitFieldReference(tree);
		if (rootIs(tree, WRONG_DOMAINS)) {
			String domain = tree.pathElements().get(0).name().name().name();
			addIssue(tree, String.format(MESSAGE, domain));
		} else if (rootIs(tree, ROOT_ELEMENTS) && secondElementIs(tree, WRONG_DOMAINS)) {
			String domain = tree.pathElement().name().name().name();
			addIssue(tree, String.format(MESSAGE, domain));
		}
	}

	private boolean secondElementIs(FieldReferenceTree tree, List<String> names) {
		return !tree.pathElements().isEmpty() 
				&& tree.pathElements().get(0).name() != null
				&& tree.pathElements().get(0).name().name() != null
				&& names.contains(tree.pathElements().get(0).name().name().name());
	}

	private boolean rootIs(FieldReferenceTree tree, List<String> names) {
		return names.contains(tree.pathElement().name().name().name());
	}

	@Override
	public void visitCreateStatement(CreateStatementTree tree) {
		super.visitCreateStatement(tree);
		if (tree.domainExpression() != null
				&& SyntacticEquivalence.skipParentheses(tree.domainExpression()).is(Kind.STRING_LITERAL)) {
			String domain = ((LiteralTree) SyntacticEquivalence.skipParentheses(tree.domainExpression())).value();
			domain = domain.substring(1, domain.length() - 1);
			if (WRONG_DOMAINS.contains(domain)) {
				addIssue(tree, String.format(MESSAGE, domain));
			}
		}
	}


}
