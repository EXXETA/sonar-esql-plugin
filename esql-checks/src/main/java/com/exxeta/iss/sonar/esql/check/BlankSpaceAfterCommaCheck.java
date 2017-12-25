/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.lexer.EsqlLexer;

/**
 * This java class is created to check that blank space should follow each comma in any ESQL statement
 * @author C50679
 *
 */
@Rule(key = "BlankSpaceAfterComma")
public class BlankSpaceAfterCommaCheck extends DoubleDispatchVisitorCheck{

	private static final String MESSAGE = "A blank space should follow each comma in any ESQL statement that makes use of commas outside of a string literal.";



	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String>  lines = CheckUtils.readLines(file);
		
		int linecounter = 0;
		for (String line : lines) {
			linecounter = linecounter + 1;	
	       
			String upperCaseTheLine = line.toUpperCase().trim();
			if(upperCaseTheLine.contains(",") ){
				int pos= upperCaseTheLine.indexOf(',');
				
						if (!upperCaseTheLine.substring(pos+1,pos+2).matches(EsqlLexer.WHITESPACE) ) {
							addIssue(new LineIssue(this, linecounter, MESSAGE));
				}
			}
		} 
	}
}

