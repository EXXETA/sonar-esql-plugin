/**
 * This java class is created to implement the logic for checking if the code is present after return or throw statement, 
 * if it is present then that code is unreachable.
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
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;

@Rule(key ="UnreachableCode")
public class UnreachableCodeCheck extends DoubleDispatchVisitorCheck {
	
	public static final String MESSAGE = "Code is unreachable following RETURN or THROW statement.";

	@Override
	public void visitProgram(ProgramTree tree) {
		
		 EsqlFile file = getContext().getEsqlFile();
		 List<String> lines = CheckUtils.readLines(file);
		
        int startingLineNumber=1;
       
		int lineNumber = startingLineNumber;
        
		if(lines.size() < 2)
            return;
       
		for(int i = 0; i <= lines.size() - 1; i++)
        {
            String originalLine = (String)lines.get(i);
            String line = UnreachableCodeCheck.removeQuotedContentByChar(originalLine, '\'');
            if(line.trim().length() > 0)
            {
                boolean returnFound = false;
                boolean returnTheFound = false;
                if((line.toUpperCase().contains("THROW ")) && (!(line.toUpperCase().replaceAll("\\s+","").contains("--"))))
                    returnFound = true;
                if(line.trim().toUpperCase().startsWith("RETURN "))
                    returnFound = true;
                
                else
                    if(UnreachableCodeCheck.isReturnsLine(line))                    	
                       returnFound = true;
                    
                if(returnFound || returnTheFound)
                {
                        int loopTilEnd = i + 1;
                        do
                        {
                            if(loopTilEnd >= lines.size() - 1)
                                break;
                            String followingLine = (String)lines.get(loopTilEnd);
                            if(followingLine.trim().length() > 0)
                            {
                            	if(!followingLine.toUpperCase().contains("ELSE") && !followingLine.toUpperCase().contains("WHEN ") && !followingLine.toUpperCase().contains("END ") && !UnreachableCodeCheck.isContainsEndLine(followingLine))
                                {
                            		addIssue(new LineIssue(this,i+1 ,MESSAGE));
                                }
                            	 break;
                            }
                            loopTilEnd++;                
		
                        } while(true);
		
		
                    }	
            	}
                 lineNumber++;
			}		
	}        


    public static String removeQuotedContentByChar(String s, char c)
    {
    	StringBuilder removeQuotedComment = new StringBuilder();
        boolean quote = false;
        for(int i = 0; i < s.length(); i++)
        {
            if(!quote)
            {
                if(s.charAt(i) == c)
                    quote = true;
                removeQuotedComment.append(s.charAt(i));
                continue;
            }
            if(s.charAt(i) == c)
            {
                quote = false;
                removeQuotedComment.append(s.charAt(i));
            }
        }

        return removeQuotedComment.toString();
    }
    
    public static boolean isReturnsLine(String line)
    {
        String withoutSpace = line.toUpperCase().replace(" ", "");
        return withoutSpace.startsWith("RETURN;");
    }
    
    public static boolean isContainsEndLine(String line)
    {
        line = line.trim().toUpperCase();
        if(line.equalsIgnoreCase("END;"))
            return true;
        if(line.startsWith("END "))
        {
            String withoutSpace = line.replace(" ", "");
            if(withoutSpace.equalsIgnoreCase("END;"))
                return true;
        }
        return false;
    }
	
	
}
        