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
package com.exxeta.iss.sonar.esql.api.tree.statement;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.utils.EsqlTreeModelTest;

public class CaseStatementTest extends EsqlTreeModelTest<CaseStatementTree>{

	@Test
	public void whenClause() {
		assertThat(Kind.WHEN_CLAUSE).matches("WHEN minimum + 0 THEN").matches("WHEN minimum + 0 THEN SET a=1;")
				.matches("WHEN minimum + 0 THEN SET a=1; SET b=3;");
	}

	@Test
	public void caseStatement() {
		assertThat(Kind.CASE_STATEMENT)
				.matches("CASE size\n  WHEN minimum + 0 THEN\n"
						+ "    SET description = 'small';\n  WHEN minimum + 1 THEN\n"
						+ "    SET description = 'medium';\n" + "       \n  WHEN minimum + 2 THEN\n"
						+ "    SET description = 'large';\n    CALL handleLargeObject();\n"
						+ "  ELSE\n    SET description = 'unknown';\n" + "    CALL handleError();\nEND CASE;")
				.matches("CASE\n" + "	WHEN i <> 0 THEN\n" + "    CALL handleI(i);\n" + "  WHEN j> 1 THEN\n"
						+ "    CALL handleIZeroAndPositiveJ(j);\n" + "  ELSE\n" + "    CALL handleAllOtherCases(j);\n"
						+ "END CASE;");
	}
	
	@Test
	public void modelTest() throws Exception{
		CaseStatementTree tree = parse("CASE" + " WHEN i <> 0 THEN\n" + "    CALL handleI(i);\n" + "  WHEN j> 1 THEN\n"
						+ "    CALL handleIZeroAndPositiveJ(j);\n" + "  ELSE\n" + "    CALL handleAllOtherCases(j);\n"
						+ "END CASE;", Kind.CASE_STATEMENT);
		
		assertNotNull(tree.caseKeyword());
		assertNull(tree.mainExpression());
		assertNotNull(tree.whenClauses());

		assertNotNull(tree.whenClauses().get(0).whenKeyword());
		assertNotNull(tree.whenClauses().get(0).expression());
		assertNotNull(tree.whenClauses().get(0).thenKeyword());
		assertNotNull(tree.whenClauses().get(0).statements());
		
		assertNotNull(tree.elseKeyword());
		assertNotNull(tree.elseSatements());
		assertNotNull(tree.endKeyword());
		assertNotNull(tree.caseKeyword2());
		assertNotNull(tree.semi());
	}
}
