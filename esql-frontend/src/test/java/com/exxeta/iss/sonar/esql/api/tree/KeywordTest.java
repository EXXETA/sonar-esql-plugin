package com.exxeta.iss.sonar.esql.api.tree;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

public class KeywordTest {

	@Test
	public void reservedKeywords(){
		assertThat(EsqlLegacyGrammar.reservedKeyword)
		.matches("WHEN")
		.matches("when")
		;
	}

	@Test
	public void nonReservedKeywords(){
		assertThat(EsqlLegacyGrammar.nonReservedKeyword)
		.matches("CALL")
		.matches("call")
		;
	}
	@Test
	public void keywords(){
		assertThat(EsqlLegacyGrammar.keyword)
		.matches("CALL")
		.matches("WHEN")
		.matches("when")
		;
	}
	
}
