package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;

public class DataTypeTest {

	@Test
	public void realLife() {
		assertThat(EsqlLegacyGrammar.dataType)
			.matches("CHARACTER")
			.matches("BOOLEAN")
			.matches("CHAR")
			.matches("DECIMAL")
			.notMatches("STRING");
		
	}
	
}
