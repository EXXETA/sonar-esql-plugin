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
import com.exxeta.iss.sonar.esql.tree.impl.function.WhenClauseExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class CaseFunctionTest extends EsqlTreeModelTest<CaseFunctionTree> {

	@Test
	public void whenClause() {
		assertThat(Kind.WHEN_CLAUSE_EXPRESSION).matches("WHEN '01' THEN 'January'");
	}

	@Test
	public void caseStatement() {
		assertThat(Kind.CASE_FUNCTION)
				.matches("CASE CurrentMonth " + "WHEN '01' THEN 'January' " + "WHEN '02' THEN 'February' "
						+ "WHEN '03' THEN 'March' " + "WHEN '04' THEN 'April' " + "WHEN '05' THEN 'May' "
						+ "WHEN '06' THEN 'June' " + "ELSE 'Second half of year' " + "END")
				.matches("CASE   " + "WHEN Month = '01' THEN 'January'  " + "WHEN Month = '02' THEN 'February'  "
						+ "WHEN Month = '03' THEN 'March'  " + "WHEN Month = '04' THEN 'April'  "
						+ "WHEN Month = '05' THEN 'May'  " + "WHEN Month = '06' THEN 'June'  "
						+ "ELSE 'Second half of year'  " + "END");
	}
	
	@Test
	public void modelTest() throws Exception{
		CaseFunctionTree tree = parse("CASE CurrentMonth " + "WHEN '01' THEN 'January' " + "WHEN '02' THEN 'February' "
						+ "WHEN '03' THEN 'March' " + "WHEN '04' THEN 'April' " + "WHEN '05' THEN 'May' "
						+ "WHEN '06' THEN 'June' " + "ELSE 'Second half of year' " + "END", Kind.CASE_FUNCTION);
		
		assertNotNull(tree);
		assertNotNull(tree.caseKeyword());
		assertNotNull(tree.sourceValue());
		assertNotNull(tree.whenClauses());
		assertNotNull(tree.elseKeyword());
		assertNotNull(tree.elseExpression());
		assertNotNull(tree.endKeyword());
		
		WhenClauseExpressionTreeImpl when = tree.whenClauses().get(0);
		assertNotNull(when);
		assertNotNull(when.whenKeyword());
		assertNotNull(when.expression());
		assertNotNull(when.thenKeyword());
		assertNotNull(when.resultValue());
	}
}
