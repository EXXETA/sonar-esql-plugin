package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class PassthruFunctionTest {


	@Test
	public void passthruStatement(){
		assertThat(Kind.PASSTHRU_FUNCTION)
		.matches("PASSTHRU('SELECT R.* FROM Schema1.Table1 AS R WHERE R.Name = ? OR R.Name =            ? ORDER BY Name'   TO Database.DSN1   VALUES ('Name1', 'Name4'))");

	}
	
}
 