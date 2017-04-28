/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.NameClausesTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.PathElementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class NameClausesTreeImpl extends EsqlTree implements NameClausesTree {
	private InternalSyntaxToken typeKeyword;
	private ExpressionTree typeExpression;;
	private InternalSyntaxToken namespaceKeyword;
	private ExpressionTree namespaceExpression;;
	private InternalSyntaxToken namespaceStar;
	private InternalSyntaxToken nameKeyword;
	private ExpressionTree nameExpression;;
	private InternalSyntaxToken identityKeyword;
	private PathElementTreeImpl pathElement;
	private InternalSyntaxToken repeatKeyword;
	private InternalSyntaxToken repeatTypeKeyword;
	private InternalSyntaxToken repeatNameKeyword;

	public NameClausesTreeImpl(InternalSyntaxToken typeKeyword, ExpressionTree typeExpression,
			InternalSyntaxToken namespaceKeyword, ExpressionTree namespaceExpression, InternalSyntaxToken namespaceStar,
			InternalSyntaxToken nameKeyword, ExpressionTree nameExpression, InternalSyntaxToken identityKeyword,
			PathElementTreeImpl pathElement, InternalSyntaxToken repeatKeyword, InternalSyntaxToken repeatTypeKeyword,
			InternalSyntaxToken repeatNameKeyword) {
		super();
		this.typeKeyword = typeKeyword;
		this.typeExpression = typeExpression;
		this.namespaceKeyword = namespaceKeyword;
		this.namespaceExpression = namespaceExpression;
		this.namespaceStar = namespaceStar;
		this.nameKeyword = nameKeyword;
		this.nameExpression = nameExpression;
		this.identityKeyword = identityKeyword;
		this.pathElement = pathElement;
		this.repeatKeyword = repeatKeyword;
		this.repeatTypeKeyword = repeatTypeKeyword;
		this.repeatNameKeyword = repeatNameKeyword;
	}

	@Override
	public InternalSyntaxToken typeKeyword() {
		return typeKeyword;
	}

	@Override
	public ExpressionTree typeExpression() {
		return typeExpression;
	}

	@Override
	public InternalSyntaxToken namespaceKeyword() {
		return namespaceKeyword;
	}

	@Override
	public ExpressionTree namespaceExpression() {
		return namespaceExpression;
	}

	@Override
	public InternalSyntaxToken namespaceStar() {
		return namespaceStar;
	}

	@Override
	public InternalSyntaxToken nameKeyword() {
		return nameKeyword;
	}

	@Override
	public ExpressionTree nameExpression() {
		return nameExpression;
	}

	@Override
	public InternalSyntaxToken identityKeyword() {
		return identityKeyword;
	}

	@Override
	public PathElementTreeImpl pathElement() {
		return pathElement;
	}

	@Override
	public InternalSyntaxToken repeatKeyword() {
		return repeatKeyword;
	}

	@Override
	public InternalSyntaxToken repeatTypeKeyword() {
		return repeatTypeKeyword;
	}

	@Override
	public InternalSyntaxToken repeatNameKeyword() {
		return repeatNameKeyword;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitNameClauses(this);
	}

	@Override
	public Kind getKind() {
		return Kind.NAME_CLAUSES;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(typeKeyword, typeExpression, namespaceKeyword, nameExpression, namespaceStar,
				nameKeyword, nameExpression, identityKeyword, pathElement, repeatKeyword, repeatTypeKeyword,
				repeatNameKeyword);
	}

}