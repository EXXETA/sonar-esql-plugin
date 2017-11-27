/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * @author C50679
 *
 */
public class FilterNodeModifyMessageCheckTest {
	@Test
	public void test() {
		EsqlCheck check = new FilterNodeModifyMessageCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/FilterNodeModifyMessage.esql")).next().atLine(4)
				.withMessage("The filter node cannot modify the message").noMore();
	}

}
