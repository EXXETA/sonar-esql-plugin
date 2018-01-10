/**
 * This java class is created to implement the logic for checking if commented esql code is present, 
 * if it is present then it should be removed before code checkin.
 * 
 * 
 * @author Prerana Agarkar
 *
 */

package com.exxeta.iss.sonar.esql.check;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.sonar.check.Rule;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;

@Rule(key ="CommentedOutEsqlCode")
public class CommentedOutEsqlCodeCheck extends DoubleDispatchVisitorCheck  {
	public static final String MESSAGE = "Esql code has been commented out.It should be removed before code checkin.";
	
	
	private static final Set STARTING_KEYWORDS = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] {
	        "BROKER SCHEMA", "CREATE FILTER MODULE", "CREATE FUNCTION", "CREATE PROCEDURE", "CREATE COMPUTE MODULE", "RETURNS", "SET", "EXCEPTION", "THROW", "IF", 
	        "WHILE", "SET", "DECLARE", "ELSE", "ELSEIF", "THEN", "LOOP", "BEGIN", "RETURN", "CALL", 
	        "MOVE", "END IF", "END MODULE", "END", "REPEAT", "UNTIL", "PROPAGATE", "RESIGNAL", "HANDLER", "CONTINUE", 
	        "CASE","CREATE FIELD"
	    })));
	
	private static final Set KEYWORDS = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] {
	        "BROKER SCHEMA", "CREATE FILTER MODULE", "CREATE FUNCTION", "CREATE PROCEDURE", "CREATE COMPUTE MODULE", "RETURNS", "SET", "EXCEPTION", "THROW", "IF", 
	        "WHILE", "DO", "SET", "DECLARE", "EXISTS", "ELSE", "ELSEIF", "THEN", "LOOP", "BEGIN", 
	        "RETURN", "CALL", "MOVE", "EXTERNAL", "SHARED", "ATOMIC", "END IF", "END MODULE", "END", "CHAR", 
	        "CHARACTER", "INTEGER", "TIME", "TIMESTAMP", "FIRSTCHILD", "LASTCHILD", "PREVIOUSSIBLING", "NEXTSIBLING", "OF", "DOMAIN", 
	        "BLOB", "FIELD", "IDENTITY", "ATTACH", "DETACH", "LEAVE", "LOG", "REPEAT", "UNTIL", "PROPAGATE", 
	        "RESIGNAL", "SQLSTATE", "VALUE", "HANDLER", "CONTINUE", "IN", "OUT", "INOUT", "REFERENCE TO", "REFERENCE", 
	        "NAMESPACE", "CONSTANT", "ALL", "CASE", "WHEN", "ASYMMETRIC", "SYMMETRIC", "DISTINCT", "BOTH", "FROM", 
	        "ITEM", "NOT", "IS"
	    })));
	@Override
			public void visitProgram(ProgramTree tree) {
				 EsqlFile file = getContext().getEsqlFile();
				 List<String> lines = CheckUtils.readLines(file);
				 
				 int commentedLineStart=0;
				 
				 for(int i =0; i<lines.size(); i++)
			        {
					 String originalLine = (String)lines.get(i);
			            
			            if(originalLine.isEmpty())
			                {
			            	continue;
			                }
			          else
			            if(CommentedOutEsqlCodeCheck.lineContainsKeyword(originalLine) || CommentedOutEsqlCodeCheck.lineStartsWithKeyword(originalLine))
			            {
			             
			            	if((originalLine.replaceAll("\\s+","").startsWith("--") && originalLine.replaceAll("\\s+","").endsWith(";")))
			                		 {
			                	         addIssue(new LineIssue(this,i+1,MESSAGE));
			                		 }
			            	else 
			            		if((originalLine.replaceAll("\\s+","").startsWith("/*")))
			            			{
			            			 commentedLineStart = i+1;
			            			 continue;
			            			}
			            		else 
			            			if(!(originalLine.replaceAll("\\s+","").endsWith("*/")))
		            			     {  
			            				continue; 
		            			     }
			            			else
			            			{
			            				addIssue(new LineIssue(this,commentedLineStart,MESSAGE)); 
			            			}
			            }
			        }	
			        
			  }
			 			 
	
	public static Set getAllKeywords()
    {
        return KEYWORDS;
    }

    public static Set getAllStartingKeywords()
    {
        return STARTING_KEYWORDS;
    }
    
    
    public static boolean lineContainsKeyword(String b)
    {
        b = b.toUpperCase();
        boolean found = false;
        Iterator iterator = getAllKeywords().iterator();
        do
        {
            if(!iterator.hasNext())
                break;
            String keyWord = (String)iterator.next();
            if(!b.contains(keyWord))
                continue;
            found = true;
            break;
            
        } while(true);
        return found;
    }
    
    public static boolean lineStartsWithKeyword(String s)
    {
        boolean found;
label1:
        {
            found = false;
            String compareInCase = s.toUpperCase().trim();
            Iterator iterator = getAllStartingKeywords().iterator();
            String keyWord;
            do
            {
                if(!iterator.hasNext())
                {
                	break label1;
                }
                
                keyWord = (String)iterator.next();
            } while(!compareInCase.startsWith(keyWord) && !compareInCase.startsWith((new StringBuilder()).append(keyWord).append(" ").toString()));
            
            if(compareInCase.startsWith("SET"))
            {
                if(compareInCase.startsWith("SET ") && compareInCase.contains("="))
                {
                	found = true;
                }
                else
                    {
                	return false;
                    }
                break label1;
            }
            if(compareInCase.startsWith("ELSE"))
            {
                if(compareInCase.startsWith("ELSE ") && compareInCase.contains(" IF") && compareInCase.contains(" THEN"))
                    {
                	found = true;
                    }
                else
                if(compareInCase.trim().equals("ELSE"))
                {
                	found = true;
                }
                else
                {
                	return false;
                }
                break label1;
            }
            if(compareInCase.startsWith("LOOP"))
            {
                if(compareInCase.endsWith("LOOP"))
                {
                    if(compareInCase.contains(":"))
                    {
                        found = true;
                        break label1;
                    }
                    if(compareInCase.trim().equals("LOOP"))
                    {
                        found = true;
                        break label1;
                    }
                }
                return false;
            }
            if(compareInCase.startsWith("PROPAGATE"))
            {
                if(compareInCase.trim().endsWith(";"))
                {
                    if(compareInCase.contains(" TO "))
                    {
                        found = true;
                        break label1;
                    }
                    if(compareInCase.contains(" FINALIZE"))
                    {
                        found = true;
                        break label1;
                    }
                }
                return false;
            }
            if(compareInCase.startsWith("CALL"))
            {
                if(compareInCase.contains("(") && compareInCase.contains(")"))
                    {
                	found = true;
                    }
                else
                {
                	return false;
                }
                break label1;
            }
            if(compareInCase.startsWith("WHILE"))
            {
                if(compareInCase.contains(" DO "))
                {
                	found = true;
                }
                else
                if(compareInCase.endsWith(" DO"))
                    {
                	found = true;
                    }
                else
                {
                	return false;
                }
                break label1;
            }
            if(compareInCase.startsWith("RETURN"))
            {
                if(compareInCase.startsWith("RETURN "))
                {
                    if(compareInCase.contains("TRUE"))
                    {
                        found = true;
                        break label1;
                    }
                    if(compareInCase.contains("FALSE"))
                    {
                        found = true;
                        break label1;
                    }
                }
                if(compareInCase.startsWith("RETURN;"))
                    {
                	found = true;
                    }
                else
                    {
                	return false;
                    }
            } else
            if(compareInCase.startsWith("DECLARE"))
            {
                if(compareInCase.startsWith("DECLARE ") && compareInCase.trim().endsWith(";"))
                    {
                	found = true;
                    }
                else
                    {
                	return false;
                    }
            } else
            if(compareInCase.startsWith("END"))
            {
                if(compareInCase.trim().endsWith(";"))
                    {
                	found = true;
                    }
                else
                    {
                	return false;
                    }
            } else
            if(compareInCase.startsWith("IF") && compareInCase.endsWith("THEN"))
                {
            	found = true;
                }
            else
                {
            	return false;
                }
        }
        return found;
    }

    
	    	
}