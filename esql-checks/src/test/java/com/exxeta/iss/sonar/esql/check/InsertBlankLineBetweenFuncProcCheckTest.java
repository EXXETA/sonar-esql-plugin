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
public class InsertBlankLineBetweenFuncProcCheckTest {
	@Test
	public void test() {
		EsqlCheck check = new InsertBlankLineBetweenFuncProcCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/InsertBlankLineBetweenFuncProc.esql"))
		.next().atLine(7).withMessage("Insert one blank line between functions and procedures.")
		.next().atLine(12).withMessage("Insert one blank line between functions and procedures.").noMore();
	}

}
