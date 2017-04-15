package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class UnusedRoutineCheckTest {
	@Test
	public void procedures() throws Exception {
		UnusedRoutineCheck check = new UnusedRoutineCheck();
		
		
		EsqlCheckVerifier.issues(check, new File("src/test/resources/procedureName.esql"))
		.next()
		.atLine(5)
		.withMessage(
				"Remove the unused procedure \"tooLongProcedureNameBecauseItHasMoreThan30characters\".")
		.next()
		.atLine(8)
		.withMessage(
				"Remove the unused procedure \"procedureOk\".")
				
				.noMore();
}
	@Test
	public void functions() throws Exception {
		UnusedRoutineCheck check = new UnusedRoutineCheck();
		
		
		EsqlCheckVerifier.issues(check, new File("src/test/resources/functionName.esql"))
		.next()
		.atLine(5)
		.withMessage(
				"Remove the unused function \"too_long_function_name_because_it_has_more_than_30_characters\".")
		.next()
		.atLine(8)
		.withMessage(
				"Remove the unused function \"functionOk\".")
				
				.noMore();
}
}