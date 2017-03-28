package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class MoveStatementTest {
	@Test
	public void moveStatement(){
		
		assertThat(Kind.MOVE_STATEMENT)
		.matches("MOVE sourceCursor NEXTSIBLING;")
		.matches("MOVE cursor FIRSTCHILD TYPE Name NAME 'Field1';");

	}
	
}
