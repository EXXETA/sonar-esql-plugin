/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class ExtractFunctionTest extends EsqlTreeModelTest<ExtractFunctionTree>{
	@Test
	public void extractFunction(){
		assertThat(Kind.EXTRACT_FUNCTION)
		.matches("EXTRACT(YEAR FROM CURRENT_DATE)")
		.matches("EXTRACT (DAYS FROM DATE '2000-02-29')")
		.matches("EXTRACT (DAYOFYEAR FROM CURRENT_TIME)");
	}
	
	
	@Test
	public void modelTest() throws Exception{
		ExtractFunctionTree tree = parse("EXTRACT(YEAR FROM CURRENT_DATE)", Kind.EXTRACT_FUNCTION);
		
		assertTrue(tree.extractKeyword()!=null);
		assertTrue(tree.openingParenthesis()!=null);
		assertTrue(tree.type()!=null);
		assertTrue(tree.fromKeyword()!=null);
		assertTrue(tree.sourceDate()!=null);
		assertTrue(tree.closingParenthesis()!=null);
	}
	
}
