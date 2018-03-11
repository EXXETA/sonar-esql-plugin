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
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;

/**
 * This java class is created to implement the logic for reference check, Navigating message tree could be replaced by a reference. 
 * @author Sapna  singh
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
		
		
		
		HashSet<Integer> violatingLinesWithPossibleReference = new HashSet<>();
        int startingLine = 0;
        
        
         processSingleModuleForReferences( startingLine, lines, violatingLinesWithPossibleReference);
        
       
        Set<Integer> linesNumbers = new HashSet<>();
        Iterator<Integer> iterator1 = violatingLinesWithPossibleReference.iterator();
        do
        {
            if(!iterator1.hasNext())
                break;
           
            Integer lineNumber = iterator1.next();
            if(linesNumbers.add(lineNumber))
            {
            	addIssue(new LineIssue(this, lineNumber,   MESSAGE ));
            }
        } while(true);
    
	}    
        
        private static void processSingleModuleForReferences(  int startingLine, List<String> moduleLines, HashSet<Integer> violatingLinesWithPossibleReference)
        {
            HashMap<String, Integer> allKeys = new HashMap<>();
            Iterator<String> iterator = moduleLines.iterator();
            
            do
            {
                if(!iterator.hasNext())
                    break;
                String line = iterator.next();
              
                line = line.trim();
                String removeQuotedComment = CheckUtils.removeQuotedContentByChar(line, '\'');
                if(removeQuotedComment.toUpperCase().trim().startsWith("SET "))
                {
                    removeQuotedComment = removeQuotedComment.substring(4);
                    if(removeQuotedComment.trim().endsWith(";"))
                        removeQuotedComment = removeQuotedComment.substring(0, removeQuotedComment.length() - 1);
                    int equalsPos = removeQuotedComment.indexOf('=');
                    if(equalsPos > 0) {
                    	String endLine = null;
                        String startLine = removeQuotedComment.substring(0, equalsPos).trim();
                        if (!(equalsPos + 1  > removeQuotedComment.length())){
                            endLine = removeQuotedComment.substring(equalsPos + 1).trim();
                        }
                        Set<String> keyValuesAll = new HashSet<>();
                        Set<String> keyValuesStart = CheckUtils.buildKeys(startLine);
                        Set<String> keyValuesEnd = new HashSet<>();
                        if (endLine!=null){
                            CheckUtils.buildKeys(endLine);
                        }

                        keyValuesStart.addAll(keyValuesEnd);
                        for(Iterator<String> iterator1 = keyValuesStart.iterator(); iterator1.hasNext();) {
                            String key = iterator1.next();
                            if (!key.contains("OutputLocalEnvironment") && !key.contains("InputLocalEnvironment")) {
                                    keyValuesAll.add(key);
                                } 
                            }

                        Iterator<String> iterator2 = keyValuesAll.iterator();
                        while(iterator2.hasNext()) 
                        {
                            String key = iterator2.next();
                            if(key == null || key.length() == 0)
                                throw new RuntimeException((new StringBuilder()).append("Key is empty: ").append(key).append(" for line:").append(line).toString());
                            Integer count = allKeys.get(key);
                            if(count == null)
                                allKeys.put(key, 1);
                            else
                                allKeys.put(key, count.intValue() + 1);
                        }
                    }
                }
            } while(true);
            iterator = allKeys.keySet().iterator();
          
            while (iterator.hasNext())
            {
                String key = iterator.next();
                Integer count = allKeys.get(key);
                if(count.intValue() > threshold )
                {
                    Integer lineNumber = CheckUtils.findLineInText(moduleLines, key);
                    if(lineNumber != null){
	                    Integer absLine = Integer.valueOf(lineNumber.intValue() + startingLine);
	                    violatingLinesWithPossibleReference.add(absLine);
                    }
                }
            }
        }
	}
