package com.exxeta.iss.sonar.esql.check;

	import java.io.File;

	import org.junit.Test;

	import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

	/**
	 * This Java class is a test class to check procedure header comments
	 * @author
	 *
	 */
	public class ProcedureCommentsCheckTest {
		@Test
		public void test() {
					
			EsqlCheckVerifier.issues(new ProcedureCommentsCheck(), new File("src/test/resources/procedureComments.esql"))
	        .next().atLine(3).withMessage("Document this procedure with all parameters.")
	        .next().atLine(12).withMessage("Document this procedure with all parameters.")      
	        .noMore();
		}
	}

