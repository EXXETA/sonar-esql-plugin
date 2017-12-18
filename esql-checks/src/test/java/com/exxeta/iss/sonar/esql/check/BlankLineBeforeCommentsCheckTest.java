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
public class BlankLineBeforeCommentsCheckTest {
	@Test
	public void test() {
		EsqlCheck check = new BlankLineBeforeCommentsCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/BlankLineBeforeComments.esql"))
		.next().atLine(4).withMessage("Insert one blank line before a block or single-line comment.")
		.next().atLine(16).withMessage("Insert one blank line before a block or single-line comment.")
		.noMore();
	}

}
