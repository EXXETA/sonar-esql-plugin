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
package com.exxeta.iss.sonar.esql.api.tree.impl.statement;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.ThrowStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class ThrowStatementTest extends EsqlTreeModelTest<ThrowStatementTree> {
	@Test
	public void throwStatement() {
		assertThat(Kind.THROW_STATEMENT).matches("THROW USER EXCEPTION;")
				.matches("THROW USER EXCEPTION CATALOG 'BIPmsgs' MESSAGE 2951 VALUES(1,2,3,4,5,6,7,8) ;")
				.matches(
						"THROW USER EXCEPTION CATALOG 'BIPmsgs' MESSAGE 2951 VALUES('The SQL State: ', SQLSTATE, 'The SQL Code: ', SQLCODE, 'The SQLNATIVEERROR: ', SQLNATIVEERROR,    'The SQL Error Text: ', SQLERRORTEXT ) ;")
				.matches("THROW USER EXCEPTION CATALOG 'BIPmsgs' MESSAGE 2951;");

	}

	@Test
	public void modelTest() throws Exception {
		ThrowStatementTree tree = parse("THROW USER EXCEPTION CATALOG 'BIPmsgs' MESSAGE 2951 VALUES(1,2,3,4,5,6,7,8) ;",
				Kind.THROW_STATEMENT);

		assertNotNull(tree.throwKeyword());
		assertNotNull(tree.userKeyword());
		assertNotNull(tree.exceptionKeyword());
		assertNull(tree.severityKeyword());
		assertNull(tree.severity());
		assertNotNull(tree.catalogKeyword());
		assertNotNull(tree.catalog());
		assertNotNull(tree.messageKeyword());
		assertNotNull(tree.message());
		assertNotNull(tree.valuesKeyword());
		assertNotNull(tree.values());
		assertNotNull(tree.semi());

	}

}
