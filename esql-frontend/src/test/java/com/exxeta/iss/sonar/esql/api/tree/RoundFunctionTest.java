package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class RoundFunctionTest {

	@Test
	public void roundFunction() {
		assertThat(Kind.ROUND_FUNCTION)
			.notMatches("ROUND(")
			.matches("ROUND (1,1)")
			.matches("ROUND(5.5, 0 MODE ROUND_FLOOR)");
	}

}
