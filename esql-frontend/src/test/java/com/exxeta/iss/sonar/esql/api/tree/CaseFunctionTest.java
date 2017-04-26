package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class CaseFunctionTest {

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
}
