/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.tree.symbols;

import java.util.Map;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.Usage;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

/**
 * This visitor creates usages for all known symbols.
 */
public class SymbolVisitor extends DoubleDispatchVisitor {

	private Scope currentScope;
	private Map<Tree, Scope> treeScopeMap;
	private SetMultimap<Scope, String> declaredBlockScopeNames = HashMultimap.create();

	public SymbolVisitor(Map<Tree, Scope> treeScopeMap) {
		this.treeScopeMap = treeScopeMap;
	}

	@Override
	public void visitProgram(ProgramTree tree) {
		this.currentScope = null;

		enterScope(tree);
		super.visitProgram(tree);
		leaveScope();
	}

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		enterScope(tree);
		super.visitCreateFunctionStatement(tree);
		leaveScope();
	}

	@Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		enterScope(tree);
		super.visitCreateProcedureStatement(tree);
		leaveScope();
	}

	@Override
	public void visitDeclareStatement(DeclareStatementTree tree) {
		for (IdentifierTree identifier : tree.nameList()) {
			scan(identifier);
			declaredBlockScopeNames.put(currentScope, identifier.name());
		}
		super.visitDeclareStatement(tree);
	}

	@Override
	public void visitIdentifier(IdentifierTree tree) {
		if (tree.is(Tree.Kind.IDENTIFIER_REFERENCE, Tree.Kind.PROPERTY_IDENTIFIER)) {
			addUsageFor(tree, Usage.Kind.READ);
		}
		super.visitIdentifier(tree);
	}
	
	@Override
	public void visitBeginEndStatement(BeginEndStatementTree tree) {
		if (isScopeAlreadyEntered(tree)) {
			super.visitBeginEndStatement(tree);

		} else {
			enterScope(tree);
			super.visitBeginEndStatement(tree);
			leaveScope();
		}
	}

	private void leaveScope() {
		if (currentScope != null) {
			currentScope = currentScope.outer();
		}
	}

	private void enterScope(Tree tree) {
		currentScope = treeScopeMap.get(tree);
		if (currentScope == null) {
			throw new IllegalStateException("No scope found for the tree");
		}
	}

	/**
	 * @return true if symbol found and usage recorded, false otherwise.
	 */
	private boolean addUsageFor(IdentifierTree identifier, Usage.Kind kind) {
		Symbol symbol = currentScope.lookupSymbol(identifier.name());
		if (symbol != null && !isUndeclaredBlockScopedSymbol(symbol)) {
			symbol.addUsage(identifier, kind);
			return true;
		}
		return false;
	}

	private boolean isUndeclaredBlockScopedSymbol(Symbol symbol) {
		return (symbol.is(Symbol.Kind.VARIABLE) || symbol.is(Symbol.Kind.CONST_VARIABLE)
				|| symbol.is(Symbol.Kind.EXTERNAL_VARIABLE)) && currentScope.equals(symbol.scope())
				&& !declaredBlockScopeNames.get(currentScope).contains(symbol.name());
	}

	private boolean isScopeAlreadyEntered(BeginEndStatementTree tree) {
		return !treeScopeMap.containsKey(tree);
	}

}
