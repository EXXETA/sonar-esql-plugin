package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import com.exxeta.iss.sonar.esql.EsqlAstScanner;

public class IterateStatementCheckTest {
		 @Test
		  public void test() {
			 IterateStatementCheck check = new IterateStatementCheck();
		    
		    SourceFile file =EsqlAstScanner.scanSingleFile(new File("src/test/resources/leaveIterate.esql"), check);
		    CheckMessagesVerifier.verify(file.getCheckMessages())
		        .next().atLine(13).withMessage("Avoid using ITERATE statement.")
		        .next().atLine(17).withMessage("Avoid using ITERATE statement.")
		        .noMore();
		  }

}
