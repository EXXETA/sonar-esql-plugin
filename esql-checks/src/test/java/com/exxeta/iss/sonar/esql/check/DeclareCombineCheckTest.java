/**
 * This java class is created to implement the logic for checking if variable is initialised or not, 
 * if more than one variable of same datatype is found uninitialised then declare statement could be combined.
 * 
 * 
 * @author Prerana Agarkar
 *
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.File;
import org.junit.Test;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class DeclareCombineCheckTest {

	@Test
	  public void test() {
			 EsqlCheckVerifier.issues(new DeclareCombineCheck(), new File("src/test/resources/declareCombine.esql"))
			    .next().atLine(6).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(8).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(10).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(12).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(14).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(16).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(18).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(20).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(24).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(27).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(28).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(29).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(30).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(32).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(34).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(36).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(38).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(40).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(42).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(44).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(46).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(48).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(50).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(52).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .next().atLine(56).withMessage("If  more than one variable of same datatype is found uninitialised then declare could be combined.")
			    .noMore();
		        
	  }
}