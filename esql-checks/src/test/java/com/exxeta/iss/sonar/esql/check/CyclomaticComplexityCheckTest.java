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
 * @author sapna singh
 *
 */
public class CyclomaticComplexityCheckTest {
	
	
	@Test
	public void test() {
		EsqlCheck check = new CyclomaticComplexityCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/CyclomaticComplexity.esql"))
		.next().atLine(2).withMessage("Refactor this function to reduce its Cognitive Complexity from 12 to the 10 allowed.")
		.next().atLine(33).withMessage("Refactor this procedure to reduce its Cognitive Complexity from 33 to the 10 allowed.")
		.noMore();
	}

}


