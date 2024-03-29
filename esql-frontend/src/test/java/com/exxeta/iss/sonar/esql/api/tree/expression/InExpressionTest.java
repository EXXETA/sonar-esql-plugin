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
package com.exxeta.iss.sonar.esql.api.tree.expression;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.tree.impl.expression.InExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.symbols.type.PrimitiveType;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class InExpressionTest extends EsqlTreeModelTest<InExpressionTreeImpl>{

	@Test
	void expression() {
		assertThat(Kind.IN_EXPRESSION)
		.matches("a in (a,b,c)")
		.matches("a not in (a,b,c)")
		;
	}
	
	@Test
	void modelTest() throws Exception{
		InExpressionTreeImpl tree = parse("a in (a,b,c)", Kind.IN_EXPRESSION);
		assertNotNull(tree);
		assertNotNull(tree.expression());
		assertNull(tree.notKeyword());
		assertNotNull(tree.inKeyword());
		assertNotNull(tree.argumentList());
		tree.add(PrimitiveType.UNKNOWN);
		assertTrue(tree.types().contains(PrimitiveType.UNKNOWN));
	}
	
}
