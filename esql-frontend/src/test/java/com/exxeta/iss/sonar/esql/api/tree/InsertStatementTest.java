package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class InsertStatementTest {


	@Test
	public void insertStatement(){
		assertThat(Kind.INSERT_STATEMENT)
		.matches("INSERT INTO Database.{Source}.{Schema}.{Table} (Name, Value) values ('Joe', 12.34);");

	}
	
}
