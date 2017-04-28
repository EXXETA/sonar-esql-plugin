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
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;
import java.util.List;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhenClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class WhenClauseTreeImpl extends EsqlTree implements WhenClauseTree{

	private final InternalSyntaxToken whenKeyword; 
	private final ExpressionTree expression;
	private final InternalSyntaxToken thenKeyword; 
	private final List<StatementTree> statements;
	public WhenClauseTreeImpl(InternalSyntaxToken whenKeyword, ExpressionTree expression,
			InternalSyntaxToken thenKeyword, List<StatementTree> statements) {
		super();
		this.whenKeyword = whenKeyword;
		this.expression = expression;
		this.thenKeyword = thenKeyword;
		this.statements = statements;
	}
	@Override
	public InternalSyntaxToken whenKeyword() {
		return whenKeyword;
	}
	@Override
	public ExpressionTree expression() {
		return expression;
	}
	@Override
	public InternalSyntaxToken thenKeyword() {
		return thenKeyword;
	}
	@Override
	public List<StatementTree> statements() {
		return statements;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitWhenClause(this);
	}
	@Override
	public Kind getKind() {
		return Kind.WHEN_CLAUSE;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(whenKeyword, expression, thenKeyword), statements.iterator());
	}

	
}
