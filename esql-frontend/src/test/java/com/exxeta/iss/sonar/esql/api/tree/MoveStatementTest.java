/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.tree.impl.statement.MoveStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.NameClausesTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class MoveStatementTest extends EsqlTreeModelTest<MoveStatementTreeImpl>{
	@Test
	public void moveStatement(){
		
		assertThat(Kind.MOVE_STATEMENT)
		.matches("MOVE sourceCursor NEXTSIBLING;")
		.matches("MOVE cursor FIRSTCHILD TYPE Name NAME 'Field1';")
		.matches("MOVE cursor TO OutputRoot;");

	}
	
	@Test
	public void modelTest() throws Exception{
		MoveStatementTreeImpl tree = parse("MOVE cursor FIRSTCHILD TYPE Name NAME 'Field1';", Kind.MOVE_STATEMENT);
		assertNotNull(tree.moveKeyword());
		assertEquals(tree.moveKeyword().text(),"MOVE");
		assertNotNull(tree.target());
		assertEquals(tree.target().text(), "cursor");
		assertNull(tree.toKeyword());
		assertNull(tree.sourceFieldReference());
		assertNotNull(tree.qualifier());
		assertNotNull(tree.nameClauses());
		assertNotNull(tree.semi());
		assertEquals(tree.semi().text(), ";");
		NameClausesTreeImpl nameClauses = tree.nameClauses();
		assertNotNull(nameClauses.typeKeyword());
		assertNotNull(nameClauses.typeExpression());
		assertNull(nameClauses.namespaceKeyword());
		assertNull(nameClauses.namespaceExpression());
		assertNull(nameClauses.namespaceStar());
		assertNotNull(nameClauses.nameKeyword());
		assertNotNull(nameClauses.nameExpression());
		
		assertNull(nameClauses.identityKeyword());
		assertNull(nameClauses.pathElement());
		assertNull(nameClauses.repeatKeyword()); 
		assertNull(nameClauses.repeatTypeKeyword()); 
		assertNull(nameClauses.repeatNameKeyword()); 

		
		
	}
	
}
