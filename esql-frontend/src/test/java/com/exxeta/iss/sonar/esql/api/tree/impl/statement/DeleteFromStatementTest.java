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

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteFromStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class DeleteFromStatementTest extends EsqlTreeModelTest<DeleteFromStatementTree>{


	@Test
	public void deleteFromStatement(){
		assertThat(Kind.DELETE_FROM_STATEMENT)
		.matches("DELETE FROM Database.SHAREHOLDINGS AS S WHERE S.ACCOUNTNO = InputBody.AccountNumber;");

	}
	
	@Test
	public void modelTest() throws Exception{
		DeleteFromStatementTree tree = parse("DELETE FROM Database.SHAREHOLDINGS AS S WHERE S.ACCOUNTNO = InputBody.AccountNumber;", Kind.DELETE_FROM_STATEMENT);
		assertNotNull(tree.deleteKeyword()); 
		assertNotNull(tree.fromKeyword());
		assertNotNull(tree.tableReference());
		assertNotNull(tree.asKeyword());
		assertNotNull(tree.asCorrelationName());
		assertNotNull(tree.whereKeyword());
		assertNotNull(tree.whereExpression());
		assertNotNull(tree.semi());
		
	}
	
}
