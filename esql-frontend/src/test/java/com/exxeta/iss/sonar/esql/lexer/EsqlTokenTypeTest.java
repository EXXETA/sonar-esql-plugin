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
package com.exxeta.iss.sonar.esql.lexer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EsqlTokenTypeTest {
	@Test
	void test() {
		assertThat(EsqlTokenType.values().length).isEqualTo(3);

		for (EsqlTokenType type : EsqlTokenType.values()) {
			assertThat(type.getName()).isEqualTo(type.name());
			assertThat(type.getValue()).isEqualTo(type.name());
		}
	}

	@Test
	void hasToBeSkippedFromAst() throws Exception {
		assertThrows(IllegalStateException.class, () -> {
			EsqlTokenType.IDENTIFIER.hasToBeSkippedFromAst(null);
		});
	}
}
