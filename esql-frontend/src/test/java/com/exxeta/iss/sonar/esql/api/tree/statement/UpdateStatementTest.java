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
package com.exxeta.iss.sonar.esql.api.tree.statement;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.UpdateStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class UpdateStatementTest extends EsqlTreeModelTest<UpdateStatementTree>{


	@Test
	void updateStatement(){
		assertThat(Kind.UPDATE_STATEMENT)
		.matches("UPDATE Database.StockPrices AS SP SET PRICE = InputBody.Message.StockPrice WHERE SP.COMPANY = InputBody.Message.Company;")
		.matches("UPDATE Database.StockPrices AS SP SET PRICE = InputBody.Message.StockPrice, B = 1;");

	}
	
	@Test
	void modelTest() throws Exception{
		UpdateStatementTree tree = parse("UPDATE Database.StockPrices AS SP SET PRICE = InputBody.Message.StockPrice, B = 1;", Kind.UPDATE_STATEMENT);
		
		assertNotNull(tree.updateKeyword());

		assertNotNull(tree.tableReference());

		assertNotNull(tree.asKeyword());

		assertNotNull(tree.alias());

		assertNotNull(tree.setKeyword());

		assertNotNull(tree.setColumns());

		assertNull(tree.whereKeyword());

		assertNull(tree.whereExpression());

		assertNotNull(tree.semi());
		
	}
}
