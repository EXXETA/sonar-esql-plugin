package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class CardinalityInLoopCheckTest {
	@Test
	public void test() {
		EsqlCheckVerifier.issues(new CardinalityInLoopCheck(), new File("src/test/resources/cardinalityInLoopTest.esql"))
		.next().atLine(5).withMessage("Avoid using CARDINALITY in loops.")
		.next().atLine(18).withMessage("Avoid using CARDINALITY in loops.")
		.next().atLine(25).withMessage("Avoid using CARDINALITY in loops.")
			.noMore();
	}
}
