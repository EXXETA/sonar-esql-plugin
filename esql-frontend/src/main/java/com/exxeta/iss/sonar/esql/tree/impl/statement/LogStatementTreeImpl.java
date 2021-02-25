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
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LogStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ParameterListTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class LogStatementTreeImpl extends EsqlTree implements LogStatementTree {

	private InternalSyntaxToken logKeyword;
	private InternalSyntaxToken eventKeyword;
	private InternalSyntaxToken userKeyword;
	private InternalSyntaxToken traceKeyword;
	private InternalSyntaxToken fullKeyword;
	private InternalSyntaxToken exceptionKeyword;
	private InternalSyntaxToken severityKeyword;
	private ExpressionTree severityExpression;
	private InternalSyntaxToken catalogKeyword;
	private ExpressionTree catalogExpression;
	private InternalSyntaxToken messageKeyword;
	private ExpressionTree messageExpression;
	private InternalSyntaxToken valuesKeyword;
	private ParameterListTreeImpl valueExpressions;
	private InternalSyntaxToken semi;

	public LogStatementTreeImpl(InternalSyntaxToken logKeyword, InternalSyntaxToken eventKeyword,
			InternalSyntaxToken userKeyword, InternalSyntaxToken traceKeyword, InternalSyntaxToken fullKeyword,
			InternalSyntaxToken exceptionKeyword, InternalSyntaxToken severityKeyword,
			ExpressionTree severityExpression, InternalSyntaxToken catalogKeyword, ExpressionTree catalogExpression,
			InternalSyntaxToken messageKeyword, ExpressionTree messageExpression, InternalSyntaxToken valuesKeyword,
			ParameterListTreeImpl valueExpressions, InternalSyntaxToken semi) {
		super();
		this.logKeyword = logKeyword;
		this.eventKeyword = eventKeyword;
		this.userKeyword = userKeyword;
		this.traceKeyword = traceKeyword;
		this.fullKeyword = fullKeyword;
		this.exceptionKeyword = exceptionKeyword;
		this.severityKeyword = severityKeyword;
		this.severityExpression = severityExpression;
		this.catalogKeyword = catalogKeyword;
		this.catalogExpression = catalogExpression;
		this.messageKeyword = messageKeyword;
		this.messageExpression = messageExpression;
		this.valuesKeyword = valuesKeyword;
		this.valueExpressions = valueExpressions;
		this.semi = semi;
	}

	@Override
	public InternalSyntaxToken logKeyword() {
		return logKeyword;
	}

	@Override
	public InternalSyntaxToken eventKeyword() {
		return eventKeyword;
	}

	@Override
	public InternalSyntaxToken userKeyword() {
		return userKeyword;
	}

	@Override
	public InternalSyntaxToken traceKeyword() {
		return traceKeyword;
	}

	@Override
	public InternalSyntaxToken fullKeyword() {
		return fullKeyword;
	}

	@Override
	public InternalSyntaxToken exceptionKeyword() {
		return exceptionKeyword;
	}

	@Override
	public InternalSyntaxToken severityKeyword() {
		return severityKeyword;
	}

	@Override
	public ExpressionTree severityExpression() {
		return severityExpression;
	}

	@Override
	public InternalSyntaxToken catalogKeyword() {
		return catalogKeyword;
	}

	@Override
	public ExpressionTree catalogExpression() {
		return catalogExpression;
	}

	@Override
	public InternalSyntaxToken messageKeyword() {
		return messageKeyword;
	}

	@Override
	public ExpressionTree messageExpression() {
		return messageExpression;
	}

	@Override
	public InternalSyntaxToken valuesKeyword() {
		return valuesKeyword;
	}

	@Override
	public ParameterListTreeImpl valueExpressions() {
		return valueExpressions;
	}

	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitLogStatement(this);
	}

	@Override
	public Kind getKind() {
		return Kind.LOG_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(logKeyword, eventKeyword, userKeyword, traceKeyword, fullKeyword, exceptionKeyword,
				severityKeyword, severityExpression, catalogKeyword, catalogExpression, messageKeyword,
				messageExpression, valuesKeyword, valueExpressions, semi);
	}

}
