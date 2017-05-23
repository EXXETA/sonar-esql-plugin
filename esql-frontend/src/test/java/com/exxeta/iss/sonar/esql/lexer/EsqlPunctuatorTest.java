package com.exxeta.iss.sonar.esql.lexer;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EsqlPunctuatorTest {
	@Test
	public void test() {
		assertThat(EsqlPunctuator.values().length).isEqualTo(21);

		for (EsqlPunctuator punctuator : EsqlPunctuator.values()) {
			assertThat(punctuator.getName()).isEqualTo(punctuator.name());
		}
	}

	@Test(expected = IllegalStateException.class)
	public void hasToBeSkippedFromAst() throws Exception {
		EsqlPunctuator.COMMA.hasToBeSkippedFromAst(null);
	}
}
