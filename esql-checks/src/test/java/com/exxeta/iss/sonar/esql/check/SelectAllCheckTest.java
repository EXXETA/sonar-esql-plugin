package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class SelectAllCheckTest {

	@Test
	public void test(){
		EsqlCheckVerifier.verify(new SelectAllCheck(), new File("src/test/resources/selectAll.esql"));
	}
}
