package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class IterateStatementCheckTest {
	@Test
	public void test() {
		/*
		 * IterateStatementCheck check = new IterateStatementCheck();
		 * 
		 * SourceFile file =EsqlAstScanner.scanSingleFile(new
		 * File("src/test/resources/leaveIterate.esql"), check);
		 * CheckMessagesVerifier.verify(file.getCheckMessages())
		 * .next().atLine(13).withMessage("Avoid using ITERATE statement.")
		 * .next().atLine(17).withMessage("Avoid using ITERATE statement.")
		 * .noMore(); }
		 */

		EsqlCheckVerifier.issues(new IterateStatementCheck(), new File("src/test/resources/leaveIterate.esql"))
		 .next().atLine(13).withMessage("Avoid using ITERATE statement.")
		 .next().atLine(17).withMessage("Avoid using ITERATE statement.")
		 .noMore();
	}

}
