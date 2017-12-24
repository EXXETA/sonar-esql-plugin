/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * This Test class is created to check the length of a function or procedure.
 * @author Sapna Singh
 *
 */
public class FunctionProcedureLengthCheckTest {

	
	
	@Test
	public void test() {
		EsqlCheck check = new FunctionProcedureLengthCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/FunctionLength.esql"))
		.next().atLine(362).withMessage("This routine has 152 lines, which is greater than the 150 lines authorized.")
		.noMore();
	}

}


