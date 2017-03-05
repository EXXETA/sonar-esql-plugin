package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class RepeatStatementTest {
	@Test
	public void repeatStatement(){
		assertThat(Kind.REPEAT_STATEMENT)
		.matches("X : REPEAT SET i = i + 1; UNTIL  i>= 3 END REPEAT X;");

	}
	
}
