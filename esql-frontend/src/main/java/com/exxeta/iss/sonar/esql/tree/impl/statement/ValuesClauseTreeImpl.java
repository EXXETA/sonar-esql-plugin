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
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ValuesClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ValuesClauseTreeImpl extends EsqlTree implements ValuesClauseTree {

	private InternalSyntaxToken identityKeyword;
	private PathElementTree identity;
	private InternalSyntaxToken typeKeyword;
	private ExpressionTree type;
	private InternalSyntaxToken namespaceKeyword;
	private ExpressionTree namespace;
	private InternalSyntaxToken nameKeyword;
	private ExpressionTree name;
	private InternalSyntaxToken valueKeyword;
	private ExpressionTree value;

	public ValuesClauseTreeImpl(InternalSyntaxToken identityKeyword, PathElementTree identity,
			InternalSyntaxToken typeKeyword, ExpressionTree type, InternalSyntaxToken namespaceKeyword,
			ExpressionTree namespace, InternalSyntaxToken nameKeyword, ExpressionTree name,
			InternalSyntaxToken valueKeyword, ExpressionTree value) {
		super();
		this.identityKeyword = identityKeyword;
		this.identity = identity;
		this.typeKeyword = typeKeyword;
		this.type = type;
		this.namespaceKeyword = namespaceKeyword;
		this.namespace = namespace;
		this.nameKeyword = nameKeyword;
		this.name = name;
		this.valueKeyword = valueKeyword;
		this.value = value;
	}

	@Override
	public InternalSyntaxToken identityKeyword() {
		return identityKeyword;
	}

	@Override
	public PathElementTree identity() {
		return identity;
	}

	@Override
	public InternalSyntaxToken typeKeyword() {
		return typeKeyword;
	}

	@Override
	public ExpressionTree type() {
		return type;
	}

	@Override
	public InternalSyntaxToken namespaceKeyword() {
		return namespaceKeyword;
	}

	@Override
	public ExpressionTree namespace() {
		return namespace;
	}

	@Override
	public InternalSyntaxToken nameKeyword() {
		return nameKeyword;
	}

	@Override
	public ExpressionTree name() {
		return name;
	}

	@Override
	public InternalSyntaxToken valueKeyword() {
		return valueKeyword;
	}

	@Override
	public ExpressionTree value() {
		return value;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitValuesClause(this);
	}

	@Override
	public Kind getKind() {
		return Kind.VALUES_CLAUSE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(identityKeyword, identity, typeKeyword, type, namespaceKeyword, namespace,
				nameKeyword, name, valueKeyword, value);
	}

}
