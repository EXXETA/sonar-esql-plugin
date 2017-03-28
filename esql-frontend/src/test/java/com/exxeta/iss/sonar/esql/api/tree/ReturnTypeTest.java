package com.exxeta.iss.sonar.esql.api.tree;

import static com.exxeta.iss.sonar.esql.utils.Assertions.assertThat;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;

public class ReturnTypeTest {

	@Test
	public void realLife() {
		assertThat(Kind.RETURN_TYPE)
			.matches("RETURNS CHARACTER");
		
	}
	
}
