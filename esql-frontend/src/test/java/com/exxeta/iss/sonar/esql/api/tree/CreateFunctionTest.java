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
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;


public class CreateFunctionTest extends EsqlTreeModelTest<CreateFunctionStatementTree>{

	
	@Test
	public void createProcedureStatement(){
		assertThat(Kind.CREATE_FUNCTION_STATEMENT)
		.matches("CREATE FUNCTION  myProc1( IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER ) RETURNS INTEGER LANGUAGE JAVA EXTERNAL NAME \"com.ibm.broker.test.MyClass.myMethod1\";")
		;
		
	}

	@Test
	public void modelTest() throws Exception{
		CreateFunctionStatementTree tree = parse("CREATE FUNCTION  myProc1( IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER ) RETURNS INTEGER LANGUAGE JAVA EXTERNAL NAME \"com.ibm.broker.test.MyClass.myMethod1\";", Kind.CREATE_FUNCTION_STATEMENT);
		assertNotNull(tree);
		assertNotNull(tree.createKeyword());
		assertEquals(tree.createKeyword().text(),"CREATE");
		assertNotNull(tree.routineType());
		assertNotNull(tree.identifier());
		assertNotNull(tree.openingParenthesis());
		assertNotNull(tree.parameterList());
		assertNotNull(tree.closingParenthesis());
		assertNotNull(tree.returnType());
		assertNotNull(tree.language());
		assertNull(tree.resultSet());
		assertNotNull(tree.routineBody());
		
	}
	
}
