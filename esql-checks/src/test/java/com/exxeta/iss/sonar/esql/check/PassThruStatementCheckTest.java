package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * @author Sapna Singh
 *
 */
public class PassThruStatementCheckTest  {
	

	@Test
	public void test() {
		EsqlCheck check = new PassThruStatementCheck();
		EsqlCheckVerifier.verify(check, new File("src/test/resources/PassthruStatementCheck.esql"));
	}
	
	
		

}
