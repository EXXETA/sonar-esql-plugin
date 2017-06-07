package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class ParameterWithDirectionCheckTest {

	@Test
	public void test(){
		
	EsqlCheckVerifier.issues(new ParameterWithDirectionCheck(), new File("src/test/resources/parameterWithDirection.esql"))
    .next().atLine(3).withMessage("Add a direction to the parameter \"b\".")
    .next().atLine(8).withMessage("Add a direction to the parameter \"bb\".")
    .noMore();
	}

}
