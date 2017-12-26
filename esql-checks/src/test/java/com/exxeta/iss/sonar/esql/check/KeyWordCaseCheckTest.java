package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;
/**
 * This Test Class Is created to ensure that all the keywords in esql file should be in UPPER CASE
 * @author Sapna Singh
 *
 */
public class KeyWordCaseCheckTest {
	@Test
	public void test() {
		EsqlCheck check = new KeyWordCaseCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/KeyWordCaseCheck.esql"))
		.next().atLine(6).withMessage("This keyword should be in uppercase.")
		.next().atLine(25).withMessage("This keyword should be in uppercase.")
		.noMore();
	}
}
