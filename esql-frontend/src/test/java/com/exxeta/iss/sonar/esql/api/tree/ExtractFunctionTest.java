package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class ExtractFunctionTest {
	@Test
	public void extractFunction(){
		
		assertThat(Kind.EXTRACT_FUNCTION)
		.matches("EXTRACT(YEAR FROM CURRENT_DATE)")
		.matches("EXTRACT (DAYS FROM DATE '2000-02-29')")
		.matches("EXTRACT (DAYOFYEAR FROM CURRENT_TIME)");

	}
	
}
