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
package com.exxeta.iss.sonar.esql.api.tree;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

class KeywordTest {

	@Test
	void reservedKeywords(){
		assertThat(EsqlLegacyGrammar.reservedKeyword)
		.matches("WHEN")
		.matches("when")
		;
	}

	@Test
	void nonReservedKeywords(){
		assertThat(EsqlLegacyGrammar.nonReservedKeyword)
		.matches("CALL")
		.matches("call")
		;
	}
	@Test
	void keywords(){
		assertThat(EsqlLegacyGrammar.keyword)
		.matches("CALL")
		.matches("WHEN")
		.matches("when")
		;
	}
	
}
