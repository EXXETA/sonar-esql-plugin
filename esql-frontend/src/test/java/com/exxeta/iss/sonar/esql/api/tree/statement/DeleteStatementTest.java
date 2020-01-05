/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class DeleteStatementTest extends EsqlTreeModelTest<DeleteStatementTree> {

	@Test
	public void deleteStatement() {
		assertThat(Kind.DELETE_STATEMENT)
		.matches("DELETE FIELD OutputRoot.XMLNS.Data.Folder1.Folder12;")
		.matches("DELETE LASTCHILD OF Cursor;");
	}
	
	@Test
	public void modelTest() throws Exception{
		DeleteStatementTree tree = parse("DELETE LASTCHILD OF Cursor;", Kind.DELETE_STATEMENT);
		assertNotNull(tree.deleteKeyword());
		assertNotNull(tree.qualifier());
		assertNotNull(tree.ofKeyword());
		assertNotNull(tree.fieldReference());
		assertNotNull(tree.semi());
	}
	
}
