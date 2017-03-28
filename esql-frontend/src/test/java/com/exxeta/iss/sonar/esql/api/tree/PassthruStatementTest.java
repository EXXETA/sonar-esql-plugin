package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class PassthruStatementTest {


	@Test
	public void passthruStatement(){
		assertThat(Kind.PASSTHRU_STATEMENT)
		.matches("PASSTHRU 'CREATE TABLE Shop.Customers (  CustomerNumber INTEGER,  FirstName      VARCHAR(256),  LastName       VARCHAR(256),  Street         VARCHAR(256),  City           VARCHAR(256),  Country        VARCHAR(256))' TO Database.DSN1;");

	}
	
}
 