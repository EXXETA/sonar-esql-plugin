package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class ForFunctionTest {

	@Test
	public void forFunction() {
		assertThat(Kind.FOR_FUNCTION)
			.matches("FOR ALL Body.Invoice.Purchases.\"Item\"[] AS I (I.Quantity <= 50)");
	}

}
