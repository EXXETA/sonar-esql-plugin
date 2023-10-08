/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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

class RowConstructorFunctionTest extends EsqlTreeModelTest<RowConstructorFunctionTree>{

	@Test
	void rowConstructorFunction() {
		assertThat(Kind.ALIASED_EXPRESSION)
		.matches("'ABCDEFGHIJKLMNOPQRSTUVWXYZ' AS \"[A-Z]\"");
		assertThat(Kind.ROW_CONSTRUCTOR_FUNCTION)
		.matches("ROW('granary' AS bread, 'riesling' AS wine, 'stilton' AS cheese)")
		.matches("ROW('ABCDEFGHIJKLMNOPQRSTUVWXYZ' AS \"[A-Z]\")");
		assertThat(Kind.SET_STATEMENT)
		.matches("SET OutputRoot.XMLNS.Data = ROW('granary' AS bread, 'riesling' AS wine, 'stilton' AS cheese);");
	}
	
	@Test
	void modelTest() throws Exception{
		RowConstructorFunctionTree tree = parse ("ROW('granary' AS bread, 'riesling' AS wine, 'stilton' AS cheese)", Kind.ROW_CONSTRUCTOR_FUNCTION);
		assertNotNull(tree.rowKeyword());
		assertNotNull(tree.openingParenthesis());
		assertNotNull(tree.aliasedExpressions());
		assertNotNull(tree.closingParenthesis());
		
	}
	
	
}
