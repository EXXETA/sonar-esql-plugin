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

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;


public class CreateStatementTest {
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
		.matches("CREATE LASTCHILD OF OutputRoot.XMLNS.TestCase.Root IDENTITY (XML.Element)NSpace1:Element1[2] VALUE 'Element 2 Value';");
	}
	
	
}
