package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class UnusedRoutineCheckTest {
	@Test
	public void test() throws Exception {
		UnusedRoutineCheck check = new UnusedRoutineCheck();
		
		
		EsqlCheckVerifier.issues(check, new File("src/test/resources/procedureName.esql"))
		.next()
		.atLine(5)
		.withMessage(
				"Unused procedure.")
		.next()
		.atLine(8)
		.withMessage(
				"Unused procedure.")
				
				.noMore();
}
}