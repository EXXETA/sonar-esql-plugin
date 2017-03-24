package com.exxeta.iss.sonar.esql.api.tree.expression;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import java.util.List;

public interface ArrayLiteralTree extends ExpressionTree {

  SyntaxToken openBracket();

  List<ExpressionTree> elements();

  List<Tree> elementsAndCommas();

  SyntaxToken closeBracket();

}
