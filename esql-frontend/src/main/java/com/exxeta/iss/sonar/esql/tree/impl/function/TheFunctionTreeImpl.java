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
package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.TheFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class TheFunctionTreeImpl extends EsqlTree implements TheFunctionTree {

	private final InternalSyntaxToken theKeyword;

	private final InternalSyntaxToken openingParenthesis;

	private final ExpressionTree expression;

	private final InternalSyntaxToken closingParenthesis;

	public TheFunctionTreeImpl(InternalSyntaxToken theKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree expression, InternalSyntaxToken closingParenthesis) {
		super();
		this.theKeyword = theKeyword;
		this.openingParenthesis = openingParenthesis;
		this.expression = expression;
		this.closingParenthesis = closingParenthesis;
	}

	@Override
	public SyntaxToken theKeyword() {
		return theKeyword;
	}

	@Override
	public SyntaxToken openingParenthesis() {
		return openingParenthesis;
	}

	@Override
	public ExpressionTree expression() {
		return expression;
	}

	@Override
	public SyntaxToken closingParenthesis() {
		return closingParenthesis;
	}

	@Override
	public Kind getKind() {
		return Kind.THE_FUNCTION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {

		return Iterators.<Tree> forArray(theKeyword, openingParenthesis, expression, closingParenthesis);
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitTheFunction(this);
	}

}
