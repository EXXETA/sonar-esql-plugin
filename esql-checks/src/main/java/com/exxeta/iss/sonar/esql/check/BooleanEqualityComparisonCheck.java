/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ParenthesisedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.UnaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import org.sonar.check.Rule;

@Rule(key = "BooleanEqualityComparison")
public class BooleanEqualityComparisonCheck extends DoubleDispatchVisitorCheck {
    private static final String MESSAGE_REFACTOR = "Refactor the code to avoid using this boolean literal.";
    private static final String MESSAGE_SIMPLIFY = "Simplify this unnecessary boolean operation.";

    @Override
    public void visitUnaryExpression(UnaryExpressionTree tree) {
        if (tree.is(Kind.LOGICAL_COMPLEMENT)) {
            visitExpression(tree.expression(), MESSAGE_SIMPLIFY);
        }

        super.visitUnaryExpression(tree);
    }


    @Override
    public void visitBinaryExpression(BinaryExpressionTree tree) {
        if (tree.is(Kind.EQUAL_TO, Kind.NOT_EQUAL_TO)) {
            visitExpression(tree.leftOperand(), MESSAGE_REFACTOR);
            visitExpression(tree.rightOperand(), MESSAGE_REFACTOR);
        }

        super.visitBinaryExpression(tree);
    }

    private void visitExpression(ExpressionTree expression, String message) {
        if (expression.is(Kind.PARENTHESISED_EXPRESSION)) {
            visitExpression(((ParenthesisedExpressionTree) expression).expression(), message);
        }

        if (expression.is(Kind.BOOLEAN_LITERAL)) {
            addIssue(expression, message);
        }
    }
}
