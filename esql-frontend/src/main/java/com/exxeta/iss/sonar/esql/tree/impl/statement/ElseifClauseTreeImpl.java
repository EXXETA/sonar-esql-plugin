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
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ElseifClauseTreeImpl extends EsqlTree implements ElseifClauseTree {

	  private InternalSyntaxToken elseifKeyword;
	  private ExpressionTree condition;
	  private InternalSyntaxToken thenKeyword;
	  private final StatementsTreeImpl statements;

	  public ElseifClauseTreeImpl(InternalSyntaxToken elseifKeyword, ExpressionTree condition, InternalSyntaxToken thenToken, StatementsTreeImpl statements) {
		    this.elseifKeyword = elseifKeyword;
		    this.condition=condition;
		    this.thenKeyword=thenToken;
		    this.statements = statements;

		  }

	  @Override
	  public SyntaxToken elseifKeyword() {
	    return elseifKeyword;
	  }
	  
	  @Override
	  public ExpressionTree condition() {
		return condition;
	}
	  
	  @Override
	  public InternalSyntaxToken thenKeyword() {
		return thenKeyword;
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
		  
		  return Iterators.forArray(elseifKeyword, condition, thenKeyword, statements);
	  }

	  @Override
	  public void accept(DoubleDispatchVisitor visitor) {
	    visitor.visitElseifClause(this);
	  }
	}
