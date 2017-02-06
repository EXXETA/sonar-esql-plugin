package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;

public class LiteralTest {

	@Test
	public void stringLiteral() {
		assertThat(Kind.STRING_LITERAL)
		.matches("'a'")
		.matches("''");
	}
	
	@Test
	public void numericalLiteral(){
		assertThat(Kind.NUMERIC_LITERAL)
		.matches("1")
		.matches("0")
		.matches("11111")
		.matches("5.3");
	}
	
	@Test
	public void lietral(){
		assertThat(EsqlLegacyGrammar.LITERAL)
		.matches("0");
	}
	
}
