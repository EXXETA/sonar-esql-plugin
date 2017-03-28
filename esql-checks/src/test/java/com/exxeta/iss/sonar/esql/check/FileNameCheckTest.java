package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class FileNameCheckTest {
	@Test
	public void test_negative() {
		FileNameCheck check = new FileNameCheck();
		check.format = "^[a-z][a-zA-Z]{1,30}\\.esql$";
		EsqlCheckVerifier.issues(check, new File("src/test/resources/test.esql"))
				.noMore();
	}

	@Test
	public void test_positive() {
		FileNameCheck check = new FileNameCheck();
		check.format = "^[A-Z][a-zA-Z]{1,30}\\.esql$";
		EsqlCheckVerifier.issues(check, new File("src/test/resources/test.esql"))
				.next()
				.withMessage(
						"Rename file \"test.esql\"  to match the regular expression ^[A-Z][a-zA-Z]{1,30}\\.esql$.")
				.noMore();
	}

}
