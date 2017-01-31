package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class SetStatementTest {

	@Test
	public void fieldReference() {
		assertThat(Kind.SET_STATEMENT)
		.matches("SET a=b")
		.matches("SET a=b.c");
	}
	
}
