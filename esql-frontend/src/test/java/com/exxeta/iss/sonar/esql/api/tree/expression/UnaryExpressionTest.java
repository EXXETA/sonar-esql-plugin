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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;
import com.exxeta.iss.sonar.esql.tree.symbols.type.RoutineType;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class UnaryExpressionTest extends EsqlTreeModelTest<UnaryExpressionTree>{

	@Test
	void plusPrefix() {
		assertThat(EsqlLegacyGrammar.unaryExpression)
		.matches("+10")
		.matches("+1")
		;
	}
	@Test
	void minusPrefix() {
		assertThat(EsqlLegacyGrammar.unaryExpression)
		.matches("+10")
		.matches("+1")
		;
	}
	@Test
	void notPrefix() {
		assertThat(EsqlLegacyGrammar.unaryExpression)
		.matches("NOT TRUE")
		;
	}
	
	@Test
	void modelTest() throws Exception{
		UnaryExpressionTree tree = parse("+10", EsqlLegacyGrammar.unaryExpression, Kind.UNARY_PLUS);
		assertNotNull(tree);
		assertNotNull(tree.expression());
		assertNotNull(tree.operator());
		Assertions.assertThat(tree.types()).isEmpty();
		((TypableTree)tree).add( RoutineType.create());
		Assertions.assertThat(tree.types()).hasSize(1);
	}
	
}
