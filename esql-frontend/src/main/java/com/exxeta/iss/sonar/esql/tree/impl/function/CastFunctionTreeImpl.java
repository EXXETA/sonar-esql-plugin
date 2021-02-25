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
import com.exxeta.iss.sonar.esql.api.tree.function.CastFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.DataTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;

public class CastFunctionTreeImpl extends EsqlTree implements CastFunctionTree {
	private InternalSyntaxToken castKeyword;
	private InternalSyntaxToken openingParenthesis;
	private SeparatedList<ExpressionTree> sourceExpressions;
	private InternalSyntaxToken asKeyword;
	private DataTypeTreeImpl dataType;
	private InternalSyntaxToken ccsidKeyword;
	private ExpressionTree ccsidExpression;
	private InternalSyntaxToken encodingKeyword;
	private ExpressionTree encodingExpression;
	private InternalSyntaxToken formatKeyword;
	private ExpressionTree formatExpression;
	private InternalSyntaxToken defaultKeyword;
	private ExpressionTree defaultExpression;
	private InternalSyntaxToken closingParenthesis;

	public CastFunctionTreeImpl(InternalSyntaxToken castKeyword, InternalSyntaxToken openingParenthesis,
			SeparatedList<ExpressionTree> sourceExpressions, InternalSyntaxToken asKeyword, DataTypeTreeImpl dataType,
			InternalSyntaxToken ccsidKeyword, ExpressionTree ccsidExpression, InternalSyntaxToken encodingKeyword,
			ExpressionTree encodingExpression, InternalSyntaxToken formatKeyword, ExpressionTree formatExpression,
			InternalSyntaxToken defaultKeyword, ExpressionTree defaultExpression,
			InternalSyntaxToken closingParenthesis) {
		super();
		this.castKeyword = castKeyword;
		this.openingParenthesis = openingParenthesis;
		this.sourceExpressions = sourceExpressions;
		this.asKeyword = asKeyword;
		this.dataType = dataType;
		this.ccsidKeyword = ccsidKeyword;
		this.ccsidExpression = ccsidExpression;
		this.encodingKeyword = encodingKeyword;
		this.encodingExpression = encodingExpression;
		this.formatKeyword = formatKeyword;
		this.formatExpression = formatExpression;
		this.defaultKeyword = defaultKeyword;
		this.defaultExpression = defaultExpression;
		this.closingParenthesis = closingParenthesis;
	}

	@Override
	public InternalSyntaxToken castKeyword() {
		return castKeyword;
	}

	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}

	@Override
	public SeparatedList<ExpressionTree> sourceExpressions() {
		return sourceExpressions;
	}

	@Override
	public InternalSyntaxToken asKeyword() {
		return asKeyword;
	}

	@Override
	public DataTypeTreeImpl dataType() {
		return dataType;
	}

	@Override
	public InternalSyntaxToken ccsidKeyword() {
		return ccsidKeyword;
	}

	@Override
	public ExpressionTree ccsidExpression() {
		return ccsidExpression;
	}

	@Override
	public InternalSyntaxToken encodingKeyword() {
		return encodingKeyword;
	}

	@Override
	public ExpressionTree encodingExpression() {
		return encodingExpression;
	}

	@Override
	public InternalSyntaxToken formatKeyword() {
		return formatKeyword;
	}

	@Override
	public ExpressionTree formatExpression() {
		return formatExpression;
	}

	@Override
	public InternalSyntaxToken defaultKeyword() {
		return defaultKeyword;
	}

	@Override
	public ExpressionTree defaultExpression() {
		return defaultExpression;
	}

	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCastFunction(this);
	}

	@Override
	public Kind getKind() {
		return Kind.CAST_FUNCTION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(castKeyword, openingParenthesis),
				sourceExpressions.elementsAndSeparators(Functions.<ExpressionTree>identity()),
				Iterators.forArray(asKeyword, dataType, ccsidKeyword, ccsidExpression, encodingKeyword,
						encodingExpression, formatKeyword, formatExpression, defaultKeyword, defaultExpression,
						closingParenthesis));
	}

}
