package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.DataTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface DeclareStatementTree extends StatementTree {

	InternalSyntaxToken declareToken();
	SeparatedList<InternalSyntaxToken> nameList();
	InternalSyntaxToken sharedExt();
	InternalSyntaxToken namesapce();
	InternalSyntaxToken constantKeyword();
	DataTypeTreeImpl dataType();
	ExpressionTree initialValueExpression();
	InternalSyntaxToken semi();

}
