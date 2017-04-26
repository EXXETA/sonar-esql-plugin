package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.api.tree.DataTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

public interface CastFunctionTree extends ComplexFunctionTree{
	 SyntaxToken castKeyword(); 
	 SyntaxToken openingParenthesis();
	 SeparatedList<Tree> sourceExpressions(); 
	 SyntaxToken asKeyword(); 
	 DataTypeTree dataType();
	 SyntaxToken ccsidKeyword();
	 ExpressionTree ccsidExpression();
	 SyntaxToken encodingKeyword();
	 ExpressionTree encodingExpression();
	 SyntaxToken formatKeyword();
	 ExpressionTree formatExpression();
	 SyntaxToken defaultKeyword();
	 ExpressionTree defaultExpression();
	 SyntaxToken closingParenthesis();

}
