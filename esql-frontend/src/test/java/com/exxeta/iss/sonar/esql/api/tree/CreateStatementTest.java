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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CallStatementTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;


public class CreateStatementTest  extends EsqlTreeModelTest<CreateStatementTreeImpl>{
	@Test
	public void domainClause(){
		assertThat(Kind.REPEAT_CLAUSE)
		.matches("REPEAT");
	}	
	
	@Test
	public void valueClause(){
		assertThat(Kind.VALUES_CLAUSE)
		.matches("VALUE 'Element 2 Value'");
	}
	
	@Test
	public void createStatement(){
		assertThat(Kind.CREATE_STATEMENT)
		.matches("CREATE FIELD OutputRoot.XMLNS.Data;")
		.matches("CREATE LASTCHILD OF OutputRoot.XMLNS.TestCase.Root IDENTITY (XML.Element)NSpace1:Element1[2] VALUE 'Element 2 Value';")
		.matches("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM');")
		.matches("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM') PARSE(inBitStream, inEncoding, inCCSID, 'DP3UK14002001', 'TestCase', 'XML1', options);")
		;
	}
	
	@Test
	public void parseClause(){
		assertThat(Kind.PARSE_CLAUSE)
		.matches("PARSE(inBitStream, inEncoding, inCCSID, 'DP3UK14002001', 'TestCase', 'XML1', options)")
		;
	}
	
	@Test
	public void modelTest() throws Exception {
		CreateStatementTreeImpl tree = parse("CREATE LASTCHILD OF OutputRoot DOMAIN('MRM') PARSE(inBitStream, inEncoding, inCCSID, 'DP3UK14002001', 'TestCase', 'XML1', options);", Kind.CREATE_STATEMENT);
		assertNotNull(tree);
		assertNotNull(tree.createKeyword());
		assertEquals(tree.createKeyword().text(), "CREATE");
		assertNotNull(tree.qualifierName());
		assertEquals(tree.qualifierName().text(), "LASTCHILD");
		assertNotNull(tree.qualifierOfKeyword());
		assertEquals(tree.qualifierOfKeyword().text(), "OF");
		assertNotNull(tree.target());
		assertNull(tree.asKeyword());
		assertNull(tree.aliasFieldReference());
		assertNotNull(tree.domainKeyword());
		assertEquals(tree.domainKeyword().text(), "DOMAIN");
		assertNotNull(tree.domainExpression());
		assertTrue(tree.domainExpression().is(Kind.PARENTHESISED_EXPRESSION));
		assertNull(tree.repeatClause());
		assertNull(tree.valuesClause());
		assertNull(tree.fromClause());
		assertNotNull(tree.parseClause());
		assertNotNull(tree.semi());
		assertEquals(tree.semi().text(), ";");
		
	}
	
}
