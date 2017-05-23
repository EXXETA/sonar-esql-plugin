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
package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.DecimalDataTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class DecimalDataTypeTreeImpl extends EsqlTree implements DecimalDataTypeTree{

	InternalSyntaxToken decimalKeyword;
	InternalSyntaxToken openParen;
	InternalSyntaxToken precision; 
	InternalSyntaxToken comma;
	InternalSyntaxToken scale; 
	InternalSyntaxToken closeParen;
	public DecimalDataTypeTreeImpl(InternalSyntaxToken openParen, InternalSyntaxToken precision,
			InternalSyntaxToken comma, InternalSyntaxToken scale, InternalSyntaxToken closeParen) {
		super();
		this.openParen = openParen;
		this.precision = precision;
		this.comma = comma;
		this.scale = scale;
		this.closeParen = closeParen;
	}
	
	
	public DecimalDataTypeTreeImpl(InternalSyntaxToken decimalKeyword) {
		super();
		this.decimalKeyword = decimalKeyword;
	}


	public DecimalDataTypeTreeImpl complete(InternalSyntaxToken decimalKeyword){
		this.decimalKeyword=decimalKeyword;
		return this;
	}


	@Override
	public InternalSyntaxToken decimalKeyword() {
		return decimalKeyword;
	}


	@Override
	public InternalSyntaxToken openParen() {
		return openParen;
	}


	@Override
	public InternalSyntaxToken precision() {
		return precision;
	}


	@Override
	public InternalSyntaxToken comma() {
		return comma;
	}


	@Override
	public InternalSyntaxToken scale() {
		return scale;
	}


	@Override
	public InternalSyntaxToken closeParen() {
		return closeParen;
	}


	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitDecimalDataType(this);
		
	}


	@Override
	public Kind getKind() {
		return Kind.DECIMAL_DATA_TYPE;
	}


	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(decimalKeyword, openParen, precision, comma, scale, closeParen);
	}
	
	
	
}
