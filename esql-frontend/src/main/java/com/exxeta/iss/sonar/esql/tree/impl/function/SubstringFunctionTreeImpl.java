/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.SubstringFunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class SubstringFunctionTreeImpl extends EsqlTree implements SubstringFunctionTree{
	private InternalSyntaxToken substringKeyword;
	private InternalSyntaxToken openingParenthesis;
	private ExpressionTree sourceExpression; 
	private InternalSyntaxToken qualifier; 
	private ExpressionTree location;
	private InternalSyntaxToken forKeyword;
	private ExpressionTree stringLength;
	private InternalSyntaxToken closingParenthesis;
	public SubstringFunctionTreeImpl(InternalSyntaxToken substringKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree sourceExpression, InternalSyntaxToken qualifier, ExpressionTree location,
			InternalSyntaxToken forKeyword, ExpressionTree stringLength, InternalSyntaxToken closingParenthesis) {
		super();
		this.substringKeyword = substringKeyword;
		this.openingParenthesis = openingParenthesis;
		this.sourceExpression = sourceExpression;
		this.qualifier = qualifier;
		this.location = location;
		this.forKeyword = forKeyword;
		this.stringLength = stringLength;
		this.closingParenthesis = closingParenthesis;
	}
	public SubstringFunctionTreeImpl(InternalSyntaxToken substringKeyword, InternalSyntaxToken openingParenthesis,
			ExpressionTree sourceExpression, InternalSyntaxToken qualifier, ExpressionTree location,
			InternalSyntaxToken closingParenthesis) {
		super();
		this.substringKeyword = substringKeyword;
		this.openingParenthesis = openingParenthesis;
		this.sourceExpression = sourceExpression;
		this.qualifier = qualifier;
		this.location = location;
		this.forKeyword = null;
		this.stringLength = null;
		this.closingParenthesis = closingParenthesis;
	}
	@Override
	public InternalSyntaxToken substringKeyword() {
		return substringKeyword;
	}
	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}
	@Override
	public ExpressionTree sourceExpression() {
		return sourceExpression;
	}
	@Override
	public InternalSyntaxToken qualifier() {
		return qualifier;
	}
	@Override
	public ExpressionTree location() {
		return location;
	}
	@Override
	public InternalSyntaxToken forKeyword() {
		return forKeyword;
	}
	@Override
	public ExpressionTree stringLength() {
		return stringLength;
	}
	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSubstringFunction(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.SUBSTRING_FUNCTION;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(substringKeyword, openingParenthesis, sourceExpression, qualifier, location, forKeyword, stringLength, closingParenthesis);
	}
	
	
	
	
	
}
