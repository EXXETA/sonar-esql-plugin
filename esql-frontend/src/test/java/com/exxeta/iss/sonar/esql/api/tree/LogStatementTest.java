package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class LogStatementTest {
	@Test
	public void moveStatement(){
		
		assertThat(Kind.LOG_STATEMENT)
		.matches("LOG EVENT SEVERITY 1 CATALOG 'BIPmsgs' MESSAGE 2951 VALUES(1,2,3,4);")
		.matches("LOG USER TRACE EXCEPTION VALUES(SQLSTATE, 'DivideByZero');");

	}
	
}
