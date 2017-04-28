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

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParseClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class ParseClauseTreeImpl extends EsqlTree implements ParseClauseTree{

	private InternalSyntaxToken parseKeyword;
	private InternalSyntaxToken openingParenthesis;
	private SeparatedList<Tree> options;
	private InternalSyntaxToken encodingKeyword;
	private ExpressionTree encoding;
	private InternalSyntaxToken ccsidKeyword;
	private ExpressionTree ccsid;
	private InternalSyntaxToken setKeyword;
	private ExpressionTree set;
	private InternalSyntaxToken typeKeyword;
	private ExpressionTree type;
	private InternalSyntaxToken formatKeyword;
	private ExpressionTree format;
	private InternalSyntaxToken closingParenthesis;
	public ParseClauseTreeImpl(InternalSyntaxToken parseKeyword, InternalSyntaxToken openingParenthesis,
			SeparatedList<Tree> options, InternalSyntaxToken encodingKeyword, ExpressionTree encoding,
			InternalSyntaxToken ccsidKeyword, ExpressionTree ccsid, InternalSyntaxToken setKeyword, ExpressionTree set,
			InternalSyntaxToken typeKeyword, ExpressionTree type, InternalSyntaxToken formatKeyword,
			ExpressionTree format, InternalSyntaxToken closingParenthesis) {
		super();
		this.parseKeyword = parseKeyword;
		this.openingParenthesis = openingParenthesis;
		this.options = options;
		this.encodingKeyword = encodingKeyword;
		this.encoding = encoding;
		this.ccsidKeyword = ccsidKeyword;
		this.ccsid = ccsid;
		this.setKeyword = setKeyword;
		this.set = set;
		this.typeKeyword = typeKeyword;
		this.type = type;
		this.formatKeyword = formatKeyword;
		this.format = format;
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
	public SeparatedList<Tree> options() {
		return options;
	}
	@Override
	public InternalSyntaxToken encodingKeyword() {
		return encodingKeyword;
	}
	@Override
	public ExpressionTree encoding() {
		return encoding;
	}
	@Override
	public InternalSyntaxToken ccsidKeyword() {
		return ccsidKeyword;
	}
	@Override
	public ExpressionTree ccsid() {
		return ccsid;
	}
	@Override
	public InternalSyntaxToken setKeyword() {
		return setKeyword;
	}
	@Override
	public ExpressionTree set() {
		return set;
	}
	@Override
	public InternalSyntaxToken typeKeyword() {
		return typeKeyword;
	}
	@Override
	public ExpressionTree type() {
		return type;
	}
	@Override
	public InternalSyntaxToken formatKeyword() {
		return formatKeyword;
	}
	@Override
	public ExpressionTree format() {
		return format;
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
		return Iterators.concat(Iterators.forArray(parseKeyword, openingParenthesis), options.elementsAndSeparators(Functions.<Tree> identity()),
				Iterators.forArray(encodingKeyword, encoding, ccsidKeyword, ccsid, setKeyword, set, typeKeyword, type, formatKeyword, format, closingParenthesis));
	}
	
	
	

}
