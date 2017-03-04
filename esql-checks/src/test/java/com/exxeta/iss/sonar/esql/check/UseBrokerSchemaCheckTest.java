package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class UseBrokerSchemaCheckTest {
	@Test
	public void test_negative() {
		UseBrokerSchemaCheck check = new UseBrokerSchemaCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/withSchema.esql")).noMore();
	}

	@Test
	public void test_positive() {
		UseBrokerSchemaCheck check = new UseBrokerSchemaCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/noSchema.esql")).next()
				.withMessage("The default Schema should not be used. Move the file to a broker schema.").noMore();
	}

}
