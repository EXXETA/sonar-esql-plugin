package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.SchemaNameTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public interface CallStatementTree extends StatementTree{
	 InternalSyntaxToken callKeyword();
	 SchemaNameTree schemaName();
	InternalSyntaxToken dot();
	InternalSyntaxToken routineName();
	InternalSyntaxToken openParen();
	SeparatedList<ExpressionTree> parameterList();
	InternalSyntaxToken closeParen();
	InternalSyntaxToken inKeyword();
	FieldReferenceTreeImpl schemaReference();
	InternalSyntaxToken externalKeyword();
	InternalSyntaxToken schemaKeyword();
	InternalSyntaxToken externalSchemaName();
	InternalSyntaxToken intoKeyword();
	FieldReferenceTreeImpl intoTarget();
	InternalSyntaxToken semi();

}
