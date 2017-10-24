package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class VariableNameStartsWithLowercaseCheckTest {
	@Test
	public void test() throws Exception {
		VariableNameStartsWithLowercaseCheck check = new VariableNameStartsWithLowercaseCheck();
		EsqlCheckVerifier.issues(check, new File("src/test/resources/variableNameStartsWithLowercase.esql")).next()
		.atLine(5)
		.withMessage("Rename variable \"Ref1\". Variable name should start with lowercase.")
		.next().atLine(6)
		.withMessage("Rename variable \"deployEnvironment\". Variable name should start with lowercase.")
		.next().atLine(7)
				.withMessage("Rename variable \"A\". Variable name should start with lowercase.").next()
				.atLine(15)
				.withMessage("Rename variable \"ANAS\". Variable name should start with lowercase.")
				.noMore();
	}
	public static void main(String args[]) throws Exception
	{
		new VariableNameStartsWithLowercaseCheckTest().test();
		
	}
}
