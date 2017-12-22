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
public class FilterNodeHaveOnlyOneReturnCheckTest {
	@Test
	public void test() {
		EsqlCheck check = new FilterNodeHaveOnlyOneReturnCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/FilterNodeHaveOnlyOneReturn.esql")).next().atLine(1)
				.withMessage("The filter node may only have one return value").noMore();
	}

}
