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
package com.exxeta.iss.sonar.esql.tree.symbols;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;
import com.exxeta.iss.sonar.esql.utils.TestUtils;

class ScopeTest extends EsqlTreeModelTest<EsqlTree> {
	private SymbolModelImpl SYMBOL_MODEL = symbolModel(
			TestUtils.createTestInputFile("src/test/resources/ast/resolve/scope.esql"));

	private Scope scopeAtLine(int line) {
		for (Scope scope : SYMBOL_MODEL.getScopes()) {
			if (scope.tree().firstToken().line() == line) {
				return scope;
			}
		}
		throw new IllegalStateException();
	}

	@Test
	void scopes_number() throws Exception {
		assertThat(SYMBOL_MODEL.getScopes()).hasSize(9);
	}

	@Test
	void test_global_scope() throws Exception {
		Scope scope = SYMBOL_MODEL.globalScope();

		assertThat(scope.isGlobal()).isTrue();
		assertThat(scope.isBlock()).isFalse();
		assertThat(scopeAtLine(1)).isEqualTo(scope);
		// assertThat(symbols(scope)).containsOnly("a", "b", "f", "const1", "let1", "c",
		// "A", "notBlock", "gen", "catch1", "try2", "identifier", "foobar",
		// "globalFunction");
	}

}
