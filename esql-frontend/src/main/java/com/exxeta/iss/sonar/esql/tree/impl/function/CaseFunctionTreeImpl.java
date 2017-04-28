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
import java.util.List;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.CaseFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class CaseFunctionTreeImpl extends EsqlTree implements CaseFunctionTree{
	private InternalSyntaxToken caseKeyword;
	private ExpressionTree sourceValue;
	private List<WhenClauseExpressionTreeImpl> whenClauses;
	private InternalSyntaxToken elseKeyword;
	private ExpressionTree elseExpression;
	private InternalSyntaxToken endKeyword;
	
	public CaseFunctionTreeImpl(InternalSyntaxToken caseKeyword, ExpressionTree sourceValue, List<WhenClauseExpressionTreeImpl> whenClauses,
			InternalSyntaxToken elseKeyword, ExpressionTree elseExpression, InternalSyntaxToken endKeyword) {
		super();
		this.caseKeyword = caseKeyword;
		this.sourceValue = sourceValue;
		this.whenClauses = whenClauses;
		this.elseKeyword = elseKeyword;
		this.elseExpression = elseExpression;
		this.endKeyword = endKeyword;
	}

	@Override
	public InternalSyntaxToken caseKeyword() {
		return caseKeyword;
	}
	
	@Override
	public ExpressionTree sourceValue() {
		return sourceValue;
	}

	@Override
	public List<WhenClauseExpressionTreeImpl> whenClauses() {
		return whenClauses;
	}

	@Override
	public InternalSyntaxToken elseKeyword() {
		return elseKeyword;
	}

	@Override
	public ExpressionTree elseExpression() {
		return elseExpression;
	}

	@Override
	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCaseFunction(this);
	}

	@Override
	public Kind getKind() {
		return Kind.CASE_FUNCTION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(caseKeyword, sourceValue), whenClauses.iterator(),
				Iterators.forArray(elseKeyword, elseExpression, endKeyword));
	}	
	
	
	
	
}
