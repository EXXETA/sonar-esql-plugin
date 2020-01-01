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

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.RowConstructorFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class RowConstructorFunctionTreeImpl extends EsqlTree implements RowConstructorFunctionTree {
	private InternalSyntaxToken rowKeyword;
	private InternalSyntaxToken openingParenthesis;
	private SeparatedList<AliasedExpressionTree> aliasedExpressions;
	private InternalSyntaxToken closingParenthesis;

	public RowConstructorFunctionTreeImpl(InternalSyntaxToken rowKeyword, InternalSyntaxToken openingParenthesis,
			SeparatedList<AliasedExpressionTree> aliasedExpressions, InternalSyntaxToken closingParenthesis) {
		super();
		this.rowKeyword = rowKeyword;
		this.openingParenthesis = openingParenthesis;
		this.aliasedExpressions = aliasedExpressions;
		this.closingParenthesis = closingParenthesis;
	}

	@Override
	public InternalSyntaxToken rowKeyword() {
		return rowKeyword;
	}

	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}

	@Override
	public SeparatedList<AliasedExpressionTree> aliasedExpressions() {
		return aliasedExpressions;
	}

	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitRowConstructorFunction(this);
	}

	@Override
	public Kind getKind() {
		return Kind.ROW_CONSTRUCTOR_FUNCTION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(rowKeyword, openingParenthesis),
				aliasedExpressions.elementsAndSeparators(Functions.<AliasedExpressionTree>identity()),
				Iterators.singletonIterator(closingParenthesis));
	}

}
