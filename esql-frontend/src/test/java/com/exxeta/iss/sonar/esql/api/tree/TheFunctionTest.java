package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class TheFunctionTest {

	@Test
	public void theFunction() {
		assertThat(Kind.THE_FUNCTION)
			.notMatches("THE(")
			.matches("THE (getList())");
	}

}
