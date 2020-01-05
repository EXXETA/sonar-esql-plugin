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
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ElseClauseTreeImpl extends EsqlTree implements ElseClauseTree {

	  private SyntaxToken elseKeyword;
	  private final StatementsTreeImpl statements;

	  public ElseClauseTreeImpl(InternalSyntaxToken elseKeyword, StatementsTreeImpl statements) {
	    this.elseKeyword = elseKeyword;
	    this.statements = statements;

	  }

	  @Override
	  public SyntaxToken elseKeyword() {
	    return elseKeyword;
	  }

	  @Override
	  public StatementsTreeImpl statements() {
	    return statements;
	  }

	  @Override
	  public Kind getKind() {
	    return Kind.ELSE_CLAUSE;
	  }

	  @Override
	  public Iterator<Tree> childrenIterator() {
	    return Iterators.<Tree>forArray(elseKeyword, statements);
	  }

	  @Override
	  public void accept(DoubleDispatchVisitor visitor) {
	    visitor.visitElseClause(this);
	  }
	}
