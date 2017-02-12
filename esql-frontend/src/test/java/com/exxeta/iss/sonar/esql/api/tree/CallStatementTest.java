package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class CallStatementTest {


	@Test
	public void callStatement(){
		assertThat(Kind.CALL_STATEMENT)
		.matches("CALL myProc1();")
		.matches("CALL myProc1() INTO cursor;")
		.matches("CALL myProc1() INTO OutputRoot.XMLNS.TestValue1;");

	}
	
	@Test
	public void fielReference(){
		assertThat(Kind.FIELD_REFERENCE)
		.matches("cursor");
	}
	
	
}
