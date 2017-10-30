/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;
import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * @author C50679
 *
 */

public class CaseStatementWithSingleWhenCheckTest {
	
	@Test
	public void test() {
		EsqlCheck check = new CaseStatementWithSingleWhenCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/CaseStatementWithSingleWhen.esql")).next().atLine(5)
				.withMessage("Replace this \"CASE\" statement by \"if\" statements to increase readability").noMore();
	}
}



