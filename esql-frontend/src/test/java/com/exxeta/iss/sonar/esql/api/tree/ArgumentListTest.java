package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class ArgumentListTest {
	@Test
	public void argumentList() {
		assertThat(Kind.ARGUMENTS)
		.matches("(a,b,c,d)")
		.matches("(A())")
		.matches("('aaaa')")
		.matches("(TIME '00:00:00')");
	}
}
