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
public class BinaryOperatorSepratedBySpaceCheckTest {
	
	@Test
	public void test() {
		EsqlCheck check = new BinaryOperatorSepratedBySpaceCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/BinaryOperatorSpace.esql"))
		.next().atLine(5).withMessage("All binary operators should be separated from their operands by spaces.")
		.next().atLine(5).withMessage("All binary operators should be separated from their operands by spaces.")
		.next().atLine(6).withMessage("All binary operators should be separated from their operands by spaces.")
		.next().atLine(7).withMessage("All binary operators should be separated from their operands by spaces.")
		.next().atLine(8).withMessage("All binary operators should be separated from their operands by spaces.")
		.next().atLine(12).withMessage("All binary operators should be separated from their operands by spaces.").noMore();
	}

}
