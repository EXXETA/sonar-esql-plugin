package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import com.exxeta.iss.sonar.esql.EsqlAstScanner;
import com.exxeta.iss.sonar.esql.check.TooManyLinesInFileCheck;
import com.exxeta.iss.sonar.esql.check.UseBrokerSchemaCheck;

public class UseBrokerSchemaCheckTest {
	@Test
	public void test_negative() {
		UseBrokerSchemaCheck check = new UseBrokerSchemaCheck();
		SourceFile file = EsqlAstScanner.scanSingleFile(new File(
				"src/test/resources/withSchema.esql"), check);
		CheckMessagesVerifier.verify(file.getCheckMessages()).noMore();
	}

	@Test
	public void test_positive() {
		UseBrokerSchemaCheck check = new UseBrokerSchemaCheck();
		SourceFile file = EsqlAstScanner.scanSingleFile(new File(
				"src/test/resources/noSchema.esql"), check);
		CheckMessagesVerifier
				.verify(file.getCheckMessages())
				.next()
				.withMessage(
						"The default Schema should not be used. Move the file to a broker schema.")
				.noMore();
	}

}
