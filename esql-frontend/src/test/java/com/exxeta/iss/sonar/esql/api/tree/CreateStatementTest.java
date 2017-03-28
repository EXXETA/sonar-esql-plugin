package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;


public class CreateStatementTest {
	@Test
	public void domainClause(){
		assertThat(Kind.REPEAT_CLAUSE)
		.matches("REPEAT");
	}	
	
	@Test
	public void valueClause(){
		assertThat(Kind.VALUES_CLAUSE)
		.matches("VALUE 'Element 2 Value'");
	}
	
	@Test
	public void createStatement(){
		assertThat(Kind.CREATE_STATEMENT)
		.matches("CREATE FIELD OutputRoot.XMLNS.Data;")
		.matches("CREATE LASTCHILD OF OutputRoot.XMLNS.TestCase.Root IDENTITY (XML.Element)NSpace1:Element1[2] VALUE 'Element 2 Value';");
	}
	
	
}
