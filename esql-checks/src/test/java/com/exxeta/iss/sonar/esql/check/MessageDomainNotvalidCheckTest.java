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
public class MessageDomainNotvalidCheckTest {
	
	@Test
	public void test() {
		EsqlCheck check = new MessageDomainNotvalidCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/MessageDomainNotvalid.esql")).next().atLine(7)
				.withMessage("The message domain may not be valid.").noMore();
	}

}
