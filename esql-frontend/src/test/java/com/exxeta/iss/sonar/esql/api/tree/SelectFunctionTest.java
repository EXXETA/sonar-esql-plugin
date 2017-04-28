package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class SelectFunctionTest {

	@Test
	public void selectFunction() {
		assertThat(Kind.SELECT_FUNCTION)
		.matches("SELECT P.PartNumber AS a,  P.Description,  P.Price FROM PartsTable.Part[]")
		.matches("SELECT * FROM Database.Datasource.SchemaName.Table As A")
		.matches("SELECT P.PartNumber AS a,  P.Description,  P.Price FROM PartsTable.Part[] AS P")
		;
	}
	
	@Test
	public void selectClause(){
		assertThat(Kind.SELECT_CLAUSE)
		.matches("P.PartNumber,  P.Description,  P.Price");
	}

	@Test
	public void fromClause(){
		assertThat(Kind.FROM_CLAUSE_EXPRESSION)
		.matches("FROM PartsTable.Part[] AS P")
		.matches("FROM Database.Datasource.SchemaName.Table As A")
		;
	}

	@Test
	public void whereClause(){
		assertThat(Kind.WHERE_CLAUSE)
		.matches("WHERE A = B");
	}

}
