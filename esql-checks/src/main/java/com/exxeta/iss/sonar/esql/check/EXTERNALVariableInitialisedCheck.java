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
 * This java class is created to implement the logic to check all the  EXTERNAL variable should be initialized in esql file.
 * @author sapna singh
 *
 */
@Rule(key = "EXTERNALVariableInitialised")
public class EXTERNALVariableInitialisedCheck extends DoubleDispatchVisitorCheck {
	
	private static final String MESSAGE = "EXTERNAL variable should be initialized";
	
	
	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String>  lines = CheckUtils.readLines(file);
		//int i = 0;
		
		for (String line : lines) {
			
	
		String upperCaseTheLine = line.toUpperCase();
		
	
	if(isDelcareStatement(upperCaseTheLine) && upperCaseTheLine.contains("EXTERNAL "))
    {
        
        boolean externalVariableEmpty = false;
        if(upperCaseTheLine.contains("CHAR"))
        {
            int iPos = line.indexOf('\'');
            
            
            int iLastPos = line.lastIndexOf('\'');
            
            if(iPos > 0 && iLastPos > 0)
            {
            	//if(iPos + 1 == iLastPos)
                externalVariableEmpty = true;
               
            }
            if(externalVariableEmpty){
            	addIssue(new PreciseIssue(this, new IssueLocation(tree,   MESSAGE )));
            }
           }
    }
}
	}
			
	
	public static boolean isDelcareStatement(String line)
    {
        if(line.trim().startsWith("DECLARE "))
            return !line.contains(" EXIT ") && !line.contains(" HANDLER ");
        else
            return false;
    }

}


