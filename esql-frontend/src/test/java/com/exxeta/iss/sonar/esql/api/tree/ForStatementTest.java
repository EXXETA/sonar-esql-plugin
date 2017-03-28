package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class ForStatementTest {

	@Test
	public void forStatement() {
		assertThat(Kind.FOR_STATEMENT)
		.matches("FOR source AS Environment.SourceData.Folder[] DO END FOR;")
		.matches("FOR source AS Environment.SourceData.Folder[] DO SET A = 1; SET B = 2; END FOR;");

	}
	
}
