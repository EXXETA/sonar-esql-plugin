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


import org.sonar.squidbridge.SquidAstVisitor;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.exxeta.iss.sonar.esql.api.EsqlReservedKeyword;
import com.exxeta.iss.sonar.esql.api.EsqlMetric;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;



public class ComplexityVisitor extends SquidAstVisitor<Grammar> {

  @Override
  public void init() {
    //EsqlGrammar grammar = (EsqlGrammar)getContext().getGrammar();
    subscribeTo(
    		EsqlGrammar.routineDeclaration,
        // Branching nodes
    		EsqlGrammar.ifStatement,
//        grammar.iterationStatement,
    		EsqlGrammar.whenClause,
    		EsqlGrammar.forStatement,
    		EsqlGrammar.whileStatement,
    		EsqlGrammar.declareHandlerStatement,
        //grammar.catch_,
    		EsqlGrammar.returnStatement,
    		EsqlGrammar.throwStatement//,
        // Expressions
        //EsqlPunctuator.QUERY,
        /*EsqlKeyword.AND,
        EsqlKeyword.OR*/);
  }

  @Override
  public void visitNode(AstNode astNode) {
    getContext().peekSourceCode().add(EsqlMetric.COMPLEXITY, 1);
  }


}
