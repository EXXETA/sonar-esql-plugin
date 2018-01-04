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
package com.exxeta.iss.sonar.esql.tree.impl.expression;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IsExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class IsExpressionTreeImpl extends EsqlTree implements IsExpressionTree, TypableTree {

	private ExpressionTree expression;
	private InternalSyntaxToken isKeyword;
	private InternalSyntaxToken notKeyword;
	private InternalSyntaxToken plusMinus;
	private InternalSyntaxToken with;
	
	private TypeSet types = TypeSet.emptyTypeSet();

	
	public IsExpressionTreeImpl(ExpressionTree expression, InternalSyntaxToken isKeyword,
			InternalSyntaxToken notKeyword, InternalSyntaxToken plusMinus, InternalSyntaxToken with) {
		super();
		this.expression = expression;
		this.isKeyword = isKeyword;
		this.notKeyword = notKeyword;
		this.plusMinus = plusMinus;
		this.with = with;
	}

	@Override
	public ExpressionTree expression() {
		return expression;
	}

	@Override
	public InternalSyntaxToken isKeyword() {
		return isKeyword;
	}
	
	@Override
	public InternalSyntaxToken notKeyword() {
		return notKeyword;
	}

	@Override
	public InternalSyntaxToken plusMinus() {
		return plusMinus;
	}

	@Override
	public InternalSyntaxToken with() {
		return with;
	}

	@Override
	public TypeSet types() {
		return types;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitIsExpression(this);

	}

	@Override
	public Kind getKind() {
		return Kind.IN_EXPRESSION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(expression, isKeyword, notKeyword, plusMinus, with);
	}

	@Override
	public void add(Type type) {
		types.add(type);
	}

}
