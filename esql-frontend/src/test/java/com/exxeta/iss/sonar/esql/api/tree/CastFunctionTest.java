package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class CastFunctionTest {

	@Test
	public void castFunction() {
		assertThat(Kind.CAST_FUNCTION)
			.matches("CAST(source AS DATE FORMAT pattern)")
			.matches("CAST(7, 6, 5 AS DATE)")
			.matches("CAST(7.4e0, 6.5e0, 5.6e0 AS DATE)")
			.matches("CAST(3.1e0, 4.2e0, 5.3e0, 6.4e0, 7.5e0, 8.6789012e0 AS GMTTIMESTAMP)");
	}
}
