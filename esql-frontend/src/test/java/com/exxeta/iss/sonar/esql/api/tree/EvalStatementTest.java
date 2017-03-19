package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class EvalStatementTest {
	@Test
	public void evalStatement(){
		assertThat(Kind.EVAL_STATEMENT)
		.matches("EVAL('SET ' || scalarVar1 || ' = 2;');");

	}
	
}
