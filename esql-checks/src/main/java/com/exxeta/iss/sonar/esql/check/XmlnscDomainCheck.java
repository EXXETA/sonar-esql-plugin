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

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.google.common.collect.ImmutableList;

@Rule(key="XmlnscDomain")
public class XmlnscDomainCheck extends DoubleDispatchVisitorCheck{

	ImmutableList<String> rootElements = ImmutableList.of("Root", "IntputRoot", "OutputRoot");
	ImmutableList<String> wrongDomain = ImmutableList.of("XML", "XMLNS");
	
	
	@Override
	public void visitFieldReference(FieldReferenceTree tree) {
		super.visitFieldReference(tree);
		if (rootElements.contains(tree.pathElement().name().name().text())
				&& !tree.pathElements().isEmpty() 
				&& tree.pathElements().get(0)!=null
				&& tree.pathElements().get(0).name()!=null
				&& tree.pathElements().get(0).name().name()!=null){
			String domain = tree.pathElements().get(0).name().name().text();
			checkDomain(domain, tree.pathElements().get(0));
		}
	}
	
	@Override
	public void visitCreateStatement(CreateStatementTree tree) {
		super.visitCreateStatement(tree);
		if (tree.domainExpression()!=null && tree.domainExpression().is(Kind.STRING_LITERAL)){
			String domain = ((LiteralTree)tree.domainExpression()).value();
			domain = domain.substring(1, domain.length()-1);
			checkDomain(domain, tree.domainExpression());
		}
	}

	private void checkDomain(String domain, Tree tree) {
		if (wrongDomain.contains(domain)){
			addIssue(tree, "Use the XMLNSC domain instead of "+domain+".");
		}
	}
	
	
}
