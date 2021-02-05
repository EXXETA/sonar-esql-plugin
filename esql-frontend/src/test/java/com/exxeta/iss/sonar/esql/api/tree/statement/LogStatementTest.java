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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.LogStatementTree;
import com.exxeta.iss.sonar.esql.tree.impl.statement.LogStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class LogStatementTest extends EsqlTreeModelTest<LogStatementTreeImpl> {
	@Test
	public void logStatement() {

		assertThat(Kind.LOG_STATEMENT).matches("LOG EVENT SEVERITY 1 CATALOG 'BIPmsgs' MESSAGE 2951 VALUES(1,2,3,4);")
				.matches("LOG USER TRACE EXCEPTION VALUES(SQLSTATE, 'DivideByZero');");

	}

	@Test
	public void modelTest() throws Exception {
		LogStatementTree tree = parse("LOG EVENT SEVERITY 1 CATALOG 'BIPmsgs' MESSAGE 2951 VALUES(1,2,3,4);",
				Kind.LOG_STATEMENT);
		assertNotNull(tree);
		assertNotNull(tree.logKeyword());
		assertEquals("LOG", tree.logKeyword().text());
		assertNotNull(tree.eventKeyword());
		assertEquals("EVENT", tree.eventKeyword().text());
		assertNull(tree.userKeyword());
		assertNull(tree.traceKeyword());
		assertNull(tree.fullKeyword());
		assertNull(tree.exceptionKeyword());
		assertNotNull(tree.severityKeyword());
		assertEquals("SEVERITY", tree.severityKeyword().text());
		assertNotNull(tree.severityExpression());
		assertTrue(tree.severityExpression().is(Kind.NUMERIC_LITERAL));

		assertNotNull(tree.catalogKeyword());
		assertEquals("CATALOG", tree.catalogKeyword().text());
		assertNotNull(tree.catalogExpression());
		assertTrue(tree.catalogExpression().is(Kind.STRING_LITERAL));

		assertNotNull(tree.messageKeyword());
		assertEquals("MESSAGE", tree.messageKeyword().text());
		assertNotNull(tree.messageExpression());
		assertTrue(tree.messageExpression().is(Kind.NUMERIC_LITERAL));

		assertNotNull(tree.valuesKeyword());
		assertEquals("VALUES", tree.valuesKeyword().text());
		assertNotNull(tree.valueExpressions());

		assertNotNull(tree.semi());

	}

}
