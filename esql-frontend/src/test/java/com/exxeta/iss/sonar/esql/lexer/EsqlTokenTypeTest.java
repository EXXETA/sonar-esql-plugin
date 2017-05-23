package com.exxeta.iss.sonar.esql.lexer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class EsqlTokenTypeTest {
	@Test
	public void test() {
		assertThat(EsqlTokenType.values().length).isEqualTo(3);

		for (EsqlTokenType type : EsqlTokenType.values()) {
			assertThat(type.getName()).isEqualTo(type.name());
			assertThat(type.getValue()).isEqualTo(type.name());
		}
	}

	@Test(expected = IllegalStateException.class)
	public void hasToBeSkippedFromAst() throws Exception {
		EsqlTokenType.IDENTIFIER.hasToBeSkippedFromAst(null);
	}
}
