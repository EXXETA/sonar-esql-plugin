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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.function.PassthruFunctionTree;
import  com.exxeta.iss.sonar.esql.utils.Assertions;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class PassthruFunctionTest extends EsqlTreeModelTest<PassthruFunctionTree>{


	@Test
	public void passthruStatement(){
		Assertions.assertThat(Kind.PASSTHRU_FUNCTION)
		.matches("PASSTHRU('SELECT R.* FROM Schema1.Table1 AS R WHERE R.Name = ? OR R.Name =            ? ORDER BY Name'   TO Database.DSN1   VALUES ('Name1', 'Name4'))")
		.matches("PASSTHRU('aaaaa' VALUES ('aaaa'))")
		.matches("PASSTHRU('aaaaa' )")
		.matches("PASSTHRU('a' || schema  TO Database.{xyz})")
		.matches(" PASSTHRU('SELECT column1,column2 FROM '||db_schema||'.table_name WHERE column3=? AND column4=?' VALUES('value1',value2))");

	}
	
	@Test
	public void modelTest() throws Exception{
		PassthruFunctionTree tree = parse("PASSTHRU('aaaaa' VALUES ('aaaa', 'bbbb'))", Kind.PASSTHRU_FUNCTION);
		assertThat(tree.passthruKeyword()).isNotNull();
		assertThat(tree.openingParenthesis()).isNotNull();
		assertThat(tree.expression()).isNotNull();
		assertThat(tree.toKeyword()).isNull();
		assertThat(tree.databaseReference()).isNull();
		assertThat(tree.valuesKeyword()).isNotNull();
		assertThat(tree.values()).isNotNull();
		assertThat(tree.values().parameters()).isNotNull();
		assertThat(tree.values().parameters().isEmpty()).isFalse();
		assertThat(tree.values().parameters().contains(tree.values().parameters().get(0))).isTrue();
		assertThat(tree.values().parameters()).hasSize(2);
		assertThat(tree.values().parameters().getSeparators()).isNotEmpty();
		assertThat(tree.values().parameters().getSeparator(0).text()).isEqualTo(",");
		assertThat(tree.values().parameters().toArray()).isNotNull();
		
		assertThat(tree.argumentList()).isNotNull();
		assertThat(tree.closingParenthesis()).isNotNull();
		
	}
	
}
 