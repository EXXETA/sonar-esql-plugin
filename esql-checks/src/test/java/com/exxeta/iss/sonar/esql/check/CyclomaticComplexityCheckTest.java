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
	
//	@Test
//	public void test1() {
//		CyclomaticComplexityCheck check = new CyclomaticComplexityCheck();
//		check.totalComplexity = 5;
//		EsqlCheckVerifier.verify(check, new File("src/test/resources/CyclomaticComplexity.esql"));
//	}
	
	
	@Test
	public void test() {
		EsqlCheck check = new CyclomaticComplexityCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/CyclomaticComplexity.esql")).next().atLine(2)
				.withMessage("Check Function \"Main\". Cyclomatic Complexity is higher then the threshold.").noMore();
	}

}


