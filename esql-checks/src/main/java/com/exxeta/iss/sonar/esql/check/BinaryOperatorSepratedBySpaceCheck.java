/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.lexer.EsqlLexer;

/**
 * This Java class is created to implement the logic for all binary operators should be separated from their operands by spaces.
 * @author C50679
 *
 */
@Rule(key = "BinaryOperatorSepratedBySpace")
public class BinaryOperatorSepratedBySpaceCheck extends DoubleDispatchVisitorCheck{
	
	private static final String MESSAGE = "All binary operators should be separated from their operands by spaces.";
	
	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String>  lines = CheckUtils.readLines(file);
		int i = 0;
		
		for (String line : lines) {
			i = i + 1;	
        String  thelines = line.toString();
	
		String upperCaseTheLine = thelines.toUpperCase().trim();
		for (String operator : BINARY_OPERATOR) {
			
			int size = operator.length();
			int pos1 = upperCaseTheLine.indexOf(operator);
			if(upperCaseTheLine.contains(operator) && ( pos1!= -1) && (size==1)){
				
				 int pos = upperCaseTheLine.indexOf(operator);
				 
				if (!upperCaseTheLine.substring(pos-1, pos+1).matches(EsqlLexer.WHITESPACE)  && !upperCaseTheLine.substring(pos+1, pos+2).matches(EsqlLexer.WHITESPACE)) {
					addIssue(new LineIssue(this, i, MESSAGE));
			} 
				else if (!upperCaseTheLine.substring(pos-1, pos).matches(EsqlLexer.WHITESPACE) && !upperCaseTheLine.substring(pos+2, pos+3).matches(EsqlLexer.WHITESPACE)){
				addIssue(new LineIssue(this, i, MESSAGE));
			}
		  }
			
				
		
		}
	}
  }

	
	
	public static final Set<String> BINARY_OPERATOR;
	 static{
		 BINARY_OPERATOR = new HashSet<String>();
		 BINARY_OPERATOR.add("=");
		 BINARY_OPERATOR.add(">");
		 BINARY_OPERATOR.add("<");
		 BINARY_OPERATOR.add("=<");
		 BINARY_OPERATOR.add(">=");
		 BINARY_OPERATOR.add("<>");
		 
    
	 }
}
