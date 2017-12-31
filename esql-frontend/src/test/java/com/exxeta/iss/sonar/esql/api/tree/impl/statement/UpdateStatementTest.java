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
package com.exxeta.iss.sonar.esql.api.tree.impl.statement;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.InsertStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetColumnTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.UpdateStatementTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class UpdateStatementTest extends EsqlTreeModelTest<UpdateStatementTree>{


	@Test
	public void updateStatement(){
		assertThat(Kind.UPDATE_STATEMENT)
		.matches("UPDATE Database.StockPrices AS SP SET PRICE = InputBody.Message.StockPrice WHERE SP.COMPANY = InputBody.Message.Company;")
		.matches("UPDATE Database.StockPrices AS SP SET PRICE = InputBody.Message.StockPrice, B = 1;");

	}
	
	@Test
	public void modelTest() throws Exception{
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
