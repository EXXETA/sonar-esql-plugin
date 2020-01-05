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
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.Iterators;

public class StatementsTreeImpl extends EsqlTree implements StatementsTree{

	private List<StatementTree> statements;
	
	public StatementsTreeImpl(List<StatementTree> statements) {
		this.statements=statements;
	}
	
	public List<StatementTree> statements() {
		return statements;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitStatements(this);
	}

	@Override
	public Kind getKind() {
		return Kind.STATEMENTS;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(statements.toArray(new Tree[statements.size()]));
	}

	
}
