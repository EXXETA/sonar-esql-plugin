package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class WhileStatementTest {
	@Test
	public void whileStatement(){
		assertThat(Kind.WHILE_STATEMENT)
		.matches("X : WHILE i <= 3 DO SET i = i + 1;  END WHILE X;");

	}
	
}
