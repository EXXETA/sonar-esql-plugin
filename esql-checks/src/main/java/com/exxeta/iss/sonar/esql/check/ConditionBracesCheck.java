/**
 * This java class is created to implement the logic for checking if braces for conditions are used or not, 
 * if it is not used then it should be inserted.
 * 
 * 
 * @author Prerana Agarkar
 *
 */

package com.exxeta.iss.sonar.esql.check;

import java.util.List;
//import javax.annotation.Nullable;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.SyntacticEquivalence;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ParenthesisedExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhileStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;

@Rule(key ="ConditionBraces")
public class ConditionBracesCheck extends DoubleDispatchVisitorCheck  {
	public static final String MESSAGE = "Use braces for conditions as it gives more readability to code.";
	    	@Override
			public void visitProgram(ProgramTree tree) {
	
				 EsqlFile file = getContext().getEsqlFile();
				 List<String> lines = CheckUtils.readLines(file);
			
			        
					if(lines.size() < 2)
			            return;
		for(int i = 0; i < lines.size() ; i++)
		 {
			 boolean conditionFound = false;
			    
			String originalLine = (String)lines.get(i);
			        
							   
			if((originalLine.replaceAll("\\s+","").contains("IF"))
				&& !(originalLine.replaceAll("\\s+","").contains("IF(")) 
				&& !(originalLine.replaceAll("\\s+","").contains("ENDIF")))
				     conditionFound=true;
				  else if((originalLine.replaceAll("\\s+","").contains("ELSEIF"))
						&& !(originalLine.replaceAll("\\s+","").contains("ELSEIF(")))
					  conditionFound = true;
				else if((originalLine.replaceAll("\\s+","").contains("WHILE")) 
						&& !(originalLine.replaceAll("\\s+","").contains("WHILE(")) 
						&& !(originalLine.replaceAll("\\s+","").contains("ENDWHILE")))
						conditionFound = true;
				else   
				       continue;
		
				if(conditionFound)  
            		addIssue(new LineIssue(this,i + 1,MESSAGE));

		 }
		 
    } 
}
			
                	
		

			


	
		

	

	