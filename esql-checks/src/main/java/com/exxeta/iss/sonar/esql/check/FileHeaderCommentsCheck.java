/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
			      			      
			     for(int lineCounter=0; lineCounter < lines.size(); lineCounter++){
			    	 				     			    	 
			    	 if(lines.get(0).startsWith(expectedLine)){
				    	  
				    	 if( lines.get(lineCounter).trim().contains(expectedLine1)){
				    		 if((lines.get(lineCounter + 1).replaceAll(" ", "").equalsIgnoreCase( expectedLine2.replaceAll(" ", "")))
				    				 && lines.get(lineCounter + 2).replaceAll(" ", "").length() > 0 && !lines.get(lineCounter + 2).replaceAll(" ", "").equalsIgnoreCase(expectedEndLine)){
				    			 break;
				    		 }else{
				    			 result = false;
					    		  break; 
				    		 }
				    	 }else{
				    		 continue;
				    	 }				    		 
				    		  
				    	}else{
				    		result = false;
				    		break;
				    	}
			     }     
		    
		    return result;
		  }
	
}
