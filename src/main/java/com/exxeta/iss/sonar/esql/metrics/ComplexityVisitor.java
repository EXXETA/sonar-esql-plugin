/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
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

package com.exxeta.iss.sonar.esql.metrics;


import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.exxeta.iss.sonar.esql.api.EsqlKeyword;
import com.exxeta.iss.sonar.esql.api.EsqlMetric;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.squid.SquidAstVisitor;



public class ComplexityVisitor extends SquidAstVisitor<EsqlGrammar> {

  @Override
  public void init() {
    EsqlGrammar grammar = getContext().getGrammar();
    subscribeTo(
        grammar.routineDeclaration,
        // Branching nodes
        grammar.ifStatement,
//        grammar.iterationStatement,
        grammar.whenClause,
        grammar.forStatement,
        grammar.whileStatement,
        grammar.declareHandlerStatement,
        //grammar.catch_,
        grammar.returnStatement,
        grammar.throwStatement,
        // Expressions
        //EsqlPunctuator.QUERY,
        EsqlKeyword.AND,
        EsqlKeyword.OR);
  }

  @Override
  public void visitNode(AstNode astNode) {
    if (astNode.is(getContext().getGrammar().returnStatement) && isLastReturnStatement(astNode)) {
      return;
    }
    getContext().peekSourceCode().add(EsqlMetric.COMPLEXITY, 1);
  }

  private boolean isLastReturnStatement(AstNode astNode) {
    return astNode.nextSibling()==null;
  }

}
