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
package com.exxeta.iss.sonar.esql.api.tree.expression;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class LiteralTest extends EsqlTreeModelTest<LiteralTree> {

	@Test
	public void stringLiteral() {
		assertThat(Kind.STRING_LITERAL)
		.matches("'a'")
		.matches("''");
	}
	
	@Test
	public void numericalLiteral(){
		assertThat(Kind.NUMERIC_LITERAL)
		.matches("1")
		.matches("0")
		.matches("11111")
		.matches("5.3");
	}
	
	@Test
	public void lietral(){
		assertThat(EsqlLegacyGrammar.LITERAL)
		.matches("0");
	}
	
	@Test
	public void timeLiteral(){
		assertThat(Kind.TIME_LITERAL)
		.matches("TIME '00:00:00'");
	}
	@Test
	public void timestampLiteral(){
		assertThat(Kind.TIMESTAMP_LITERAL)
		.matches("TIMESTAMP '2017-01-01 00:00:00'")
		.notMatches("timestamp");
	}
	
	@Test
	public void stringLiteralModelTest() throws Exception{
		LiteralTree tree = parse("'a'", Kind.STRING_LITERAL);
		assertNotNull(tree);
		assertNotNull(tree.token());
		assertNotNull(tree.value());
		assertNotNull(tree.toString());
		
	}
	
}
