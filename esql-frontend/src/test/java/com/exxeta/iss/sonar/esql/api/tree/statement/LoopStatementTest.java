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

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

class LoopStatementTest extends EsqlTreeModelTest<LoopStatementTree>{
	@Test
	void loopStatement(){
		assertThat(Kind.LOOP_STATEMENT)
		.matches("X : LOOP  IF i>= 4 THEN LEAVE X; END IF;  SET i = i + 1;END LOOP X;")
		.matches("LOOP  IF i>= 4 THEN LEAVE X; END IF;  SET i = i + 1;END LOOP;");
	}
	
	@Test
	void modelTest() throws Exception{
		LoopStatementTree tree = parse("X : LOOP  IF i>= 4 THEN LEAVE X; END IF;  SET i = i + 1;END LOOP X;", Kind.LOOP_STATEMENT);
		assertNotNull(tree.label());
		assertNotNull(tree.colon());
		assertNotNull(tree.loopKeyword());
		assertNotNull(tree.statements());
		assertNotNull(tree.endKeyword());
		assertNotNull(tree.loopKeyword2());
		assertNotNull(tree.label2());
		assertNotNull(tree.semi());
		
	}
	
}
