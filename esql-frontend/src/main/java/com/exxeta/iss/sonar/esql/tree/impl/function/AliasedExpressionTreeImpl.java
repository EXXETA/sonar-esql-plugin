/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class AliasedExpressionTreeImpl extends EsqlTree implements AliasedExpressionTree{
	private ExpressionTree expression;
	private InternalSyntaxToken asKeyword;
	private FieldReferenceTreeImpl alias;
	public AliasedExpressionTreeImpl(ExpressionTree expression, InternalSyntaxToken asKeyword,
			FieldReferenceTreeImpl alias) {
		super();
		this.expression = expression;
		this.asKeyword = asKeyword;
		this.alias = alias;
	}
	@Override
	public ExpressionTree expression() {
		return expression;
	}
	@Override
	public InternalSyntaxToken asKeyword() {
		return asKeyword;
	}
	@Override
	public FieldReferenceTreeImpl alias() {
		return alias;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitAliasedExpression(this);
	}
	@Override
	public Kind getKind() {
		return Kind.ALIASED_EXPRESSION;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(expression, asKeyword, alias);
	}
	
	
	
	
}
