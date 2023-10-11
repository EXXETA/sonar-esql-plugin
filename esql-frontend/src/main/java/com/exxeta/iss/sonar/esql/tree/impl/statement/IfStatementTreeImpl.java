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
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class IfStatementTreeImpl extends EsqlTree implements IfStatementTree {

	private final InternalSyntaxToken ifKeyword;
	private final ExpressionTree condition;
	private final InternalSyntaxToken thenKeyword;
	private final StatementsTreeImpl statements;
	private final List<ElseifClauseTree> elseifClauses;
	private final ElseClauseTree elseClause;
	private final InternalSyntaxToken endKeyword;
	private final InternalSyntaxToken ifKeyword2;
	private final InternalSyntaxToken semiToken;

	public IfStatementTreeImpl(InternalSyntaxToken ifKeyword, ExpressionTree expression,
			InternalSyntaxToken thenKeyword, StatementsTreeImpl statements, List<ElseifClauseTree> elseifClauses,
			ElseClauseTree elseClause, InternalSyntaxToken endKeyword, InternalSyntaxToken ifKeyword2,
			InternalSyntaxToken semiToken) {
		super();
		this.ifKeyword = ifKeyword;
		this.condition = expression;
		this.thenKeyword = thenKeyword;
		this.statements = statements;
		this.elseifClauses = elseifClauses == null ? Collections.emptyList() : elseifClauses;
		this.elseClause = elseClause;
		this.endKeyword = endKeyword;
		this.ifKeyword2 = ifKeyword2;
		this.semiToken = semiToken;
	}

	@Override
	public SyntaxToken ifKeyword() {
		return ifKeyword;
	}

	@Override
	public ExpressionTree condition() {
		return condition;
	}

	@Override
	public SyntaxToken thenToken() {
		return thenKeyword;
	}

	@Override
	public StatementsTreeImpl statements() {
		return statements;
	}

	@Override
	public List<ElseifClauseTree> elseifClauses() {
		return elseifClauses;
	}

	@Nullable
	@Override
	public ElseClauseTree elseClause() {
		return elseClause;
	}

	@Override
	public SyntaxToken endKeyword() {
		return endKeyword;
	}

	@Override
	public SyntaxToken ifKeyword2() {
		return ifKeyword2;
	}
	
	@Override
	public SyntaxToken semiToken() {
		return semiToken;
	}

	public boolean hasElse() {
		return elseClause != null;
	}

	@Override
	public Kind getKind() {
		return Kind.IF_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.<Tree>concat(Iterators.forArray(ifKeyword, condition, thenKeyword, statements),
				elseifClauses.iterator(), Iterators.forArray(elseClause, endKeyword, ifKeyword2, semiToken));
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitIfStatement(this);
	}

}
