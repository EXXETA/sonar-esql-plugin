package com.exxeta.iss.sonar.esql.api.tree.statement;

import com.exxeta.iss.sonar.esql.api.tree.RoutineDeclarationTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

public interface CreateProcedureStatementTree extends RoutineDeclarationTree{
	SyntaxToken createKeyword();

	SyntaxToken procedureKeyword();

	SyntaxToken identifier();

	SyntaxToken openingParenthesis();

	SeparatedList<ParameterTree> parameterList();

	SyntaxToken closingParenthesis();

	ReturnTypeTree returnType();

	LanguageTree language();

	ResultSetTree resultSet();

	RoutineBodyTree routineBody();
}
