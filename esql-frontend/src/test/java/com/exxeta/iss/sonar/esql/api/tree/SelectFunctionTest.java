/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class SelectFunctionTest {

	@Test
	public void selectFunction() {
		assertThat(Kind.SELECT_FUNCTION)
		.matches("SELECT P.PartNumber AS a,  P.Description,  P.Price FROM PartsTable.Part[]")
		.matches("SELECT * FROM Database.Datasource.SchemaName.Table As A")
		.matches("SELECT P.PartNumber AS a,  P.Description,  P.Price FROM PartsTable.Part[] AS P")
		;
	}
	
	@Test
	public void selectClause(){
		assertThat(Kind.SELECT_CLAUSE)
		.matches("P.PartNumber,  P.Description,  P.Price");
	}

	@Test
	public void fromClause(){
		assertThat(Kind.FROM_CLAUSE_EXPRESSION)
		.matches("FROM PartsTable.Part[] AS P")
		.matches("FROM Database.Datasource.SchemaName.Table As A")
		;
	}

	@Test
	public void whereClause(){
		assertThat(Kind.WHERE_CLAUSE)
		.matches("WHERE A = B");
	}

}
