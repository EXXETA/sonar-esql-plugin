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
import com.exxeta.iss.sonar.esql.tree.expression.LikeExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.symbols.type.PrimitiveType;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class LikeExpressionTest extends EsqlTreeModelTest<LikeExpressionTreeImpl>{

	@Test
	public void expression() {
		assertThat(Kind.LIKE_EXPRESSION)
		.matches("Body.Trade.Company LIKE 'I__'")
		.matches("Body.Trade.Company LIKE 'I%'")
		.matches("Body.Trade.Company NOT LIKE 'IBM\\_Corp'")
		.matches("Body.Trade.Company LIKE 'IBM$_Corp' ESCAPE '$'")
		;
	}
	
	@Test
	public void modelTest() throws Exception{
		LikeExpressionTreeImpl tree = parse("Body.Trade.Company LIKE 'I__'", Kind.LIKE_EXPRESSION);
		assertNotNull(tree);
		assertNotNull(tree.source());
		assertNull(tree.notKeyword());
		assertNotNull(tree.likeKeyword());
		assertNotNull(tree.pattern());
		assertNull(tree.escapeKeyword());
		assertNull(tree.escapeChar());
		
		
		tree.add(PrimitiveType.UNKNOWN);
		assertTrue(tree.types().contains(PrimitiveType.UNKNOWN));
	}
	
}
