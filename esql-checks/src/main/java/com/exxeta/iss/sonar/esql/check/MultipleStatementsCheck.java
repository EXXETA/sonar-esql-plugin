/**
 * This java class is created to implement the logic for checking if multiple statements are present on the same line,
 * if it is present then it should be removed.
 * 
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

@Rule(key ="MultipleStatements")
public class MultipleStatementsCheck extends DoubleDispatchVisitorCheck  {
	
	public static final String MESSAGE = "Multiple statements on the same line.";
	
	@Override
	public void visitProgram(ProgramTree tree) {
		
		 EsqlFile file = getContext().getEsqlFile();
		 List<String> lines = CheckUtils.readLines(file);
		 
		if(lines.size() < 2)
            {
			return;
            }
		for(int i = 0; i < lines.size() ; i++)
		{
			String theLine = (String)lines.get(i);
			String quotedContentRemoved = MultipleStatementsCheck.removeQuotedContent(theLine.trim());
			int statementsCount = MultipleStatementsCheck.countNonLineFeedCharacters(quotedContentRemoved, ";");
			
			if(statementsCount > 2)
			{
				addIssue(new LineIssue(this,i + 1,MESSAGE));
			}
			
		}
		
	}
	

	public static String removeQuotedContent(String s)
    {
        String res = removeQuotedContentByChar(s, '\'');
        res = removeQuotedContentByChar(res, '"');
        return res;
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
	
	public static int countNonLineFeedCharacters(String s, String ch)
    {
        
		String[] lineCount = s.split(";", -1);
		return lineCount.length;
       
       
    }


}
