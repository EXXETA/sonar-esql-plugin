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
package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.SyntacticEquivalence;

@Rule(key="IdenticalExpressionOnBinaryOperator")
public class IdenticalExpressionOnBinaryOperatorCheck extends DoubleDispatchVisitorCheck{

	 private static final String MESSAGE = "Correct one of the identical sub-expressions on both sides of operator \"%s\"";
	 
	 @Override
	  public void visitBinaryExpression(BinaryExpressionTree tree) {
	    if (!tree.is(Kind.MULTIPLY, Kind.PLUS, Kind.CONCAT)
	      && SyntacticEquivalence.areEquivalent(tree.leftOperand(), tree.rightOperand()) ) {

	      String message = String.format(MESSAGE, tree.operator().text());
	      addIssue(tree.rightOperand(), message)
	        .secondary(tree.leftOperand());
	    }

	    super.visitBinaryExpression(tree);
	  }
	
}
