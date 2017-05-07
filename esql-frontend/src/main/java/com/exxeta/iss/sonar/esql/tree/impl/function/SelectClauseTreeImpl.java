/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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

import java.util.Collections;
import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedFieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.function.SelectClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class SelectClauseTreeImpl extends EsqlTree implements SelectClauseTree {

	private SeparatedList<AliasedExpressionTree> aliasedFieldReferenceList;
	private InternalSyntaxToken itemKeyword;
	private ExpressionTree itemExpression;
	private InternalSyntaxToken aggregationType;
	private InternalSyntaxToken openingParenthesis;
	private ExpressionTree aggregationExpression;
	private InternalSyntaxToken closingParenthesis;

	public SelectClauseTreeImpl(SeparatedList<AliasedExpressionTree> aliasedFieldReferenceList) {
		this.aliasedFieldReferenceList = aliasedFieldReferenceList;
	}

	public SelectClauseTreeImpl(InternalSyntaxToken itemKeyword, ExpressionTree itemExpression) {
		super();
		this.itemKeyword = itemKeyword;
		this.itemExpression = itemExpression;
		this.aliasedFieldReferenceList = new SeparatedList<>(Collections.emptyList(), Collections.emptyList());
	}

	public SelectClauseTreeImpl(InternalSyntaxToken aggregationType, InternalSyntaxToken openingParenthesis,
			ExpressionTree aggregationExpression, InternalSyntaxToken closingParenthesis) {
		super();
		this.aggregationType = aggregationType;
		this.openingParenthesis = openingParenthesis;
		this.aggregationExpression = aggregationExpression;
		this.closingParenthesis = closingParenthesis;
		this.aliasedFieldReferenceList = new SeparatedList<>(Collections.emptyList(), Collections.emptyList());
	}

	@Override
	public SeparatedList<AliasedExpressionTree> aliasedFieldReferenceList() {
		return aliasedFieldReferenceList;
	}

	@Override
	public InternalSyntaxToken itemKeyword() {
		return itemKeyword;
	}

	@Override
	public ExpressionTree itemExpression() {
		return itemExpression;
	}

	@Override
	public InternalSyntaxToken aggregationType() {
		return aggregationType;
	}

	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}

	@Override
	public ExpressionTree aggregationExpression() {
		return aggregationExpression;
	}

	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSelectClause(this);
	}

	@Override
	public Kind getKind() {
		return Kind.SELECT_CLAUSE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(
				aliasedFieldReferenceList.elementsAndSeparators(Functions.<AliasedExpressionTree>identity()),
				Iterators.forArray(itemKeyword, itemExpression, aggregationType, openingParenthesis,
						aggregationExpression, closingParenthesis));
	}

}
