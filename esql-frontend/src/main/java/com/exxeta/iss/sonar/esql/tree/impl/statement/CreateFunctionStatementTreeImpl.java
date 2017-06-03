/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LanguageTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ResultSetTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.expression.IdentifierTreeImpl;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class CreateFunctionStatementTreeImpl extends CreateRoutineTreeImpl implements CreateFunctionStatementTree {

	private final SyntaxToken createKeyword;

	private final SyntaxToken functionKeyword;

	private final IdentifierTree identifier;

	private final SyntaxToken openingParenthesis;

	private final SeparatedList<ParameterTree> parameterList;

	private final SyntaxToken closingParenthesis;

	private final ReturnTypeTree returnType;
	private final LanguageTree language;

	private final ResultSetTree resultSet;

	private final RoutineBodyTree routineBody;

	public CreateFunctionStatementTreeImpl(SyntaxToken createKeyword, SyntaxToken functionKeyword,
			IdentifierTree identifier, SyntaxToken openingParenthesis, SeparatedList<ParameterTree> parameterList,
			SyntaxToken closingParenthesis, ReturnTypeTree returnType, LanguageTree language, ResultSetTree resultSet,
			RoutineBodyTree routineBody) {
		super();
		this.createKeyword = createKeyword;
		this.functionKeyword = functionKeyword;
		this.identifier = identifier;
		this.openingParenthesis = openingParenthesis;
		this.parameterList = parameterList;
		this.closingParenthesis = closingParenthesis;
		this.returnType = returnType;
		this.language = language;
		this.resultSet = resultSet;
		this.routineBody = routineBody;
	}

	@Override
	public SyntaxToken createKeyword() {
		return createKeyword;
	}

	@Override
	public SyntaxToken functionKeyword() {
		return functionKeyword;
	}

	@Override
	public IdentifierTree identifier() {
		return identifier;
	}

	@Override
	public SyntaxToken openingParenthesis() {
		return openingParenthesis;
	}

	@Override
	public SeparatedList<ParameterTree> parameterList() {
		return parameterList;
	}

	@Override
	public SyntaxToken closingParenthesis() {
		return closingParenthesis;
	}

	@Override
	public ReturnTypeTree returnType() {
		return returnType;
	}

	@Override
	public LanguageTree language() {
		return language;
	}

	@Override
	public ResultSetTree resultSet() {
		return resultSet;
	}

	@Override
	public RoutineBodyTree routineBody() {
		return routineBody;
	}

	@Override
	public Kind getKind() {
		return Kind.CREATE_FUNCTION_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(createKeyword, functionKeyword, identifier, openingParenthesis),
				parameterList.elementsAndSeparators(Functions.<ParameterTree> identity()),
				Iterators.forArray(closingParenthesis, returnType, language, resultSet, routineBody));
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCreateFunctionStatement(this);
	}

}
