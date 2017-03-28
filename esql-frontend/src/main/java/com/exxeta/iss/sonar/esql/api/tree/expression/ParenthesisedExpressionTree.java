package com.exxeta.iss.sonar.esql.api.tree.expression;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;

public interface ParenthesisedExpressionTree extends ExpressionTree {

  SyntaxToken openParenthesis();

  ExpressionTree expression();

  SyntaxToken closeParenthesis();

}
