package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class UpdateStatementTest {


	@Test
	public void updateStatement(){
		assertThat(Kind.UPDATE_STATEMENT)
		.matches("UPDATE Database.StockPrices AS SP SET PRICE = InputBody.Message.StockPrice WHERE SP.COMPANY = InputBody.Message.Company;");

	}
	
}
