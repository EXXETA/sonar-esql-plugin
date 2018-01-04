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

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.AttachStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class AttachStatementTest extends EsqlTreeModelTest<AttachStatementTree> {
	@Test
	public void attachStatement(){
		assertThat(Kind.ATTACH_STATEMENT)
		.matches("ATTACH ref1 TO OutputRoot.XMLNSC.Data.Order[2] AS LASTCHILD;");

	}
	
	@Test
	public void modelTest() throws Exception {
		AttachStatementTree tree = parse("ATTACH ref1 TO OutputRoot.XMLNSC.Data.Order[2] AS LASTCHILD;", Kind.ATTACH_STATEMENT);
		assertNotNull(tree);
		
		assertNotNull(tree.attachKeyword());

		assertNotNull(tree.dynamicReference());

		assertNotNull(tree.toKeyword());

		assertNotNull(tree.fieldReference());

		assertNotNull(tree.asKeyword());

		assertNotNull(tree.location());

		assertNotNull(tree.semi());
	}
}
