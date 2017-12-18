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
public class ProcessInvokingItselfCheckTest {

	@Test
	public void test() {
		EsqlCheck check = new ProcessInvokingItselfCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/ProcessInvokingItself.esql"))
		.next().atLine(7).withMessage("process invoking itself.")
		.next().atLine(15).withMessage("process invoking itself.").noMore();
	}
}
