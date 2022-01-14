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

class SubstringFunctionTest extends EsqlTreeModelTest<SubstringFunctionTree> {

	@Test
	void substringFunction() {
		assertThat(Kind.SUBSTRING_FUNCTION).matches("SUBSTRING('Hello World!' FROM 7 FOR 4)")
				.matches("SUBSTRING('Hello World!' FROM 7)");
	}

	@Test
	void modelTest() throws Exception {
		SubstringFunctionTree tree = parse("SUBSTRING('Hello World!' FROM 7 FOR 4)", Kind.SUBSTRING_FUNCTION);
		assertNotNull(tree.substringKeyword());
		assertNotNull(tree.openingParenthesis());
		assertNotNull(tree.sourceExpression());
		assertNotNull(tree.qualifier());
		assertNotNull(tree.location());
		assertNotNull(tree.forKeyword());
		assertNotNull(tree.stringLength());
		assertNotNull(tree.closingParenthesis());
	}

}
