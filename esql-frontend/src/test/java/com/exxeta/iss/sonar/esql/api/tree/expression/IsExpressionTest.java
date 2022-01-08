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
package com.exxeta.iss.sonar.esql.api.tree.expression;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import org.assertj.core.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.tree.symbols.type.RoutineType;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class IsExpressionTest extends EsqlTreeModelTest<IsExpressionTree>{

	@Test
	void expression() {
		assertThat(Kind.IS_EXPRESSION)
		.matches("a is null")
		.matches("a is not false")
		.matches("a() is +inf")
		.matches("a() is -inf")
		.matches("(a+b) is number")
		;
	}
	
	@Test
	void modelTest() throws Exception {
		IsExpressionTree tree = parse("a IS NULL", Kind.IS_EXPRESSION);
		assertNotNull(tree);
		assertNotNull(tree.expression());
		assertNotNull(tree.isKeyword());
		assertNull(tree.notKeyword());
		assertNull(tree.plusMinus());
		assertNotNull(tree.with());
		Assertions.assertThat(tree.types()).isEmpty();
		((TypableTree)tree).add( RoutineType.create());
		Assertions.assertThat(tree.types()).hasSize(1);

	}

}
