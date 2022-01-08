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
package com.exxeta.iss.sonar.esql.api.tree.statement;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class SetStatementTest extends EsqlTreeModelTest<SetStatementTree> {

	@Test
	void setStatement() {
		assertThat(Kind.SET_STATEMENT)
		.matches("SET a=b;")
		.matches("SET a = b.c;")
		.matches("SET i = 0;")
		.matches("SET OutputRoot=InputRoot;")
		.matches("SET a = PASSTHRU('aaaaa' VALUES ('aaaa'));")
		.matches("SET outRef.ns36:eventPayLoad.(XMLNSC.NamespaceDecl)=NULL;")
		;
		

		
	}
	@Test
	void model() throws Exception{
		SetStatementTree tree = parse("SET a=b;", Kind.SET_STATEMENT);
		assertNotNull(tree);
		assertNotNull(tree.setKeyword());
		assertNotNull(tree.variableReference());
		assertNull(tree.type());
		assertNotNull(tree.equalSign());
		assertNotNull(tree.expression());
		assertNotNull(tree.semiToken());
		
		
	}
}
