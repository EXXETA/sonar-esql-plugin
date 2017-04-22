package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class PositionFunctionTest {

	@Test
	public void positionFunction() {
		assertThat(Kind.POSITION_FUNCTION)
			.matches("POSITION('Village' IN 'Hursley Village')")
			.matches("POSITION ('A' IN 'ABCABCABCABCABC' FROM 4)")
			.matches("POSITION ('B' IN 'ABCABCABCABCABC' REPEAT 2)")
			.matches("POSITION ('A' IN 'ABCABCABCABCABC' FROM 4 REPEAT 2)");
	}

}
