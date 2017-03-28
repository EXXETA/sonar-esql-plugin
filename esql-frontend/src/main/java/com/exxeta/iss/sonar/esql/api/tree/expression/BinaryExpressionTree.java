package com.exxeta.iss.sonar.esql.api.tree.expression;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;


public interface BinaryExpressionTree extends ExpressionTree {

  ExpressionTree leftOperand();

  SyntaxToken operator();

  ExpressionTree rightOperand();

}
