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
public class BlankSpaceAfterCommaCheckTest {
	@Test
	public void test() {
		EsqlCheck check = new BlankSpaceAfterCommaCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/BlankSpaceAfterComma.esql"))
		.next().atLine(4).withMessage("A blank space should follow each comma in any ESQL statement that makes use of commas outside of a string literal.")
		.next().atLine(10).withMessage("A blank space should follow each comma in any ESQL statement that makes use of commas outside of a string literal.").noMore();
	}

}
