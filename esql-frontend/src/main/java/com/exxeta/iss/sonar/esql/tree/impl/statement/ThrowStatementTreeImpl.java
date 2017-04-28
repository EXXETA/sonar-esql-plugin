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

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterListTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ThrowStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;

public class ThrowStatementTreeImpl extends EsqlTree implements ThrowStatementTree{

	private SyntaxToken throwKeyword;

	private SyntaxToken userKeyword;

	private SyntaxToken exceptionKeyword;

	private SyntaxToken severityKeyword;

	private ExpressionTree severity;

	private SyntaxToken catalogKeyword;

	private ExpressionTree catalog;

	private SyntaxToken messageKeyword;

	private ExpressionTree message;

	private SyntaxToken valuesKeyword;

	private ParameterListTree values;
	
	private SyntaxToken semi;

	public ThrowStatementTreeImpl(SyntaxToken throwKeyword, SyntaxToken userKeyword,
			SyntaxToken exceptionKeyword, SyntaxToken severityKeyword, ExpressionTree severity,
			SyntaxToken catalogKeyword, ExpressionTree catalog, SyntaxToken messageKeyword, ExpressionTree message,
			SyntaxToken valuesKeyword, ParameterListTree values, SyntaxToken semi) {
		super();
		this.throwKeyword = throwKeyword;
		this.userKeyword = userKeyword;
		this.exceptionKeyword = exceptionKeyword;
		this.severityKeyword = severityKeyword;
		this.severity = severity;
		this.catalogKeyword = catalogKeyword;
		this.catalog = catalog;
		this.messageKeyword = messageKeyword;
		this.message = message;
		this.valuesKeyword = valuesKeyword;
		this.values = values;
		this.semi=semi;
	}

	@Override
	public SyntaxToken throwKeyword() {
		return throwKeyword;
	}

	@Override
	public SyntaxToken userKeyword() {
		return userKeyword;
	}

	@Override
	public SyntaxToken exceptionKeyword() {
		return exceptionKeyword;
	}

	@Override
	public SyntaxToken severityKeyword() {
		return severityKeyword;
	}

	@Override
	public ExpressionTree severity() {
		return severity;
	}

	@Override
	public SyntaxToken catalogKeyword() {
		return catalogKeyword;
	}

	@Override
	public ExpressionTree catalog() {
		return catalog;
	}

	@Override
	public SyntaxToken messageKeyword() {
		return messageKeyword;
	}

	@Override
	public ExpressionTree message() {
		return message;
	}

	@Override
	public SyntaxToken valuesKeyword() {
		return valuesKeyword;
	}

	@Override
	public ParameterListTree values() {
		return values;
	}
	
	@Override
	public SyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitThrowStatement(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.THROW_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(throwKeyword, userKeyword, exceptionKeyword, severityKeyword, severity, catalogKeyword, catalog, messageKeyword, message, valuesKeyword, values, semi);
	}
	
	
	
}
