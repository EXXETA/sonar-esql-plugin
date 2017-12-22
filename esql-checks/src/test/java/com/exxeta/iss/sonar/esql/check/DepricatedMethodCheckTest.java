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
public class DepricatedMethodCheckTest {
	@Test
	public void test() {
		EsqlCheck check = new DepricatedMethodCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/DepricatedMethod.esql"))
		.next().atLine(20).withMessage("Depricated methods should not be used.").noMore();
	
	}	


}
