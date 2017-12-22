package com.exxeta.iss.sonar.esql.check;

import java.util.List;
import org.sonar.check.Rule;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;

/**
 * This Java class is created to check file header comments
 * @author
 *
 */
@Rule(key="FileHeaderComments")
public class FileHeaderCommentsCheck extends DoubleDispatchVisitorCheck {
	
	private static final String MESSAGE = "Add or update the header at the start of this file.";
		
	@Override
	public void visitProgram(ProgramTree tree) {
	
		      checkPlainText();
		    
	}
	 private void checkPlainText() {
		   
		    EsqlFile file = getContext().getEsqlFile();
		    List<String> lines = CheckUtils.readLines(file);
		    
		    if (!matches(lines)) {
		       	addIssue(new LineIssue(this, 1, MESSAGE));
		    }
		  }
	 
	 private static boolean matches( List<String> lines) {
		    boolean result;  
		    	  		    	
			      result = true;
			      String expectedLine ="/*";
			      String expectedLine1 ="Change Log";
			      String expectedLine2 ="S.No. Incident Number  Jira Number  Description  Date Fixed  Updated ESQL/Node";
			      String expectedEndLine ="*/";
			      boolean headerPresent = false;
			      			      
			     for(int lineCounter=0; lineCounter < lines.size(); lineCounter++){
			    	 				     			    	 
			    	 if(lines.get(0).startsWith(expectedLine)){
				    	  
				    	 if(!headerPresent && lines.get(lineCounter).trim().contains(expectedLine1)){
				    		 if((lines.get(lineCounter + 1).replaceAll(" ", "").equalsIgnoreCase( expectedLine2.replaceAll(" ", "")))
				    				 && lines.get(lineCounter + 2).replaceAll(" ", "").length() > 0 && !lines.get(lineCounter + 2).replaceAll(" ", "").equalsIgnoreCase(expectedEndLine)){
				    			 headerPresent = true;
				    			 break;
				    		 }else{
				    			 headerPresent = false;
				    			 result = false;
					    		  break; 
				    		 }
				    	 }else{
				    		 continue;
				    	 }				    		 
				    		  
				    	}else{
				    		headerPresent = false;
				    		result = false;
				    		break;
				    	}
			     }     
		    
		    return result;
		  }
	
}
