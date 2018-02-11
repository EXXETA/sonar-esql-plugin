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
package com.exxeta.iss.sonar.esql.api.tree.function;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.function.ListConstructorFunctionTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class ListConstructorFunctionTest extends EsqlTreeModelTest<ListConstructorFunctionTree>{

	@Test
	public void listConstructorFunction() {
		assertThat(Kind.LIST_CONSTRUCTOR_FUNCTION)
		.matches("LIST{InputBody.Car.color, 'green', 'blue'}");
	}
	@Test
	public void modelTest() throws Exception {
		ListConstructorFunctionTree tree = parse("LIST{InputBody.Car.color, 'green', 'blue'}", Kind.LIST_CONSTRUCTOR_FUNCTION);
		assertNotNull(tree.listKeyword());
		assertNotNull(tree.openingCurlyBrace());
		assertNotNull(tree.expressions());
		assertNotNull(tree.closingCurlyBrace());
	}
	
}
