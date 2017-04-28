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

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.RoundFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class RoundFunctionTreeImpl extends EsqlTree implements RoundFunctionTree {
	private InternalSyntaxToken roundKeyword;
	private InternalSyntaxToken openingParenthesis;
	private ExpressionTree sourceNumber;
	private InternalSyntaxToken comma;
	private ExpressionTree precision;
	private InternalSyntaxToken modeKeyword;
	private InternalSyntaxToken roundingMode;
	private InternalSyntaxToken closingParenthesis;

	public RoundFunctionTreeImpl(InternalSyntaxToken roundKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree sourceNumber, InternalSyntaxToken comma, ExpressionTree precision,
			InternalSyntaxToken modeKeyword, InternalSyntaxToken roundingMode, InternalSyntaxToken closingParenthesis) {
		super();
		this.roundKeyword = roundKeyword;
		this.openingParenthesis = openingParenthesis;
		this.sourceNumber = sourceNumber;
		this.comma = comma;
		this.precision = precision;
		this.modeKeyword = modeKeyword;
		this.roundingMode = roundingMode;
		this.closingParenthesis = closingParenthesis;
	}

	public RoundFunctionTreeImpl(InternalSyntaxToken roundKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree sourceNumber, InternalSyntaxToken comma, ExpressionTree precision,
			InternalSyntaxToken closingParenthesis) {
		super();
		this.roundKeyword = roundKeyword;
		this.openingParenthesis = openingParenthesis;
		this.sourceNumber = sourceNumber;
		this.comma = comma;
		this.precision = precision;
		this.modeKeyword = null;
		this.roundingMode = null;
		this.closingParenthesis = closingParenthesis;
	}

	@Override
	public InternalSyntaxToken roundKeyword() {
		return roundKeyword;
	}

	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}

	@Override
	public ExpressionTree sourceNumber() {
		return sourceNumber;
	}

	public InternalSyntaxToken comma() {
		return comma;
	}

	public ExpressionTree precision() {
		return precision;
	}

	public InternalSyntaxToken modeKeyword() {
		return modeKeyword;
	}

	public InternalSyntaxToken roundingMode() {
		return roundingMode;
	}

	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitRoundFunction(this); 
	}

	@Override
	public Kind getKind() {
		return Kind.ROUND_FUNCTION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(roundKeyword,openingParenthesis, sourceNumber, comma, precision, modeKeyword, roundingMode, closingParenthesis);
	}
	
	

}
