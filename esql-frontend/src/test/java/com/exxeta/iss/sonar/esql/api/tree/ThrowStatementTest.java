package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class ThrowStatementTest {
	@Test
	public void throwStatement(){
		assertThat(Kind.THROW_STATEMENT)
		.matches("THROW USER EXCEPTION;")
		.matches("THROW USER EXCEPTION CATALOG 'BIPmsgs' MESSAGE 2951 VALUES(1,2,3,4,5,6,7,8) ;")
		.matches("THROW USER EXCEPTION CATALOG 'BIPmsgs' MESSAGE 2951 VALUES('The SQL State: ', SQLSTATE, 'The SQL Code: ', SQLCODE, 'The SQLNATIVEERROR: ', SQLNATIVEERROR,    'The SQL Error Text: ', SQLERRORTEXT ) ;")
		.matches("THROW USER EXCEPTION CATALOG 'BIPmsgs' MESSAGE 2951;");

	}
	
}
