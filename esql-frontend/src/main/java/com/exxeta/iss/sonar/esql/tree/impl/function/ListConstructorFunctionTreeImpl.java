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
package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.ListConstructorFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class ListConstructorFunctionTreeImpl extends EsqlTree implements ListConstructorFunctionTree {
	private InternalSyntaxToken listKeyword;
	private InternalSyntaxToken openingCurlyBrace;
	private SeparatedList<ExpressionTree> aliasedExpressions;
	private InternalSyntaxToken closingCurlyBrace;


	public ListConstructorFunctionTreeImpl(InternalSyntaxToken listKeyword, InternalSyntaxToken openingCurlyBrace,
			SeparatedList<ExpressionTree> aliasedExpressions, InternalSyntaxToken closingCurlyBrace) {
		super();
		this.listKeyword = listKeyword;
		this.openingCurlyBrace = openingCurlyBrace;
		this.aliasedExpressions = aliasedExpressions;
		this.closingCurlyBrace = closingCurlyBrace;
	}

	@Override
	public InternalSyntaxToken listKeyword() {
		return listKeyword;
	}

	@Override
	public InternalSyntaxToken openingCurlyBrace() {
		return openingCurlyBrace;
	}

	@Override
	public SeparatedList<ExpressionTree> expressions() {
		return aliasedExpressions;
	}

	@Override
	public InternalSyntaxToken closingCurlyBrace() {
		return closingCurlyBrace;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitListConstructorFunction(this);
	}

	@Override
	public Kind getKind() {
		return Kind.LIST_CONSTRUCTOR_FUNCTION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(listKeyword, openingCurlyBrace),
				aliasedExpressions.elementsAndSeparators(Functions.<ExpressionTree>identity()),
				Iterators.singletonIterator(closingCurlyBrace));
	}

}
