package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class BooleanInversionCheckTest {
	@Test
	public void test() {
		EsqlCheckVerifier.issues(new BooleanInversionCheck(), new File("src/test/resources/booleanInversion.esql"))
			.next().atLine(5).withMessage("Use the opposite operator (\"<\") instead.")
			.next().atLine(6).withMessage("Use the opposite operator (\">=\") instead.")
			.noMore();
	}
}
