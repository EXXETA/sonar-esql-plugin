/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * This Test class is created to Test that all the EXTERNAL variable should be initialized in esql file.
 * @author C50679 (sapna.singh@infosys.com)
 *
 */
public class EXTERNALVariableInitialisedCheckTest {
	
	@Test
	public void test() {
		EsqlCheck check = new EXTERNALVariableInitialisedCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/EXTERNALVariableInitialised.esql")).next().atLine(1)
				.withMessage("EXTERNAL variable should be initialized").noMore();
	}
}
