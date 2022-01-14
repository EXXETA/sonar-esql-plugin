/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.api.tree.function;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;


class CreateFunctionTest extends EsqlTreeModelTest<CreateFunctionStatementTree>{

	
	@Test
	void createProcedureStatement(){
		assertThat(Kind.CREATE_FUNCTION_STATEMENT)
				.matches("CREATE FUNCTION  myProc1( IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER ) RETURNS INTEGER LANGUAGE JAVA EXTERNAL NAME \"com.ibm.broker.test.MyClass.myMethod1\";")
				.matches("CREATE FUNCTION  myProc1( IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER ) RETURNS INTEGER LANGUAGE JAVA EXTERNAL NAME \"com.ibm.broker.test.MyClass.myMethod1\" CLASSLOADER \"{My_Java_SharedLib}\";")
		.matches("CREATE FUNCTION  \"A\"( ) SET a=1;")
		;
		
	}

	@Test
	void modelTest() throws Exception{
		CreateFunctionStatementTree tree = parse("CREATE FUNCTION  myProc1( IN P1 INTEGER, OUT P2 INTEGER, INOUT P3 INTEGER ) RETURNS INTEGER LANGUAGE JAVA EXTERNAL NAME \"com.ibm.broker.test.MyClass.myMethod1\";", Kind.CREATE_FUNCTION_STATEMENT);
		assertNotNull(tree);
		assertNotNull(tree.createKeyword());
		assertEquals("CREATE", tree.createKeyword().text());
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
