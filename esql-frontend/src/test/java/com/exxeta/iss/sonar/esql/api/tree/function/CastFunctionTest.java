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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class CastFunctionTest extends EsqlTreeModelTest<CastFunctionTree> {

	@Test
	void castFunction() {
		assertThat(Kind.CAST_FUNCTION).matches("CAST(source AS DATE FORMAT pattern)").matches("CAST(7, 6, 5 AS DATE)")
				.matches("CAST(7.4e0, 6.5e0, 5.6e0 AS DATE)")
				.matches("CAST(3.1e0, 4.2e0, 5.3e0, 6.4e0, 7.5e0, 8.6789012e0 AS GMTTIMESTAMP)")
				.matches("CAST ( 1  AS INTERVAL DAY )")
				.matches("CAST ( 1  AS INTERVAL DAY CCSID 1208 ENCODING defaultEncoding DEFAULT 1)")
				.matches("CAST(num_str AS INTEGER DEFAULT -1)")
				;
	}

	@Test
	void modelTest() throws Exception {
		CastFunctionTree tree = parse("CAST(source AS DATE FORMAT pattern)", Kind.CAST_FUNCTION);

		assertNotNull(tree.castKeyword());
		assertNotNull(tree.openingParenthesis());
		assertNotNull(tree.sourceExpressions());
		assertNotNull(tree.asKeyword());
		assertNotNull(tree.dataType());
		assertNull(tree.dataType().intervalDataType());
		assertNull(tree.dataType().decimalDataType());
		assertNull(tree.dataType().toKeyword());
		assertNull(tree.ccsidKeyword());
		assertNull(tree.ccsidExpression());
		assertNull(tree.encodingKeyword());
		assertNull(tree.encodingExpression());
		assertNotNull(tree.formatKeyword());
		assertNotNull(tree.formatExpression());
		assertNull(tree.defaultKeyword());
		assertNull(tree.defaultExpression());
		assertNotNull(tree.closingParenthesis());
	}
}
