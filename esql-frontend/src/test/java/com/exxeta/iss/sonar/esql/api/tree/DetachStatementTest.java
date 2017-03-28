package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class DetachStatementTest {
	@Test
	public void detachStatement(){
		assertThat(Kind.DETACH_STATEMENT)
		.matches("DETACH OutputRoot.test;");

	}
	
}
