/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * @author sapna singh
 *
 */
public class AvoidNestedIfCheckTest {

	
	@Test
	public void test() {
		EsqlCheck check = new AvoidNestedIfCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/AvoidNestedIf.esql"))
		.next().atLine(9).withMessage("This if has a nesting level of 4 which is higher than the maximum allowed 3Avoid nested IF statements: use ELSEIF or CASE WHEN clauses to get quicker drop-out.")
		.next().atLine(13).withMessage("This if has a nesting level of 4 which is higher than the maximum allowed 3Avoid nested IF statements: use ELSEIF or CASE WHEN clauses to get quicker drop-out.").noMore();
	}
	
}
