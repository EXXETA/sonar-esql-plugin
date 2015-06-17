package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import com.exxeta.iss.sonar.esql.EsqlAstScanner;
import com.exxeta.iss.sonar.esql.check.FileNameCheck;

public class FileNameCheckTest {
	@Test
	public void test_negative() {
		SourceFile file = scanFile("^[a-z][a-zA-Z]{1,30}\\.esql$");
		CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
	}

	@Test
	public void test_positive() {
		SourceFile file = scanFile("^[A-Z][a-zA-Z]{1,30}\\.esql$");
		CheckMessagesVerifier
				.verify(file.getCheckMessages())
				.next()
				.withMessage(
						"Rename file \"test.esql\"  to match the regular expression ^[A-Z][a-zA-Z]{1,30}\\.esql$.")
				.noMore();
	}

	private SourceFile scanFile(String format) {
		FileNameCheck check = new FileNameCheck();
		check.format = format;
		return EsqlAstScanner.scanSingleFile(new File(
				"src/test/resources/test.esql"), check);
	}
}
