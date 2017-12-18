package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * This Java class is a test class to check function header comments
 * @author 
 *
 */
public class FunctionCommentsCheckTest {
	@Test
	public void test() {
				
		EsqlCheckVerifier.issues(new FunctionCommentsCheck(), new File("src/test/resources/functionComments.esql"))
        .next().atLine(3).withMessage("Document this function with all parameters and return types.")
        .next().atLine(15).withMessage("Document this function with all parameters and return types.")      
        .noMore();
	}
}
