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
import com.exxeta.iss.sonar.esql.api.tree.statement.IterateStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class IterateStatementTreeImpl extends EsqlTree implements IterateStatementTree{

	private final  InternalSyntaxToken iterateKeyword;
	private final LabelTreeImpl label;
	private final InternalSyntaxToken semi;
	public IterateStatementTreeImpl(InternalSyntaxToken iterateKeyword, LabelTreeImpl label, InternalSyntaxToken semi) {
		super();
		this.iterateKeyword = iterateKeyword;
		this.label = label;
		this.semi = semi;
	}
	@Override
	public InternalSyntaxToken iterateKeyword() {
		return iterateKeyword;
	}
	@Override
	public LabelTreeImpl label() {
		return label;
	}
	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitIterateStatement(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.ITERATE_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(iterateKeyword, label, semi);
	}
	
	
	
	
	

}
