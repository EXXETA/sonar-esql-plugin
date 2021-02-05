/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.function.OverlayFunctionTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class OverlayFunctionTest extends EsqlTreeModelTest<OverlayFunctionTree>{

	@Test
	public void overlayFunction() {
		assertThat(Kind.OVERLAY_FUNCTION)
			.matches("OVERLAY ('ABCDEFGHIJ' PLACING '1234' FROM 4 FOR 3)");
	}
	
	@Test
	public void modelTest() throws Exception{
		OverlayFunctionTree tree = parse("OVERLAY ('ABCDEFGHIJ' PLACING '1234' FROM 4 FOR 3)", Kind.OVERLAY_FUNCTION);
		assertNotNull(tree.overlayKeyword());
		assertNotNull(tree.openingParenthesis());
		assertNotNull(tree.sourceString());
		assertNotNull(tree.placingKeyword());
		assertNotNull(tree.sourceString2());
		assertNotNull(tree.fromKeyword());
		assertNotNull(tree.startPosition());
		assertNotNull(tree.forKeyword());
		assertNotNull(tree.stringLength());
		assertNotNull(tree.closingParenthesis());
		tree = parse("OVERLAY ('ABCDEFGHIJ' PLACING '1234' FROM 4 )", Kind.OVERLAY_FUNCTION);
		assertNotNull(tree.overlayKeyword());
		assertNotNull(tree.openingParenthesis());
		assertNotNull(tree.sourceString());
		assertNotNull(tree.placingKeyword());
		assertNotNull(tree.sourceString2());
		assertNotNull(tree.fromKeyword());
		assertNotNull(tree.startPosition());
		assertNull(tree.forKeyword());
		assertNull(tree.stringLength());
		assertNotNull(tree.closingParenthesis());
	}

}
