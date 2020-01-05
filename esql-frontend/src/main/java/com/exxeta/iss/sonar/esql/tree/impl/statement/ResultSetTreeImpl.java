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
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.ResultSetTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.Iterators;

public class ResultSetTreeImpl extends EsqlTree implements ResultSetTree {
	private final SyntaxToken dynamicKeyword; 
	private final SyntaxToken resultKeyword;
	private final SyntaxToken setsKeyword;
	private final SyntaxToken integer;
	public ResultSetTreeImpl(SyntaxToken dynamicKeyword, SyntaxToken resultKeyword, SyntaxToken setsKeyword,
			SyntaxToken integer) {
		super();
		this.dynamicKeyword = dynamicKeyword;
		this.resultKeyword = resultKeyword;
		this.setsKeyword = setsKeyword;
		this.integer = integer;
	}
	@Override
	public SyntaxToken dynamicKeyword() {
		return dynamicKeyword;
	}
	@Override
	public SyntaxToken resultKeyword() {
		return resultKeyword;
	}
	@Override
	public SyntaxToken setsKeyword() {
		return setsKeyword;
	}
	@Override
	public SyntaxToken integer() {
		return integer;
	}
	
	@Override
	public Kind getKind() {
		return Kind.RESULT_SET;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(dynamicKeyword, resultKeyword, setsKeyword, integer);
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitResultSet(this);
	}

	
}
