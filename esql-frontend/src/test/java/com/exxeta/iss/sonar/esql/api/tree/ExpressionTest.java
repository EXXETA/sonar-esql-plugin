package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;

public class ExpressionTest {

	@Test
	public void expression() {
		assertThat(EsqlLegacyGrammar.expression)
		.matches("b")
		.matches("b.c")
		.matches("0");
	}
	
}
