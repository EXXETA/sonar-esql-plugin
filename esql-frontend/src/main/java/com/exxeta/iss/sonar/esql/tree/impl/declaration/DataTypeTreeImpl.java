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
package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.DataTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class DataTypeTreeImpl extends EsqlTree implements DataTypeTree{

	private IntervalDataTypeTreeImpl intervalDataType;
	private DecimalDataTypeTreeImpl decimalDataType;
	private InternalSyntaxToken dataType;
	private InternalSyntaxToken toKeyword;

	public DataTypeTreeImpl(IntervalDataTypeTreeImpl firstOf) {
		this.intervalDataType = firstOf;
	}

	public DataTypeTreeImpl(DecimalDataTypeTreeImpl firstOf) {
		this.decimalDataType = firstOf;
	}

	public DataTypeTreeImpl(InternalSyntaxToken referenceKeyword, InternalSyntaxToken toKeyword) {
		this.dataType = referenceKeyword;
		this.toKeyword = toKeyword;
	}

	public DataTypeTreeImpl(InternalSyntaxToken dataType) {
		this.dataType = dataType;
	}

	@Override
	public IntervalDataTypeTreeImpl intervalDataType() {
		return intervalDataType;
	}

	@Override
	public DecimalDataTypeTreeImpl decimalDataType() {
		return decimalDataType;
	}

	@Override
	public InternalSyntaxToken dataType() {
		return dataType;
	}

	@Override
	public InternalSyntaxToken toKeyword() {
		return toKeyword;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitDataType(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.DATA_TYPE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(intervalDataType, decimalDataType, dataType, toKeyword);
	}
	
	

}
