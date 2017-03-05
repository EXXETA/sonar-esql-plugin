package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class ReturnStatementTest {
	@Test
	public void returnStatement(){
		assertThat(Kind.RETURN_STATEMENT)
		.matches("RETURN;")
		.matches("RETURN (PriceTotal/NumItems> 42);");

	}
	
}
