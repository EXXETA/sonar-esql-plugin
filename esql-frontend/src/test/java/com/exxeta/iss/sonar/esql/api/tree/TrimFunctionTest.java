package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class TrimFunctionTest {

	@Test
	public void trimFunction() {
		assertThat(Kind.TRIM_FUNCTION)
			.matches("TRIM(TRAILING 'b' FROM 'aaabBb')")
			.matches("TRIM('  a  ')")
			.matches("TRIM(LEADING FROM '  a  ')");
	}

}
