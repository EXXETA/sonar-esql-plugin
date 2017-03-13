package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class DeleteStatementTest {

	@Test
	public void deleteStatement() {
		assertThat(Kind.DELETE_STATEMENT)
		.matches("DELETE FIELD OutputRoot.XMLNS.Data.Folder1.Folder12;")
		.matches("DELETE LASTCHILD OF Cursor;");
	}
	
}
