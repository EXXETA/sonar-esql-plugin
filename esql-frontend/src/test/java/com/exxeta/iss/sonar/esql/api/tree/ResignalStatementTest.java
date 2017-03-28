package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class ResignalStatementTest {
	@Test
	public void resignalStatement(){
		assertThat(Kind.RESIGNAL_STATEMENT)
		.matches("RESIGNAL;");

	}
	
}
