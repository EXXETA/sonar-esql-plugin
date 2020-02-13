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

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhileStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WhileStatementTest extends EsqlTreeModelTest<WhileStatementTree>{
	@Test
	public void whileStatement(){
		assertThat(Kind.WHILE_STATEMENT)
		.matches("X : WHILE i <= 3 DO SET i = i + 1;  END WHILE X;")
		.matches("WHILE i <= 3 DO SET i = i + 1;  END WHILE;")
		;

	}
	
	@Test
	public void modelTest() throws Exception{
		WhileStatementTree tree = parse("X : WHILE i <= 3 DO SET i = i + 1;  END WHILE X;", Kind.WHILE_STATEMENT);
		assertNotNull(tree);
		assertNotNull(tree.label());
		assertNotNull(tree.colon());
		assertNotNull(tree.whileKeyword());
		assertNotNull(tree.condition());
		assertNotNull(tree.doKeyword());
		assertNotNull(tree.statements());
		assertEquals(1, tree.statements().statements().size());
		assertNotNull(tree.endKeyword());
		assertNotNull(tree.whileKeyword2());
		assertNotNull(tree.label());
		assertNotNull(tree.semi());

	}
}
