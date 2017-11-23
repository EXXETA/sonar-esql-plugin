package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * @author C50679
 *
 */
public class PassThruStatementCheckTest  {
	

	@Test
	public void test() {
		EsqlCheck check = new PassThruStatementCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/PassthruStatementCheck.esql")).next().atLine(9)
				.withMessage("Use parameter markers '?' when using the PASSTHRU statement in ESQL").noMore();
	}
	
	
		

}