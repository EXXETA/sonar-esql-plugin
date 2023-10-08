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

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.EvalStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class EvalStatementTreeImpl extends EsqlTree implements EvalStatementTree{
	private InternalSyntaxToken evalKeyword;
	private InternalSyntaxToken openingParenthesis;
	private ExpressionTree expression;
	private InternalSyntaxToken closingParenthesis;
	private InternalSyntaxToken semi;
	public EvalStatementTreeImpl(InternalSyntaxToken evalKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree expression, InternalSyntaxToken closingParenthesis, InternalSyntaxToken semi) {
		super();
		this.evalKeyword = evalKeyword;
		this.openingParenthesis = openingParenthesis;
		this.expression = expression;
		this.closingParenthesis = closingParenthesis;
		this.semi=semi;
	}
	@Override
	public InternalSyntaxToken evalKeyword() {
		return evalKeyword;
	}
	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}
	@Override
	public ExpressionTree expression() {
		return expression;
	}
	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}
	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitEvalStatement(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.EVAL_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(evalKeyword, openingParenthesis, expression, closingParenthesis, semi);
	}
	
	
	
	
}
