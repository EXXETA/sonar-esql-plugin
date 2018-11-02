/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParseClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.lexer.EsqlPunctuator;
import com.exxeta.iss.sonar.esql.parser.TreeFactory.Tuple;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.sonar.sslr.api.typed.Optional;

public class ParseClauseTreeImpl extends EsqlTree implements ParseClauseTree{

	private InternalSyntaxToken parseKeyword;
	private InternalSyntaxToken openingParenthesis;
	private ExpressionTree expression;
	
	private List<Tuple<InternalSyntaxToken, Optional<ExpressionTree>>> parameters;
	private InternalSyntaxToken closingParenthesis;

	public ParseClauseTreeImpl(InternalSyntaxToken parseKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree expression,
			List<Tuple<InternalSyntaxToken, Optional<ExpressionTree>>> parameters, InternalSyntaxToken closingParenthesis) {
		super();
		this.parseKeyword = parseKeyword;
		this.openingParenthesis = openingParenthesis;
		this.expression=expression;
		this.parameters=parameters;
		this.closingParenthesis = closingParenthesis;
	}
	@Override
	public InternalSyntaxToken parseKeyword() {
		return parseKeyword;
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
	public InternalSyntaxToken optionsSeparator() {
		return getSeparator(0, EsqlNonReservedKeyword.OPTIONS);
	}

	@Override
	public ExpressionTree optionsExpression() {
		return getExpression(0, EsqlNonReservedKeyword.OPTIONS);
	}

	@Override
	public InternalSyntaxToken encodingSeparator() {
		return getSeparator(1, EsqlNonReservedKeyword.ENCODING);
	}


	@Override
	public ExpressionTree encodingExpression() {
		return getExpression(1, EsqlNonReservedKeyword.ENCODING);
	}


	@Override
	public InternalSyntaxToken ccsidSeparator() {
		return getSeparator(2, EsqlNonReservedKeyword.CCSID);
	}


	@Override
	public ExpressionTree ccsidExpression() {
		return getExpression(2, EsqlNonReservedKeyword.CCSID);
	}


	@Override
	public InternalSyntaxToken setSeparator() {
		return getSeparator(3, EsqlNonReservedKeyword.SET);
	}


	@Override
	public ExpressionTree setExpression() {
		return getExpression(3, EsqlNonReservedKeyword.SET);
	}


	@Override
	public InternalSyntaxToken typeSeparator() {
		return getSeparator(4, EsqlNonReservedKeyword.TYPE);
	}


	@Override
	public ExpressionTree typeExpression() {
		return getExpression(4, EsqlNonReservedKeyword.TYPE);
	}


	@Override
	public InternalSyntaxToken formatSeparator() {
		return getSeparator(5, EsqlNonReservedKeyword.FORMAT);
	}


	@Override
	public ExpressionTree formatExpression() {
		return getExpression(5, EsqlNonReservedKeyword.FORMAT);
	}

	private InternalSyntaxToken getSeparator(int position, EsqlNonReservedKeyword keyword) {
		if (isCommaSeparated()){
			if (parameters.size()>position){
				return parameters.get(position).first();
			} else {
				return null;
			}
		} else {
			for (Tuple<InternalSyntaxToken, Optional<ExpressionTree>> tuple : parameters){
				if (tuple.first().is(keyword)){
					return tuple.first();
				}
			}
			return null;
		}		
	}
	private ExpressionTree getExpression(int position, EsqlNonReservedKeyword keyword) {
		if (isCommaSeparated()){
			if (parameters.size()>position){
				if (parameters.get(position).second().isPresent()){
					return parameters.get(position).second().get();
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else if (parameters!=null){
			for (Tuple<InternalSyntaxToken, Optional<ExpressionTree>> tuple : parameters){
				if (tuple.first().is(keyword) && tuple.second().isPresent()){
					return tuple.second().get();
				}
			}
		}		
		return null;
	}

	public boolean isCommaSeparated() {
		return parameters!=null && !parameters.isEmpty() && parameters.get(0).first().is(EsqlPunctuator.COMMA);
	}
	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitParseClause(this);
	}
	@Override
	public Kind getKind() {
		return Kind.PARSE_CLAUSE;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		ArrayList<Tree> children = new ArrayList<>();
		children.add(parseKeyword);
		children.add(openingParenthesis);
		children.add(expression);
		if (parameters != null) {
			for (Tuple<InternalSyntaxToken, Optional<ExpressionTree>> parameter : parameters) {
				children.add(parameter.first());
				if (parameter.second().isPresent()) {
					children.add(parameter.second().get());
				}
			}
		}
		children.add(closingParenthesis);
		return children.iterator();
	}
	
	
	

}
