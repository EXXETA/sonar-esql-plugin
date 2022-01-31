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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class PositionFunctionTest extends EsqlTreeModelTest<PositionFunctionTree> {

	@Test
	void positionFunction() {
		assertThat(Kind.POSITION_FUNCTION).matches("POSITION('Village' IN 'Hursley Village')")
				.matches("POSITION ('A' IN 'ABCABCABCABCABC' FROM 4)")
				.matches("POSITION ('B' IN 'ABCABCABCABCABC' REPEAT 2)")
				.matches("POSITION ('A' IN 'ABCABCABCABCABC' FROM 4 REPEAT 2)");
	}

	@Test
	void modelTest() throws Exception {
		PositionFunctionTree tree = parse("POSITION ('A' IN 'ABCABCABCABCABC' FROM 4)", Kind.POSITION_FUNCTION);
		assertNotNull(tree.positionKeyword());
		assertNotNull(tree.openingParenthesis());
		assertNotNull(tree.searchExpression());
		assertNotNull(tree.inKeyword());
		assertNotNull(tree.sourceExpression());
		assertNotNull(tree.fromKeyword());
		assertNotNull(tree.fromExpression());
		assertNull(tree.repeatKeyword());
		assertNull(tree.repeatExpression());
		assertNotNull(tree.closingParenthesis());
	}

}
