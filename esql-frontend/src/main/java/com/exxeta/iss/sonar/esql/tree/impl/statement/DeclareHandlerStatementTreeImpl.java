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
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareHandlerStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SqlStateTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class DeclareHandlerStatementTreeImpl extends EsqlTree implements DeclareHandlerStatementTree{

	private InternalSyntaxToken declareKeyword;
	private InternalSyntaxToken handlerType;
	private InternalSyntaxToken handlerKeyword;
	private InternalSyntaxToken forKeyword;
	private SeparatedList<SqlStateTree> sqlStates;
	private StatementTree statement;
	public DeclareHandlerStatementTreeImpl(InternalSyntaxToken declareKeyword, InternalSyntaxToken handlerType,
			InternalSyntaxToken handlerKeyword, InternalSyntaxToken forKeyword, SeparatedList<SqlStateTree> sqlStates,
			StatementTree statement) {
		super();
		this.declareKeyword = declareKeyword;
		this.handlerType = handlerType;
		this.handlerKeyword = handlerKeyword;
		this.forKeyword = forKeyword;
		this.sqlStates = sqlStates;
		this.statement = statement;
	}
	public InternalSyntaxToken declareKeyword() {
		return declareKeyword;
	}
	public InternalSyntaxToken handlerType() {
		return handlerType;
	}
	public InternalSyntaxToken handlerKeyword() {
		return handlerKeyword;
	}
	public InternalSyntaxToken forKeyword() {
		return forKeyword;
	}
	public SeparatedList<SqlStateTree> sqlStates() {
		return sqlStates;
	}
	public StatementTree statement() {
		return statement;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitDeclareHandlerStatement(this);
	}
	@Override
	public Kind getKind() {
		return Kind.DECLARE_HANDLER_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(declareKeyword, handlerType, handlerKeyword, forKeyword), sqlStates.elementsAndSeparators(Functions.<SqlStateTree> identity()), Iterators.singletonIterator(statement));
	}
	
	
	
}
