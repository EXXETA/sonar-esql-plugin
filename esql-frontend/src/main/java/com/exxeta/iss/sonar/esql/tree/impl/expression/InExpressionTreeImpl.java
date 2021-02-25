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
package com.exxeta.iss.sonar.esql.tree.impl.expression;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.InExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ParameterListTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class InExpressionTreeImpl extends EsqlTree implements InExpressionTree, TypableTree {

	private ExpressionTree expression;
	private InternalSyntaxToken notKeyword;
	private InternalSyntaxToken inKeyword;
	private ParameterListTreeImpl argumentList;

	private TypeSet types = TypeSet.emptyTypeSet();

	public InExpressionTreeImpl(ExpressionTree expression, InternalSyntaxToken notKeyword, 
			InternalSyntaxToken inKeyword, ParameterListTreeImpl argumentList) {
		super();
		this.expression = expression;
		this.notKeyword = notKeyword;
		this.inKeyword = inKeyword;
		this.argumentList = argumentList;
	}

	@Override
	public ExpressionTree expression() {
		return expression;
	}

	@Override
	public InternalSyntaxToken notKeyword() {
		return notKeyword;
	}

	@Override
	public InternalSyntaxToken inKeyword() {
		return inKeyword;
	}

	@Override
	public ParameterListTreeImpl argumentList() {
		return argumentList;
	}

	@Override
	public TypeSet types() {
		return types;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitInExpression(this);

	}

	@Override
	public Kind getKind() {
		return Kind.IN_EXPRESSION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(expression, notKeyword, inKeyword, argumentList);
	}

	@Override
	public void add(Type type) {
		types.add(type);
	}

}
