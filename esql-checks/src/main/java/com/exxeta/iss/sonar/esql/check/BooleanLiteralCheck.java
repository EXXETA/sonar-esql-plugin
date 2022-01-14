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
package com.exxeta.iss.sonar.esql.check;

import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.UnaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.google.common.collect.ImmutableSet;

@Rule(key="BooleanLiteral")
public class BooleanLiteralCheck extends SubscriptionVisitorCheck {
	@Override
	  public Set<Kind> nodesToVisit() {
	    return ImmutableSet.of(Kind.EQUAL_TO, Kind.NOT_EQUAL_TO, Kind.CONDITIONAL_AND, Kind.CONDITIONAL_OR, Kind.LOGICAL_COMPLEMENT);
	  }

	  @Override
	  public void visitNode(Tree tree) {
	    LiteralTree literal;
	    if(tree.is(Kind.LOGICAL_COMPLEMENT)) {
	      literal = getBooleanLiteral(((UnaryExpressionTree)tree).expression());
	    } else {
	      literal = getBooleanLiteralOperands((BinaryExpressionTree)tree);
	    }
	    if(literal != null) {
	      addIssue(literal, "Remove the literal \"" + literal.value() + "\" boolean value.");
	    }
	  }

	  private static LiteralTree getBooleanLiteral(Tree tree) {
	    LiteralTree result = null;
	    if (tree.is(Kind.BOOLEAN_LITERAL)) {
	      result = (LiteralTree) tree;
	    }
	    return result;
	  }

	  private static LiteralTree getBooleanLiteralOperands(BinaryExpressionTree tree) {
	    LiteralTree result = getBooleanLiteral(tree.leftOperand());
	    if (result == null) {
	      result = getBooleanLiteral(tree.rightOperand());
	    }
	    return result;
	  }
}
