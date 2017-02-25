package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class CaseStatementTest {

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
}
