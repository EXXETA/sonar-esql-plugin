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
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class RepeatStatementTest extends EsqlTreeModelTest<RepeatStatementTree>{
	@Test
	public void repeatStatement(){
		assertThat(Kind.REPEAT_STATEMENT)
		.matches("X : REPEAT SET i = i + 1; UNTIL  i>= 3 END REPEAT X;")
		.matches("REPEAT SET i = i + 1; UNTIL  i>= 3 END REPEAT;");

	}
	
	@Test
	public void modelTest() throws Exception {
		RepeatStatementTree tree = parse("X : REPEAT SET i = i + 1; UNTIL  i>= 3 END REPEAT X;", Kind.REPEAT_STATEMENT);
		assertNotNull(tree);
		assertNotNull(tree.label());
		assertNotNull(tree.colon());
		assertNotNull(tree.repeatKeyword());
		assertNotNull(tree.statements());
		assertNotNull(tree.untilKeyword());
		assertNotNull(tree.condition());
		assertNotNull(tree.endKeyword());
		assertNotNull(tree.repeatKeyword2());
		assertNotNull(tree.label2());
		assertNotNull(tree.colon());
		assertNotNull(tree.semi());
	}
}
