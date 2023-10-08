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
import com.exxeta.iss.sonar.esql.api.tree.expression.BetweenExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class BetweenExpressionTreeImpl extends EsqlTree implements BetweenExpressionTree, TypableTree {

	private ExpressionTree expression;
	private InternalSyntaxToken notKeyword;
	private InternalSyntaxToken betweenKeyword;
	private InternalSyntaxToken symmetricKeyword;
	private ExpressionTree endpoint1;
	private InternalSyntaxToken andKeyword;
	private ExpressionTree endpoint2;

	private TypeSet types = TypeSet.emptyTypeSet();

	public BetweenExpressionTreeImpl(ExpressionTree expression, InternalSyntaxToken notKeyword,
			InternalSyntaxToken betweenKeyword, InternalSyntaxToken symmetricKeyword, ExpressionTree endpoint1,
			InternalSyntaxToken andKeyword, ExpressionTree endpoint2) {
		super();
		this.expression = expression;
		this.notKeyword = notKeyword;
		this.betweenKeyword = betweenKeyword;
		this.symmetricKeyword = symmetricKeyword;
		this.endpoint1 = endpoint1;
		this.andKeyword = andKeyword;
		this.endpoint2 = endpoint2;
	}


	public ExpressionTree expression() {
		return expression;
	}

	public InternalSyntaxToken notKeyword() {
		return notKeyword;
	}

	public InternalSyntaxToken betweenKeyword() {
		return betweenKeyword;
	}

	public InternalSyntaxToken symmetricKeyword() {
		return symmetricKeyword;
	}

	public ExpressionTree endpoint1() {
		return endpoint1;
	}

	public InternalSyntaxToken andKeyword() {
		return andKeyword;
	}

	public ExpressionTree endpoint2() {
		return endpoint2;
	}

	@Override
	public TypeSet types() {
		return types;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitBetweenExpression(this);

	}

	@Override
	public Kind getKind() {
		return Kind.BETWEEN_EXPRESSION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(expression, notKeyword, betweenKeyword, symmetricKeyword, endpoint1, andKeyword, endpoint2);
	}

	@Override
	public void add(Type type) {
		types.add(type);
	}

}
