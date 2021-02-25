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
package com.exxeta.iss.sonar.esql.highlighter;

import java.util.LinkedList;
import java.util.List;

import org.sonar.api.batch.sensor.symbol.NewSymbol;
import org.sonar.api.batch.sensor.symbol.NewSymbolTable;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.Usage;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitorContext;

public class HighlightSymbolTableBuilder {

	private HighlightSymbolTableBuilder() {
	}

	public static void build(NewSymbolTable newSymbolTable, TreeVisitorContext context) {
		for (Symbol symbol : context.getSymbolModel().getSymbols()) {
			highlightSymbol(newSymbolTable, symbol);
		}
		newSymbolTable.save();
	}

	private static void highlightSymbol(NewSymbolTable newSymbolTable, Symbol symbol) {
		if (!symbol.usages().isEmpty()) {
			List<Usage> usagesList = new LinkedList<>(symbol.usages());
			SyntaxToken token = (usagesList.get(0).identifierTree()).identifierToken();
			NewSymbol newSymbol = getHighlightedSymbol(newSymbolTable, token);
			for (int i = 1; i < usagesList.size(); i++) {
				SyntaxToken referenceToken = getToken(usagesList.get(i).identifierTree());
				addReference(newSymbol, referenceToken);
			}

		}
	}

	private static void addReference(NewSymbol symbol, SyntaxToken referenceToken) {
		symbol.newReference(referenceToken.line(), referenceToken.column(), referenceToken.line(),
				referenceToken.column() + referenceToken.text().length());
	}

	private static NewSymbol getHighlightedSymbol(NewSymbolTable newSymbolTable, SyntaxToken token) {
		return newSymbolTable.newSymbol(token.line(), token.column(), token.line(),
				token.column() + token.text().length());
	}

	private static SyntaxToken getToken(IdentifierTree identifierTree) {
		return (identifierTree).identifierToken();
	}
}
