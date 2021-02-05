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
package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.WhenClauseExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class WhenClauseExpressionTreeImpl extends EsqlTree implements WhenClauseExpressionTree{
	private final InternalSyntaxToken whenKeyword; 
	private final ExpressionTree expression;
	private final InternalSyntaxToken thenKeyword; 
	private final ExpressionTree resultValue;
	public WhenClauseExpressionTreeImpl(InternalSyntaxToken whenKeyword, ExpressionTree expression,
			InternalSyntaxToken thenKeyword, ExpressionTree resultValue) {
		super();
		this.whenKeyword = whenKeyword;
		this.expression = expression;
		this.thenKeyword = thenKeyword;
		this.resultValue = resultValue;
	}
	public InternalSyntaxToken whenKeyword() {
		return whenKeyword;
	}
	public ExpressionTree expression() {
		return expression;
	}
	public InternalSyntaxToken thenKeyword() {
		return thenKeyword;
	}
	public ExpressionTree resultValue() {
		return resultValue;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitWhenClauseExpression(this);
	}
	@Override
	public Kind getKind() {
		return Kind.WHEN_CLAUSE_EXPRESSION;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(whenKeyword, expression, thenKeyword, resultValue);
	}

	
}
