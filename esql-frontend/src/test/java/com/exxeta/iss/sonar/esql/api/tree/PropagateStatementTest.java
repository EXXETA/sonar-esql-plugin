package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class PropagateStatementTest {
	@Test
	public void propagateStatement(){
		assertThat(Kind.PROPAGATE_STATEMENT)
		.matches("PROPAGATE TO LABEL 'ABC';");

	}
	
	
}
