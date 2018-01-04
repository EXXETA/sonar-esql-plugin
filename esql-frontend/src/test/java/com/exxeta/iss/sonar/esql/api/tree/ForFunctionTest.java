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
package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.tree.impl.function.ForFunctionTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class ForFunctionTest extends EsqlTreeModelTest<ForFunctionTreeImpl>{

	@Test
	public void forFunction() {
		assertThat(Kind.FOR_FUNCTION)
			.matches("FOR ALL Body.Invoice.Purchases.\"Item\"[] AS I (I.Quantity <= 50)")
			.matches("FOR ALL Body.Invoice.Purchases.\"Item\"[] (Quantity <= 50)");
	}

	@Test
	public void modelTest() throws Exception{
		ForFunctionTreeImpl tree = parse("FOR ALL Body.Invoice.Purchases.\"Item\"[] AS I (I.Quantity <= 50)", Kind.FOR_FUNCTION);
		
		assertNotNull(tree);
		
		assertNotNull(tree.forKeyword());
		assertNotNull(tree. qualifier());
		assertNotNull(tree.fieldReference());
		assertNotNull(tree.asKeyword());
		assertNotNull(tree.asIdentifier());
		assertNotNull(tree.openingParenthesis());
		assertNotNull(tree.expression());
		assertNotNull(tree.closingParenthesis());
	}
	
}
