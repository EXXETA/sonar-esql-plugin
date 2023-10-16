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

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;
import org.junit.jupiter.api.Test;

class RollbackStatementTest extends EsqlTreeModelTest<RollbackStatementTree> {


	@Test
	void commitStatement(){
		assertThat(Kind.ROLLBACK_STATEMENT)
		.matches("ROLLBACK;");

	}
	
	@Test
	void modelTest() throws Exception {
		RollbackStatementTree tree = parse("ROLLBACK Database.{Source};", Kind.ROLLBACK_STATEMENT);
		assertNotNull(tree.rollbackKeyword());

		assertNotNull(tree.databaseReference());

		assertNotNull(tree.semi());

	}
	
}
