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
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.tree.impl.statement.DeclareStatementTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class DeclareStatementTest extends EsqlTreeModelTest<DeclareStatementTree>{

	@Test
	public void modelTest() throws Exception {
		DeclareStatementTreeImpl tree = parse("DECLARE deployEnvironment EXTERNAL CHARACTER 'Dev';", Kind.DECLARE_STATEMENT);
		
		assertNotNull(tree.declareToken());
		assertNotNull(tree.nameList());
		assertNotNull(tree.sharedExt());
		assertNull(tree.namesapce());
		assertNull(tree.constantKeyword());
		assertNotNull(tree.dataType());
		assertNotNull(tree.initialValueExpression());
		assertNotNull(tree.semi());
		
		parse("DECLARE aaa NAMESPACE 'com.exxeta.test';");
		parse("DECLARE Schema1 NAME 'Joe';");
	}
	
}
