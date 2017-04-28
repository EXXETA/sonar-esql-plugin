package com.exxeta.iss.sonar.esql.api.tree.function;

import com.exxeta.iss.sonar.esql.tree.impl.function.FromClauseExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.SelectClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.WhereClauseTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface SelectFunctionTree extends ComplexFunctionTree{
	 InternalSyntaxToken selectKeyword();
	 SelectClauseTreeImpl selectClause();
	 FromClauseExpressionTreeImpl fromClause();
	 WhereClauseTreeImpl whereClause();
}
