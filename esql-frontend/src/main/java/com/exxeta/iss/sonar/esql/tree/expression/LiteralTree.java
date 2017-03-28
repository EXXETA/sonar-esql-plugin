package com.exxeta.iss.sonar.esql.tree.expression;


import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.google.common.annotations.Beta;

@Beta
public interface LiteralTree extends ExpressionTree {

  SyntaxToken token();

  String value();

}
