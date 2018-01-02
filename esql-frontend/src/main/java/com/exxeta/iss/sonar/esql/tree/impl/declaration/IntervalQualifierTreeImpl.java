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
package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.IntervalQualifierTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class IntervalQualifierTreeImpl extends EsqlTree implements IntervalQualifierTree{

	private InternalSyntaxToken from;
	private InternalSyntaxToken toKeyword;
	private InternalSyntaxToken to;
	
	public IntervalQualifierTreeImpl(InternalSyntaxToken from) {
		this.from = from;
	}

	public IntervalQualifierTreeImpl(InternalSyntaxToken from, InternalSyntaxToken toKeyword, InternalSyntaxToken to) {
		this.from = from;
		this.toKeyword = toKeyword;
		this.to = to;
	}

	@Override
	public InternalSyntaxToken from() {
		return from;
	}

	@Override
	public InternalSyntaxToken toKeyword() {
		return toKeyword;
	}

	@Override
	public InternalSyntaxToken to() {
		return to;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitIntervalQualifier(this);
	}

	@Override
	public Kind getKind() {
		return Kind.INTERVAL_QUALIFIER;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(from, toKeyword, to);
	}
	
	
	
	

}
