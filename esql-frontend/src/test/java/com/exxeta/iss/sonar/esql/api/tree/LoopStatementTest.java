package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class LoopStatementTest {
	@Test
	public void callStatement(){
		assertThat(Kind.LOOP_STATEMENT)
		.matches("X : LOOP  IF i>= 4 THEN LEAVE X; END IF;  SET i = i + 1;END LOOP X;");

	}
	
}
