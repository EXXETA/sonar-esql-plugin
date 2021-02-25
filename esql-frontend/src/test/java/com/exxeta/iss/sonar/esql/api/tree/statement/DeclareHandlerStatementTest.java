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
package com.exxeta.iss.sonar.esql.api.tree.statement;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.SqlStateTree;
import com.exxeta.iss.sonar.esql.tree.impl.statement.DeclareHandlerStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class DeclareHandlerStatementTest extends EsqlTreeModelTest<DeclareHandlerStatementTreeImpl> {

	@Test
	public void declareHandlerStatement() {
		assertThat(Kind.DECLARE_HANDLER_STATEMENT)
				.matches("DECLARE EXIT HANDLER FOR SQLSTATE VALUE 'U11222' SET a = 1;")
				.matches("DECLARE EXIT HANDLER FOR SQLSTATE VALUE 'U11222', SQLSTATE VALUE 'U11223' SET a = 2;");

	}

	@Test
	public void modelTest() throws Exception {
		DeclareHandlerStatementTreeImpl tree = parse(
				"DECLARE EXIT HANDLER FOR SQLSTATE VALUE 'U11222', SQLSTATE VALUE 'U11223' SET a = 2;",
				Kind.DECLARE_HANDLER_STATEMENT);

		assertNotNull(tree);
		assertNotNull(tree.declareKeyword());
		assertNotNull(tree.handlerType());
		assertNotNull(tree.forKeyword());
		assertNotNull(tree.handlerKeyword());
		assertNotNull(tree.sqlStates());
		assertNotNull(tree.statement());
		
		SqlStateTree sqlState = tree.sqlStates().get(0);
		assertNotNull(sqlState);
		
		
		assertNotNull(sqlState.sqlstateKeyword());
		assertNull(sqlState.likeKeyword());
		assertNull(sqlState.likeText());
		assertNull(sqlState.escapeKeyword());
		assertNull(sqlState.escapeText());
		assertNotNull(sqlState.valueKeyword()); 
		assertNotNull(sqlState.valueText());
		
		tree = parse(
				"DECLARE EXIT HANDLER FOR SQLSTATE LIKE'%' ESCAPE '$' SET a = 2;",
				Kind.DECLARE_HANDLER_STATEMENT);
	}

}
