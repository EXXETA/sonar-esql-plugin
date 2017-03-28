package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class DeclareHandlerStatementTest {


	@Test
	public void declareHandlerStatement(){
		assertThat(Kind.DECLARE_HANDLER_STATEMENT)
		.matches("DECLARE EXIT HANDLER FOR SQLSTATE VALUE 'U11222' SET a = 1;");

	}
	
}
