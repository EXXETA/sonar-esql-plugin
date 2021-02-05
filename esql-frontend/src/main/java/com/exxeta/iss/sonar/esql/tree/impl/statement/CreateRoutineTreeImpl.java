/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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
import java.util.stream.Stream;

import com.exxeta.iss.sonar.esql.api.symbols.Usage;
import com.exxeta.iss.sonar.esql.api.tree.RoutineDeclarationTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.LanguageTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ParameterTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ResultSetTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnTypeTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RoutineBodyTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.symbols.Scope;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public abstract class CreateRoutineTreeImpl extends EsqlTree implements RoutineDeclarationTree {

	protected final SyntaxToken createKeyword;

	protected final SyntaxToken routineType;

	protected final IdentifierTree identifier;

	protected final SyntaxToken openingParenthesis;

	protected final SeparatedList<ParameterTree> parameterList;

	protected final SyntaxToken closingParenthesis;

	protected final ReturnTypeTree returnType;
	protected final LanguageTree language;

	protected final ResultSetTree resultSet;

	protected final RoutineBodyTree routineBody;

	private Scope scope;

	public CreateRoutineTreeImpl(SyntaxToken createKeyword, SyntaxToken routineType, IdentifierTree identifier,
			SyntaxToken openingParenthesis, SeparatedList<ParameterTree> parameterList, SyntaxToken closingParenthesis,
			ReturnTypeTree returnType, LanguageTree language, ResultSetTree resultSet, RoutineBodyTree routineBody) {
		super();
		this.createKeyword = createKeyword;
		this.routineType = routineType;
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
	public SyntaxToken routineType() {
		return routineType;
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
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(createKeyword, routineType, identifier, openingParenthesis),
				parameterList.elementsAndSeparators(Functions.<ParameterTree>identity()),
				Iterators.forArray(closingParenthesis, returnType, language, resultSet, routineBody));
	}

	public final Scope scope() {
		return scope;
	}

	public final void scope(Scope scope) {
		this.scope = scope;
	}

	public Stream<Usage> outerScopeSymbolUsages() {
		return SymbolUsagesVisitor.outerScopeSymbolUsages(this);
	}

	private static class SymbolUsagesVisitor extends DoubleDispatchVisitor {

		private CreateRoutineTreeImpl functionTree;
		private Stream.Builder<Usage> outerScopeUsages = Stream.builder();

		private SymbolUsagesVisitor(CreateRoutineTreeImpl scopeTreeImpl) {
			this.functionTree = scopeTreeImpl;
		}

		private static Stream<Usage> outerScopeSymbolUsages(CreateRoutineTreeImpl createRoutineTree) {
			SymbolUsagesVisitor symbolUsagesVisitor = new SymbolUsagesVisitor(createRoutineTree);
			symbolUsagesVisitor.scan(createRoutineTree.routineBody());
			symbolUsagesVisitor.scan(createRoutineTree.parameterList());
			return symbolUsagesVisitor.outerScopeUsages.build();
		}

		@Override
		public void visitIdentifier(IdentifierTree tree) {
			tree.symbolUsage().ifPresent(usage -> {
				Tree symbolScopeTree = usage.symbol().scope().tree();
				if (symbolScopeTree.isAncestorOf(functionTree)) {
					outerScopeUsages.add(usage);
				}
			});
		}
	}

}
