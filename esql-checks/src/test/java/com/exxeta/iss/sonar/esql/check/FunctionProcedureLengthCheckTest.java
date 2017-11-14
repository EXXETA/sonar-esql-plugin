/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * This Test class is created to check the cyclomatic complexity of the code.
 * @author Arjav Shah
 *
 */
public class FunctionProcedureLengthCheckTest {

	
	
	@Test
	public void test() {
		EsqlCheck check = new FunctionProcedureLengthCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/FunctionLength.esql")).next().atLine(362)
				.withMessage("Procedure \"GetElectronicDocument\" is of length 153 lines, which is higher than the allowed threshold.(Threshold : 150)").noMore();
	}

}


