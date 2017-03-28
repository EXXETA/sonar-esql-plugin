package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;

public class PrimaryExpressionTest {

	@Test
	public void realLife() {
		assertThat(EsqlLegacyGrammar.expression)
			.matches("'sss'")
			.matches("1")
			.matches("TRUE")
			.matches("1 + 1")
			.matches("0")				;
	}

}
