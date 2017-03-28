package com.exxeta.iss.sonar.esql.api.tree.expression;

import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.google.common.annotations.Beta;

@Beta
public interface UnaryExpressionTree extends ExpressionTree {

  SyntaxToken operator();

  ExpressionTree expression();

}
