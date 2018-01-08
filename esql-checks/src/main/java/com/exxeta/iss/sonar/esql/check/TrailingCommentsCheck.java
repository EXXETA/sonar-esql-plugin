/**
 * This java class is created to implement the logic for checking if the line contains both code and comments, 
 * if it contains both then trailing comments should be removed.
 * 
 * 
 * @author Prerana Agarkar
 *
 */

package com.exxeta.iss.sonar.esql.check;

import java.util.List;
import org.sonar.check.Rule;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;

@Rule(key ="TrailingComments")
public class TrailingCommentsCheck extends DoubleDispatchVisitorCheck  {
	public static final String MESSAGE = "The line contains both code and comments. Trailing comments are discouraged.";
	
	@Override
	public void visitProgram(ProgramTree tree) {
		 
		 EsqlFile file = getContext().getEsqlFile();
		 List<String> lines = CheckUtils.readLines(file);
		 
		 for(int i = 0; i < lines.size() ; i++)
		 {
			 
			 String originalLine = (String)lines.get(i);
			 
			 if((originalLine.replaceAll("\\s+","").contains(";--")))
					 {
				       
				        addIssue(new LineIssue(this,i + 1,MESSAGE));
					 }
			 else if(!(originalLine.replaceAll("\\s+","").isEmpty()) && !(originalLine.replaceAll("\\s+","").startsWith("--")) && !(originalLine.replaceAll("\\s+","").startsWith("/*")))
					 {
				          String lineArray[]=originalLine.split(" ");
				          String trailingComment="--";
				          
				          
				          
				         for(int j=0;j<lineArray.length-1;j++)
				         {
				        	 
				        	 if(lineArray[j].contains(trailingComment))
				        	 {
				        		 addIssue(new LineIssue(this,i + 1,MESSAGE));
							     break;
				        	 }
				         }
				 
					 }
			 else
			 {
				 continue;
			 }
		 }
	}
}