/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

/**
 * This java class is created to implement the logic for reference check, Navigating message tree could be replaced by a reference. 
 * @author Sapna. singh
 *
 */
@Rule(key = "NavigatingTreeCouldBeReference")
public class NavigatingTreeCouldBeReferenceCheck extends DoubleDispatchVisitorCheck{
	
	private static final String MESSAGE = "Navigating message tree could be replaced by a reference.";
	
	private static final int DEFAULT_THRESHOLD = 3;
	 @RuleProperty(
			    key = "NavigatingTreeCouldBeReference",
			    description = "Navigating message tree could be replaced by a reference.",
			    defaultValue = "" + DEFAULT_THRESHOLD)
	 public static int threshold = DEFAULT_THRESHOLD;
	
	
	
	@Override
	public void visitProgram(ProgramTree tree) {
		
		EsqlFile file = getContext().getEsqlFile();
		List<String> lines = CheckUtils.readLines(file);
		
		
		
		HashSet violatingLinesWithPossibleReference = new HashSet();
        int startingLine = 0;
        
        
         processSingleModuleForReferences( startingLine, lines, violatingLinesWithPossibleReference);
        
       
        Set linesNumbers = new HashSet();
        Iterator iterator1 = violatingLinesWithPossibleReference.iterator();
        do
        {
            if(!iterator1.hasNext())
                break;
           
            Integer lineNumber = (Integer)iterator1.next();
            if(linesNumbers.add(lineNumber))
            {
            	//addIssue(new PreciseIssue(this, new IssueLocation(tree,   MESSAGE )));
            	addIssue(new LineIssue(this, lineNumber,   MESSAGE ));
            }
        } while(true);
    
	}    
        
        private static void processSingleModuleForReferences(  int startingLine, List moduleLines, HashSet violatingLinesWithPossibleReference)
        {
            HashMap allKeys = new HashMap();
            Iterator iterator = moduleLines.iterator();
            
            do
            {
                if(!iterator.hasNext())
                    break;
                String line = (String)iterator.next();
              
                line = line.trim();
                String removeQuotedComment = CheckUtils.removeQuotedContentByChar(line, '\'');
                if(removeQuotedComment.toUpperCase().trim().startsWith("SET "))
                {
                    removeQuotedComment = removeQuotedComment.substring(4);
                    if(removeQuotedComment.trim().endsWith(";"))
                        removeQuotedComment = removeQuotedComment.substring(0, removeQuotedComment.length() - 1);
                    int equalsPos = removeQuotedComment.indexOf("=");
                    if(equalsPos > 0)
                    
                    {
                    	String endLine = null;
                        String startLine = removeQuotedComment.substring(0, equalsPos).trim();
                        if (!(equalsPos + 1  > removeQuotedComment.length())){
                         endLine = removeQuotedComment.substring(equalsPos + 1).trim();
                        }
                        Set keyValuesAll = new HashSet();
                        Set keyValuesStart = new HashSet();
                        Set keyValuesEnd = new HashSet();
                        keyValuesStart = CheckUtils.buildKeys(startLine);
                        keyValuesEnd = CheckUtils.buildKeys(endLine);
                        keyValuesStart.addAll(keyValuesEnd);
                        for(Iterator iterator1 = keyValuesStart.iterator(); iterator1.hasNext();)
                        {
                            String key = (String)iterator1.next();
                            if(!key.contains("OutputLocalEnvironment"))
                            {
                                if(!key.contains("InputLocalEnvironment"))
                                {
                                    keyValuesAll.add(key);
                                } 
                            }
                        }

                        Iterator iterator2 = keyValuesAll.iterator();
                        while(iterator2.hasNext()) 
                        {
                            String key = (String)iterator2.next();
                            if(key == null || key.length() == 0)
                                throw new RuntimeException((new StringBuilder()).append("Key is empty: ").append(key).append(" for line:").append(line).toString());
                            Integer count = (Integer)allKeys.get(key);
                            if(count == null)
                                allKeys.put(key, new Integer(1));
                            else
                                allKeys.put(key, new Integer(count.intValue() + 1));
                        }
                    }
                }
            } while(true);
            iterator = allKeys.keySet().iterator();
          
            do
            {
                if(!iterator.hasNext())
                    break;
                String key = (String)iterator.next();
                Integer count = (Integer)allKeys.get(key);
                if(count.intValue() > threshold )
                {
                    Integer lineNumber = CheckUtils.findLineInText(moduleLines, key);
                    if(lineNumber == null)
                        throw new RuntimeException((new StringBuilder()).append("Cannot find line again ").append(key).toString());
                    Integer absLine = Integer.valueOf(lineNumber.intValue() + startingLine);
                    violatingLinesWithPossibleReference.add(absLine);
                }
            } while(true);
        }
	}
	
        	
        	
  	
        	
      	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	
       

	
