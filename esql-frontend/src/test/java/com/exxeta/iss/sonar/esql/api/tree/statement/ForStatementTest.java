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

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.tree.impl.statement.ForStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class ForStatementTest extends EsqlTreeModelTest<ForStatementTreeImpl>{

	@Test
	void forStatement() {
		assertThat(Kind.FOR_STATEMENT)
		.matches("FOR source AS Environment.SourceData.Folder[] DO END FOR;")
		.matches("FOR source AS Environment.SourceData.Folder[] DO SET A = 1; SET B = 2; END FOR;");

	}
	
	
	@Test
	void modelTest() throws Exception{
		ForStatementTreeImpl tree = parse("FOR source AS Environment.SourceData.Folder[] DO SET A = 1; SET B = 2; END FOR;", Kind.FOR_STATEMENT);
		
		assertNotNull(tree);
		
		assertNotNull(tree.forKeyword());
		assertNotNull(tree.correlationName());
		assertNotNull(tree.asKeyword());
		assertNotNull(tree.fieldReference());
		assertNotNull(tree.doKeyword());
		assertNotNull(tree.statements());
		assertNotNull(tree.forKeyword2());
		assertNotNull(tree.endKeyword());
		assertNotNull(tree.semi());
		
	}
}
