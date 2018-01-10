/**
 * This java class is created to implement the logic for checking if variable is initialised or not, 
 * if more than one variable of same datatype is found uninitialised then declare statement could be combined.
 * 
 * 
 * @author Prerana Agarkar
 *
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.sonar.check.Rule;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;



@Rule(key ="DeclareCombine")
public class DeclareCombineCheck extends DoubleDispatchVisitorCheck  {
	public static final String MESSAGE = "If  more than one variable of same datatype is found uninitialised then declare could be combined.";

	private static final Set<String> DECLARE_TYPES_ARRAY_LAST_ELEMENT = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] {"BOOLEAN;","INT;","INTEGER;","FLOAT;",
			"DECIMAL;","DATE;","TIME;","TIMESTAMP;","GMTTIMESTAMP;","GMTTIME;","CHAR;","CHARACTER;","BLOB;","BIT;","ROW;","SHARED;","NAMESPACE;","NAME;"})));
	
	private static final Set<String> DECLARE_TYPES_ARRAY_COMBINE_ELEMENT = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "REFERENCETO;","CONSTANTINTEGER;","CONSTANTINT;",
			"EXTERNALINTEGER;","EXTERNALINT;","CONSTANTCHAR;","CONSTANTCHARACTER;","EXTERNALCHAR;","EXTERNALCHARACTER;","SHAREDROW;"})));
	
	public static ArrayList<String> declareDatatypeList_Last_Element = new ArrayList<String>(DECLARE_TYPES_ARRAY_LAST_ELEMENT);
	public static ArrayList<String> declareDatatypeList_Combine_Element = new ArrayList<String>(DECLARE_TYPES_ARRAY_COMBINE_ELEMENT);
	public static Set<String> hset=new HashSet<String>();
	
	public static String originalLine=null;
	int j=0;
	
	@Override
	public void visitProgram(ProgramTree tree) {
		boolean found=false;
		String arrayLastElement=null;
		String arraySecondLastElement=null;
		String arrayCombineElement=null;
		
		EsqlFile file = getContext().getEsqlFile();
		List<String> lines = CheckUtils.readLines(file);
		
		for(j = 0; j < lines.size() ; j++)
		 {   
			 String originalLine = (String)lines.get(j);
			 
			 if((originalLine.replaceAll("\\s+","").contains("DECLARE")) && (!(originalLine.replaceAll("\\s+","").contains(","))))
			 {   
				 String lineArray[]=originalLine.split(" ");
		
				 arrayLastElement= lineArray[lineArray.length-1];
				 
				 arraySecondLastElement=lineArray[lineArray.length-2];
				 
				 if(((arraySecondLastElement.replaceAll("\\s+","").equalsIgnoreCase("EXTERNAL"))) || ((arraySecondLastElement.replaceAll("\\s+","").equalsIgnoreCase("CONSTANT")))
						 || ((arraySecondLastElement.replaceAll("\\s+","").equalsIgnoreCase("SHARED")) || ((arraySecondLastElement.replaceAll("\\s+","").equalsIgnoreCase("REFERENCE")))))
				 {
				
					 arrayCombineElement=arraySecondLastElement.concat(arrayLastElement);
					 
					 for(int i=0;i<declareDatatypeList_Combine_Element.size();i++)  
					 {   	
						 if(!(arrayCombineElement.replaceAll("\\s+","").equalsIgnoreCase(declareDatatypeList_Combine_Element.get(i))))
						    {
							 continue;
						    }
						 else
						 {  
							 if(hset.contains(declareDatatypeList_Combine_Element.get(i)))
				                  {
							    	  addIssue(new LineIssue(this,j+1,MESSAGE)); 
				                  }
							   else
							      {
								   hset.add(declareDatatypeList_Combine_Element.get(i));
							      } 
						  }
						 break;
					 }
						
				}
						 
				 
				 else
				 {
					 for(int i=0;i<declareDatatypeList_Last_Element.size();i++)  
					 {   	
					   if(!(arrayLastElement.replaceAll("\\s+","").equalsIgnoreCase(declareDatatypeList_Last_Element.get(i))))
						   
					   {  
						   continue;
					   }
					   else
					   {
						 if(hset.contains(declareDatatypeList_Last_Element.get(i)))
		                  {
					    	  addIssue(new LineIssue(this,j+1,MESSAGE)); 
		                  }
					   else
					      {
						    hset.add(declareDatatypeList_Last_Element.get(i));
					      } 
					   }
					 break;
					 }
				 }
				 				
continue;			
 }
	
}

}			 
			
}


