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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.tree.impl.statement.BeginEndStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class BeginEndStatementTest extends EsqlTreeModelTest<BeginEndStatementTree> {

	@Test
	public void modelTest() throws Exception {
		BeginEndStatementTreeImpl tree = parse("BEGIN CALL A(); END;", Kind.BEGIN_END_STATEMENT);

		assertNotNull(tree.beginKeyword());
		assertNotNull(tree.statements());
		assertNotNull(tree.endKeyword());
		assertNotNull(tree.semiToken());

		tree = parse("BEGIN NOT ATOMIC CALL A(); END;", Kind.BEGIN_END_STATEMENT);
	}

	@Test
	public void modelTestWithLabel() throws Exception {
		BeginEndStatementTreeImpl tree = parse("A: BEGIN CALL A(); END A;", Kind.BEGIN_END_STATEMENT);

		assertNotNull(tree.labelName1());
		assertNotNull(tree.colon());
		assertNotNull(tree.beginKeyword());
		assertNull(tree.notKeyword());
		assertNull(tree.atomicKeyword());
		assertNotNull(tree.statements());
		assertNotNull(tree.endKeyword());
		assertNotNull(tree.labelName2());
		assertNotNull(tree.semiToken());
	}

}
