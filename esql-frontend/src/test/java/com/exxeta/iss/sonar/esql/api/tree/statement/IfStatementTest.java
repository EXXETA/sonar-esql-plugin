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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class IfStatementTest extends EsqlTreeModelTest<IfStatementTree> {
	@Test
	void ifStatement() {
		assertThat(Kind.IF_STATEMENT).matches("IF TRUE THEN CALL B(); END IF;");

	}

	@Test
	void modelTest() throws Exception {
		IfStatementTree tree = parse("IF TRUE THEN CALL A(); ELSEIF TRUE THEN CALL B(); ELSE CALL C(); END IF;", Kind.IF_STATEMENT);
		assertNotNull(tree.ifKeyword());
		assertNotNull(tree.condition());

		assertNotNull(tree.statements());

		assertNotNull(tree.thenToken());

		assertNotNull(tree.elseifClauses());
		assertNotNull(tree.elseifClauses().get(0).elseifKeyword());
		assertNotNull(tree.elseifClauses().get(0).condition());
		assertNotNull(tree.elseifClauses().get(0).thenKeyword());
		assertNotNull(tree.elseifClauses().get(0).statements());

		assertTrue(tree.hasElse());
		assertNotNull(tree.elseClause());
		assertNotNull(tree.elseClause().elseKeyword());
		assertNotNull(tree.elseClause().statements());

		assertNotNull(tree.endKeyword());

		assertNotNull(tree.ifKeyword2());

		assertNotNull(tree.semiToken());
		
		tree = parse("IF TRUE THEN CALL A(); END IF;", Kind.IF_STATEMENT);
		assertFalse(tree.hasElse());
	}

}
