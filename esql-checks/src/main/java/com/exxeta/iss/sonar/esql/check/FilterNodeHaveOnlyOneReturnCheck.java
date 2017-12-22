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
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

/**
 * @author C50679
 *
 */
@Rule(key = "FilterNodeHaveOnlyOneReturn")
public class FilterNodeHaveOnlyOneReturnCheck extends DoubleDispatchVisitorCheck {
	
	
	private static final String MESSAGE = "The filter node may only have one return value";
	
	
	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String>  line = CheckUtils.readLines(file);
		
		
		String lines[] = line.stream().toArray(String[]::new);
		 int lineNumber = 1;
		 String startLine = lines[lineNumber - 1];
	     String upperCaseTheLine = startLine.toUpperCase();
		
		boolean filter = false;
        if(CheckUtils.isCreateFilterModuleLine(upperCaseTheLine))
            filter = true;
        if(filter)
        {
        	int trueCount = 0;
            int falseCount = 0;
            int returnOther = 0;
            int throwsError = 0;
            
            boolean done = false;
           
			int line1 = lineNumber - 1;
           
            do
            {
                if(done || line1 >= lines.length - 1)
                    break;
                line1++;
                String currentLine = lines[line1].toUpperCase();
               
                
               if(CheckUtils.isReturnsTrueLine(currentLine))
                    trueCount++;
                else
                if(CheckUtils.isReturnsFalseLine(currentLine))
                    falseCount++;
                else
                if(CheckUtils.isReturnsLine(currentLine))
                    returnOther++;
                else
                if(CheckUtils.isThrowsError(currentLine))
                    throwsError++;
                else
                if(CheckUtils.isReturnsUsingEqualsLine(currentLine))
                {
                    trueCount++;
                    falseCount++;
                } else
                if(currentLine.contains("RETURN "))
                    returnOther++;
                
                if(CheckUtils.isEndModuleLine(currentLine))
                    done = true;
                if(currentLine.contains("CREATE ") && currentLine.contains(" MODULE "))
                    done = true;
                
            } while(true);
        
            boolean returnViolation = false;
            if(trueCount + falseCount + returnOther + throwsError == 0)
                returnViolation = true;
            if(trueCount == 0 && returnOther == 0)
                returnViolation = true;
            if(falseCount == 0 && returnOther == 0 && throwsError == 0)
                returnViolation = true;
            if(returnViolation)
            {
            	
            	addIssue(new PreciseIssue(this, new IssueLocation(tree,   MESSAGE )));
            }
            }
            
         }
}
