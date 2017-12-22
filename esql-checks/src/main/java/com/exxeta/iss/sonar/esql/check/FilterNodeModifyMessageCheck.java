/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

/**
 * @author C50679
 *
 */
@Rule(key = "FilterNodeModifyMessage")
public class FilterNodeModifyMessageCheck  extends DoubleDispatchVisitorCheck {
	
	private static final String MESSAGE = "The filter node cannot modify the message";
	//private static final String MESSAGE2 = "The filter node may only have one return value";
	
	
	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String>  line = CheckUtils.readLines(file);
		int i = 0;
		
		String lines[] = line.stream().toArray(String[]::new);
		 int lineNumber = 1;
		 String startLine = lines[lineNumber - 1];
		 
		 
         String upperCaseTheLine = startLine.toUpperCase();
		
		boolean filter = false;
        if(CheckUtils.isCreateFilterModuleLine(upperCaseTheLine))
            filter = true;
        if(filter)
        {
            
            boolean done = false;
           // int lineNumber;
			int line1 = lineNumber - 1;
            boolean filterViolation = false;
            do
            {
                if(done || line1 >= lines.length - 1)
                    break;
                line1++;
                String currentLine = lines[line1].toUpperCase();
                i =line1+1;
                if(currentLine.toUpperCase().contains("SET "))
                {
                    int pos = currentLine.indexOf("=");
                    if(pos > 0)
                    {
                        String leftSide = currentLine.substring(0, pos);
                        if(!leftSide.startsWith("IF ") && (leftSide.contains("ROOT ") || leftSide.contains("ROOT.")))
                        {
                            done = true;
                            filterViolation = true;
                        }
                    }
                }
                
            } while(true);
        //  int i = 0;
            if(filterViolation)
            {
            	addIssue(new LineIssue(this, i,   MESSAGE ));
            	//addIssue(new PreciseIssue(this, new IssueLocation(tree,   MESSAGE )));
            }
            
         }
        
      }
}



