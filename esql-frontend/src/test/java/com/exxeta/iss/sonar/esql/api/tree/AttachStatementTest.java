package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class AttachStatementTest {
	@Test
	public void attachStatement(){
		assertThat(Kind.ATTACH_STATEMENT)
		.matches("ATTACH ref1 TO OutputRoot.XMLNSC.Data.Order[2] AS LASTCHILD;");

	}
	
}
