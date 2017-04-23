package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class SubstringFunctionTest {

	@Test
	public void substringFunction() {
		assertThat(Kind.SUBSTRING_FUNCTION)
			.matches("SUBSTRING('Hello World!' FROM 7 FOR 4)")
			.matches("SUBSTRING('Hello World!' FROM 7)");
	}

}
