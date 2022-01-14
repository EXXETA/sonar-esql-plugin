/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.tree.impl.statement;

import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LanguageTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ResultSetTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

public class CreateProcedureStatementTreeImpl extends CreateRoutineTreeImpl implements CreateProcedureStatementTree {


	public CreateProcedureStatementTreeImpl(SyntaxToken createKeyword, SyntaxToken routineType,
			IdentifierTree identifier, SyntaxToken openingParenthesis, SeparatedList<ParameterTree> parameterList,
			SyntaxToken closingParenthesis, ReturnTypeTree returnType, LanguageTree language, ResultSetTree resultSet,
			RoutineBodyTree routineBody) {
		super(createKeyword, routineType, identifier, openingParenthesis, parameterList, closingParenthesis, returnType,
				language, resultSet, routineBody);
	}


	@Override
	public Kind getKind() {
		return Kind.CREATE_PROCEDURE_STATEMENT;
	}


	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCreateProcedureStatement(this);
	}

}
