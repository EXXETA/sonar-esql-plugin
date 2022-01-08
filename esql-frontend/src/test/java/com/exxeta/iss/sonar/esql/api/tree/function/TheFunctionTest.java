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
package com.exxeta.iss.sonar.esql.api.tree.function;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class TheFunctionTest extends EsqlTreeModelTest<TheFunctionTree> {

	@Test
	void theFunction() {
		assertThat(Kind.THE_FUNCTION)
			.notMatches("THE(")
			.matches("THE (getList())");
	}

	@Test
	void modelTest() throws Exception {
		TheFunctionTree tree = parse("THE (getList())", Kind.THE_FUNCTION);
		
		assertNotNull(tree);
		assertNotNull(tree.theKeyword());
		assertNotNull(tree.openingParenthesis());
		assertNotNull(tree.expression());
		assertNotNull(tree.closingParenthesis());
	}
	
}
