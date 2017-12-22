/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * @author Sapna Singh
 *
 */
public class MeaningfulVariableCheckTest {
	@Test
	public void test() throws Exception {
		EsqlCheck check = new MeaningfulVariableCheck();
		 EsqlCheckVerifier.issues(check, new File(
				"src/test/resources/MeaningfulVariable.esql"))
				.next()
				.atLine(5)
				.withMessage(
						"Rename variable \"re\" to match the regular expression ^[a-z][a-zA-Z0-9]{3,30}$.")
				.next()
				.atLine(6)
				.withMessage(
						"Rename variable \"x\" to match the regular expression ^[a-z][a-zA-Z0-9]{3,30}$.")
				.noMore();
	}
	

}
