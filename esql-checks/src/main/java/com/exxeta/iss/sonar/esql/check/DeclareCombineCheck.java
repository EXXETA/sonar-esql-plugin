/**
 * This java class is created to implement the logic for checking if variable is initialised or not, 
 * if more than one variable of same datatype is found uninitialised then declare statement could be combined.
 * 
 * 
 * @author Prerana Agarkar
 *
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.google.common.collect.ImmutableList;



@Rule(key ="DeclareCombine")
public class DeclareCombineCheck extends DoubleDispatchVisitorCheck  {
	public static final String MESSAGE = "If  more than one variable of same datatype is found uninitialised then declare could be combined.";

	private static final List<String> DECLARE_TYPES_ARRAY_LAST_ELEMENT = ImmutableList.of("BOOLEAN;","INT;","INTEGER;","FLOAT;",
			"DECIMAL;","DATE;","TIME;","TIMESTAMP;","GMTTIMESTAMP;","GMTTIME;","CHAR;","CHARACTER;","BLOB;","BIT;","ROW;","SHARED;","NAMESPACE;","NAME;");
	
	private static final List<String> DECLARE_TYPES_ARRAY_COMBINE_ELEMENT = ImmutableList.of( "REFERENCETO;","CONSTANTINTEGER;","CONSTANTINT;",
			"EXTERNALINTEGER;","EXTERNALINT;","CONSTANTCHAR;","CONSTANTCHARACTER;","EXTERNALCHAR;","EXTERNALCHARACTER;","SHAREDROW;");
	

	int j=0;
	
	@Override
	public void visitProgram(ProgramTree tree) {
		String arrayLastElement=null;
		String arraySecondLastElement=null;
		String arrayCombineElement=null;
		Set<String> hset=new HashSet<>();
		
		EsqlFile file = getContext().getEsqlFile();
		List<String> lines = CheckUtils.readLines(file);
		
		for(j = 0; j < lines.size() ; j++)
		 {   
			 String originalLine = lines.get(j);
			 
			 if((originalLine.replaceAll("\\s+","").contains("DECLARE")) && (!(originalLine.replaceAll("\\s+","").contains(","))))
			 {   
				 String[] lineArray=originalLine.split(" ");
		
				 arrayLastElement= lineArray[lineArray.length-1];
				 
				 arraySecondLastElement=lineArray[lineArray.length-2];
				 
				 if((("EXTERNAL".equalsIgnoreCase(arraySecondLastElement.replaceAll("\\s+","")))) || ("CONSTANT".equalsIgnoreCase(arraySecondLastElement.replaceAll("\\s+","")))
						 || (("SHARED".equalsIgnoreCase(arraySecondLastElement.replaceAll("\\s+",""))) || (("REFERENCE".equalsIgnoreCase(arraySecondLastElement.replaceAll("\\s+",""))))))
				 {
				
					 arrayCombineElement=arraySecondLastElement.concat(arrayLastElement);
					 
					 for(int i=0;i<DECLARE_TYPES_ARRAY_COMBINE_ELEMENT.size();i++)  
					 {   	
						 if(!(arrayCombineElement.replaceAll("\\s+","").equalsIgnoreCase(DECLARE_TYPES_ARRAY_COMBINE_ELEMENT.get(i))))
						    {
							 continue;
						    }
						 else
						 {  
							 if(hset.contains(DECLARE_TYPES_ARRAY_COMBINE_ELEMENT.get(i)))
				                  {
							    	  addIssue(new LineIssue(this,j+1,MESSAGE)); 
				                  }
							   else
							      {
								   hset.add(DECLARE_TYPES_ARRAY_COMBINE_ELEMENT.get(i));
							      } 
						  }
						 break;
					 }
						
				}
						 
				 
				 else
				 {
					 for(int i=0;i<DECLARE_TYPES_ARRAY_LAST_ELEMENT.size();i++)  
					 {   	
					   if(!(arrayLastElement.replaceAll("\\s+","").equalsIgnoreCase(DECLARE_TYPES_ARRAY_LAST_ELEMENT.get(i))))
						   
					   {  
						   continue;
					   }
					   else
					   {
						 if(hset.contains(DECLARE_TYPES_ARRAY_LAST_ELEMENT.get(i)))
		                  {
					    	  addIssue(new LineIssue(this,j+1,MESSAGE)); 
		                  }
					   else
					      {
						    hset.add(DECLARE_TYPES_ARRAY_LAST_ELEMENT.get(i));
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


