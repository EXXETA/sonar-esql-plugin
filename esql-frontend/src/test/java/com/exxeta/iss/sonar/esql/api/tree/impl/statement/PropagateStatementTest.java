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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.ControlsTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.MessageSourceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PropagateStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class PropagateStatementTest extends EsqlTreeModelTest<PropagateStatementTree>{
	@Test
	public void propagateStatement(){
		assertThat(Kind.PROPAGATE_STATEMENT)
		.matches("PROPAGATE TO LABEL 'ABC';")
		.matches("PROPAGATE;")
		.matches("PROPAGATE ENVIRONMENT 'DEV' MESSAGE 'msg' EXCEPTION 'exc';");
		

	}
	
	@Test
	public void modelTest() throws Exception{
		PropagateStatementTree tree = parse("PROPAGATE TO LABEL 'abc' FINALIZE NONE DELETE NONE;", Kind.PROPAGATE_STATEMENT);
		assertNotNull(tree.propagateKeyword());
		assertNotNull(tree.toKeyword());
		assertNotNull(tree.targetType());
		assertNotNull(tree.target());
		assertNotNull(tree.messageSource());
		assertNotNull(tree.controls());
		assertNotNull(tree.semi());
		MessageSourceTree messageSource = tree.messageSource();
		
		assertNull(messageSource.environmentKeyword());
		assertNull(messageSource.environment());
		assertNull(messageSource.messageKeyword()); 
		assertNull(messageSource.message()); 
		assertNull(messageSource.exceptionKeyword());
		assertNull(messageSource.exception());
		
		
		ControlsTree controls = tree.controls();
		
		assertNotNull(controls.finalizeKeyword());
		assertNotNull(controls.finalizeType());
		assertNotNull(controls.deleteKeyword());
		assertNotNull(controls.deleteType());
	}
	
	
}
