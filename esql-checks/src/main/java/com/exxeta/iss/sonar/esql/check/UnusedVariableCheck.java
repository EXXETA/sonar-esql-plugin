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
package com.exxeta.iss.sonar.esql.check;

import java.util.Collection;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.SymbolModel;
import com.exxeta.iss.sonar.esql.api.symbols.Usage;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

/**
 * This Java class is created to implement the logic to find all the unused
 * variables.
 * 
 * @author Sapna Singh
 * @author Thomas Pohl
 *
 */
@Rule(key = "UnusedVariable")
public class UnusedVariableCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Remove the unused '%s' variable.";

	@Override
	public void visitProgram(ProgramTree tree) {
		super.visitProgram(tree);
		SymbolModel symbolModel = getContext().getSymbolModel();
		for (Symbol symbol : symbolModel.getSymbols()) {

			Collection<Usage> usages = symbol.usages();
			if (noUsages(usages) && symbol.isVariable() && !symbol.scope().isGlobal()) {
				raiseIssuesOnDeclarations(symbol, String.format(MESSAGE, symbol.name()));
			}
		}
	}

	private void raiseIssuesOnDeclarations(Symbol symbol, String message) {
		for (Usage usage : symbol.usages()) {
			if (usage.isDeclaration()) {
				addIssue(usage.identifierTree(), message);
			}
		}
	}

	private static boolean noUsages(Collection<Usage> usages) {
		return usages.isEmpty() || usagesAreInitializations(usages);
	}

	private static boolean usagesAreInitializations(Collection<Usage> usages) {
		for (Usage usage : usages) {
			if (!usage.isDeclaration()) {
				return false;
			}
		}
		return true;
	}

}
