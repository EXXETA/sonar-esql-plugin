package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class DeleteFromStatementTest {


	@Test
	public void deleteFromStatement(){
		assertThat(Kind.DELETE_FROM_STATEMENT)
		.matches("DELETE FROM Database.SHAREHOLDINGS AS S WHERE S.ACCOUNTNO = InputBody.AccountNumber;");

	}
	
}
