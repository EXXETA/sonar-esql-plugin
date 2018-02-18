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
package com.exxeta.iss.sonar.esql.api.tree.function;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class RoundFunctionTest extends EsqlTreeModelTest<RoundFunctionTree>{

	@Test
	public void roundFunction() {
		assertThat(Kind.ROUND_FUNCTION)
			.notMatches("ROUND(")
			.matches("ROUND (1,1)")
			.matches("ROUND(5.5, 0 MODE ROUND_FLOOR)");
	}
	
	@Test
	public void modelTest() throws Exception{
		RoundFunctionTree tree = parse("ROUND(5.5, 0 MODE ROUND_FLOOR)", Kind.ROUND_FUNCTION);
		
		assertNotNull(tree.roundKeyword());
		assertNotNull(tree.openingParenthesis());
		assertNotNull(tree.sourceNumber());
		assertNotNull(tree.comma());
		assertNotNull(tree.precision());
		assertNotNull(tree.modeKeyword());
		assertNotNull(tree.roundingMode());
		assertNotNull(tree.closingParenthesis());
	}

}
