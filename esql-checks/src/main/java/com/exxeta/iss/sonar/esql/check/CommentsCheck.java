/**
 * This java class is created to implement the logic for checking if comment is included or not, 
 * over every 20 lines of code.
 * 
 * @author Prerana Agarkar
 *
 */
package com.exxeta.iss.sonar.esql.check;


import java.util.List;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;

@Rule(key = "Comments")
public class CommentsCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Include comment within the range of every 20 lines of code.";
	private static final int DEFAULT_THRESHOLD = 20;
	 @RuleProperty(
			    key = "Comments",
			    description = "Include comment within the range of every 20 lines of code.",
			    defaultValue = "" + DEFAULT_THRESHOLD)
	 public int maximumThreshold = DEFAULT_THRESHOLD;
	 
	 
	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String> lines = CheckUtils.readLines(file);
		
		int linesCounter=0;
		int commentsCount=0;
		int i =0;
		for (String line :lines)
		{
		   String  originalLine = line.toString();
		   i = i+1; 
		   
		   
		
			  if(originalLine.replaceAll("\\s+","").startsWith("--"))
			  {
				  linesCounter=0;
				  commentsCount=commentsCount+1;
				 
				  if(linesCounter<=DEFAULT_THRESHOLD)
				  {
				     if(commentsCount>=1)
				     {
				       linesCounter=0; 
					   continue;
				     } 
				  }
			  }
			  

 	 
			if(originalLine.replaceAll("\\s+","").startsWith("/*"))
				     {
				        linesCounter=0;
				        commentsCount=commentsCount+1;
			
				        if(linesCounter<=DEFAULT_THRESHOLD)
						  {
						     if(commentsCount>=1)
						     {
						    	 continue;
						     }
						  }
				     }
			  
			  else
				  {
				       if(originalLine.replaceAll("\\s+","").endsWith("*/"))
			         	       {  
				    	          linesCounter=0;
            			       } 
				  }   
			if(!(originalLine.replaceAll("\\s+","").isEmpty()))
			  { 
				 
				 
				  linesCounter=linesCounter+1;
				  
				  if(linesCounter<=DEFAULT_THRESHOLD)
				  {
					  continue;
				  }
				  
				  
				  if(linesCounter>DEFAULT_THRESHOLD)
						  
				  {
					  addIssue(new LineIssue(this,i+1,MESSAGE)); 
					  linesCounter=0;
					  continue; 
				  }
				        	 
				 }
			  }
        }  
   } 
		
		

			
							
		
		
	
	
