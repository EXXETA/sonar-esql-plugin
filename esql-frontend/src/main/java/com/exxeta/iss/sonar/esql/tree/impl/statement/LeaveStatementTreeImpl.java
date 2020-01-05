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

import java.util.Iterator;

import com.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LeaveStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class LeaveStatementTreeImpl extends EsqlTree implements LeaveStatementTree{

	private final  InternalSyntaxToken leaveKeyword;
	private final LabelTreeImpl label;
	private final InternalSyntaxToken semi;
	public LeaveStatementTreeImpl(InternalSyntaxToken leaveKeyword, LabelTreeImpl label, InternalSyntaxToken semi) {
		super();
		this.leaveKeyword = leaveKeyword;
		this.label = label;
		this.semi = semi;
	}
	@Override
	public InternalSyntaxToken leaveKeyword() {
		return leaveKeyword;
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
		visitor.visitLeaveStatement(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.LEAVE_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(leaveKeyword, label, semi);
	}
	
	
	
	
	

}
