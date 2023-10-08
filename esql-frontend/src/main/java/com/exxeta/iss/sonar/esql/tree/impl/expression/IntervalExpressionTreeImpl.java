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
package com.exxeta.iss.sonar.esql.tree.impl.expression;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IntervalExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.IntervalQualifierTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class IntervalExpressionTreeImpl extends EsqlTree implements IntervalExpressionTree, TypableTree {

	private InternalSyntaxToken openParenToken;
	private ExpressionTree additiveExpression;
	private InternalSyntaxToken closeParenToken;
	private IntervalQualifierTreeImpl intervalQualifier;
	private TypeSet types = TypeSet.emptyTypeSet();

	public IntervalExpressionTreeImpl(InternalSyntaxToken openParenToken, ExpressionTree additiveExpression,
			InternalSyntaxToken closeParenToken, IntervalQualifierTreeImpl intervalQualifier) {
		super();
		this.openParenToken = openParenToken;
		this.additiveExpression = additiveExpression;
		this.closeParenToken = closeParenToken;
		this.intervalQualifier = intervalQualifier;
	}

	@Override
	public InternalSyntaxToken openParenToken() {
		return openParenToken;
	}

	@Override
	public ExpressionTree additiveExpression() {
		return additiveExpression;
	}

	@Override
	public InternalSyntaxToken closeParenToken() {
		return closeParenToken;
	}

	@Override
	public IntervalQualifierTreeImpl intervalQualifier() {
		return intervalQualifier;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(openParenToken, additiveExpression, closeParenToken, intervalQualifier);
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitIntervalExpression(this);
	}

	@Override
	public Kind getKind() {
		return Kind.INTERVAL_EXPRESSION;
	}

	@Override
	public TypeSet types() {
		return types.immutableCopy();
	}

	@Override
	public void add(Type type) {
		types.add(type);
	}
}
