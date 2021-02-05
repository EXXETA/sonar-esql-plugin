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
package com.exxeta.iss.sonar.esql.api.tree.expression;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.tree.impl.expression.BetweenExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.symbols.type.PrimitiveType;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class BetweenExpressionTest extends EsqlTreeModelTest<BetweenExpressionTreeImpl>{

	@Test
	public void expression() {
		assertThat(Kind.BETWEEN_EXPRESSION)
		.matches("a between b and c")
		.matches("a not between b and c")
		.matches("c BETWEEN c.\"from\" AND currentDayIntervalRef")
		;
	}
	
	@Test
	public void modelTest() throws Exception{
		BetweenExpressionTreeImpl tree = parse("a between b and c", Kind.BETWEEN_EXPRESSION);
		assertNotNull(tree);
		assertNotNull(tree.expression());
		assertNull(tree.notKeyword());
		assertNotNull(tree.betweenKeyword());
		assertNull(tree.symmetricKeyword());
		assertNotNull(tree.endpoint1());
		assertNotNull(tree.andKeyword());
		assertNotNull(tree.endpoint2());
		
		tree.add(PrimitiveType.UNKNOWN);
		assertTrue(tree.types().contains(PrimitiveType.UNKNOWN));
	}
	
}
