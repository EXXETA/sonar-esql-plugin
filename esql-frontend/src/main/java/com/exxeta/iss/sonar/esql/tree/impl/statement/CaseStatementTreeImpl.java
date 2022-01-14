/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class CaseStatementTreeImpl extends EsqlTree implements CaseStatementTree {

	private final InternalSyntaxToken caseKeyword;
	private final ExpressionTree mainExpression;
	private final List<WhenClauseTreeImpl> whenClauses;
	private final InternalSyntaxToken elseKeyword;
	private final StatementsTreeImpl elseSatements;
	private final InternalSyntaxToken endKeyword;
	private final InternalSyntaxToken caseKeyword2;
	private final InternalSyntaxToken semi;

	public CaseStatementTreeImpl(InternalSyntaxToken caseKeyword, ExpressionTree mainExpression,
			List<WhenClauseTreeImpl> whenClauses, InternalSyntaxToken elseKeyword, StatementsTreeImpl elseStatements,
			InternalSyntaxToken endKeyword, InternalSyntaxToken caseKeyword2, InternalSyntaxToken semi) {
		super();
		this.caseKeyword = caseKeyword;
		this.mainExpression = mainExpression;
		this.whenClauses = whenClauses;
		this.elseKeyword = elseKeyword;
		this.elseSatements = elseStatements;
		this.endKeyword = endKeyword;
		this.caseKeyword2 = caseKeyword2;
		this.semi = semi;
	}

	@Override
	public InternalSyntaxToken caseKeyword() {
		return caseKeyword;
	}

	@Override
	public ExpressionTree mainExpression() {
		return mainExpression;
	}

	@Override
	public List<WhenClauseTreeImpl> whenClauses() {
		return whenClauses;
	}

	@Override
	public InternalSyntaxToken elseKeyword() {
		return elseKeyword;
	}

	@Override
	public StatementsTreeImpl elseSatements() {
		return elseSatements;
	}

	@Override
	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	@Override
	public InternalSyntaxToken caseKeyword2() {
		return caseKeyword2;
	}

	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCaseStatement(this);

	}

	@Override
	public Kind getKind() {
		return Kind.CASE_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(caseKeyword, mainExpression), whenClauses.iterator(),
				Iterators.forArray(elseKeyword, elseSatements,endKeyword, caseKeyword2, semi));
	}

}
