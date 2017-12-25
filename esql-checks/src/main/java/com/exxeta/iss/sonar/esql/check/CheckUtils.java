/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
 */package com.exxeta.iss.sonar.esql.check;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;

public class CheckUtils {
	private CheckUtils() {
	}

	public static boolean equalNodes(AstNode node1, AstNode node2) {
		if (!node1.getType().equals(node2.getType()) || node1.getNumberOfChildren() != node2.getNumberOfChildren()) {
			return false;
		}

		List<AstNode> children1 = node1.getChildren();
		List<AstNode> children2 = node2.getChildren();
		for (int i = 0; i < children1.size(); i++) {
			if (!equalNodes(children1.get(i), children2.get(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean containsValue(List<Token> list, String value) {
		for (Token currentToken : list) {
			if (currentToken.getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}

	public static List<String> readLines(EsqlFile file) {
		try (BufferedReader reader = newBufferedReader(file)) {
			return reader.lines().collect(Collectors.toList());

		} catch (IOException e) {
			throw new IllegalStateException("Unable to read file " + file.relativePath(), e);
		}
	}

	private static BufferedReader newBufferedReader(EsqlFile file) {
		return new BufferedReader(new StringReader(file.contents()));
	}
	
	/* changes Strats from here  (Sapna Singh) */
	
	public static boolean isCreateFilterModuleLine(String line)
    {
        String upperCaseLine = line.toUpperCase().trim();
        if(upperCaseLine.startsWith("CREATE "))
        {
            String withoutSpace = upperCaseLine.replace(" ", "");
            if(withoutSpace.startsWith("CREATEFILTERMODULE"))
                return true;
        }
        return false;
    }
	
	public static String removeQuotedContent(String s) {
		String res = removeQuotedContentByChar(s, '\'');
		res = removeQuotedContentByChar(res, '"');
		return res;
	}
	
	public static String removeQuotedContentByChar(String s, char c) {
		StringBuilder removeQuotedComment = new StringBuilder();
		boolean quote = false;
		for (int i = 0; i < s.length(); i++) {
			if (!quote) {
				if (s.charAt(i) == c)
					quote = true;
				removeQuotedComment.append(s.charAt(i));
				continue;
			}
			if (s.charAt(i) == c) {
				quote = false;
				removeQuotedComment.append(s.charAt(i));
			}
		}

		return removeQuotedComment.toString();
	}
	
	public static int countCharacters(String s, String ch) {
		int cnt = 0;
		String[] lines = s.split(ch);
		cnt = lines.length - 1;
		return cnt;
	}
	
	 public static boolean isReturnsTrueLine(String line)
	    {
	        String withoutSpace = line.toUpperCase().replace(" ", "");
	        return withoutSpace.startsWith("RETURNTRUE;");
	    }

	    public static boolean isReturnsFalseLine(String line)
	    {
	        String withoutSpace = line.toUpperCase().replace(" ", "");
	        return withoutSpace.startsWith("RETURNFALSE;");
	    }

	    public static boolean isThrowsError(String line)
	    {
	        String withoutSpace = line.toUpperCase().replace(" ", "");
	        return withoutSpace.contains("THROWUSEREXCEPTION");
	    }

	    public static boolean isReturnsLine(String line)
	    {
	        String withoutSpace = line.toUpperCase().replace(" ", "");
	        return withoutSpace.startsWith("RETURN;");
	    }

	    public static boolean isEndModuleLine(String line)
	    {
	        String withoutSpace = line.toUpperCase().replace(" ", "");
	        return withoutSpace.startsWith("ENDMODULE;");
	    }
	    public static boolean isReturnsUsingEqualsLine(String line)
	    {
	        String upperLine = line.toUpperCase();
	        if(upperLine.contains("RETURN"))
	        {
	            if(upperLine.contains("="))
	                return true;
	            if(upperLine.contains("<>"))
	                return true;
	        }
	        return false;
	    }
	    
	    public static Set<String> buildKeys(String lineParam)
	    {
	       String line = lineParam;
	        Set<String> keys = new HashSet<>();
	        int startId = line.lastIndexOf('(');
	        if(startId > -1)
	            line = line.substring(startId + 1);
	        int endId = line.lastIndexOf(')');
	        if(endId > -1)
	            line = line.substring(0, endId);
	        String[] lines = line.split("\\|\\|");
	        String[] as = lines;
	        int j = as.length;
	        for(int k = 0; k < j; k++)
	        {
	            String singleLine = as[k];
	            String[] lineParts = singleLine.split(",");
	            String[] as1 = lineParts;
	            int l = as1.length;
	            for(int i1 = 0; i1 < l; i1++)
	            {
	                String thisLine = as1[i1];
	                int fromPos = thisLine.indexOf(" FROM ");
	                if(fromPos > -1)
	                    thisLine = thisLine.substring(0, fromPos);
	                int asPos = thisLine.indexOf(" AS ");
	                if(asPos > -1)
	                    thisLine = thisLine.substring(0, asPos);
	                String[] lineParts2 = thisLine.split(" AND ");
	                String[] as2 = lineParts2;
	                int j1 = as2.length;
	                for(int k1 = 0; k1 < j1; k1++)
	                {
	                    String thisLineAfterAnd = as2[k1];
	                    int cnt = 0;
	                    int i = 0;
	                    boolean done=false;
	                    while(!done)
	                    {
	                        if(i >= thisLineAfterAnd.length())
	                            done = true;
	                        if(!done && thisLineAfterAnd.charAt(i) == '.')
	                        {
	                            if(cnt >= 1)
	                            {
	                                String newKey = thisLineAfterAnd.substring(0, i);
	                                keys.add(newKey);
	                            }
	                            cnt++;
	                        }
	                        if(!done && (thisLineAfterAnd.charAt(i) == ' ' || thisLineAfterAnd.charAt(i) == '='))
	                            done=true;
	                        i++;
	                    } ;
	                }

	            }

	        }

	        return keys;
	    }

	    public static Integer findLineInText(List<String> textLines, String line)
	    {
	        int i = 0;
	        for(Iterator<String> iterator = textLines.iterator(); iterator.hasNext();)
	        {
	            String s = iterator.next();
	            i++;
	            if(s.contains(line))
	                return i;
	        }

	        return null;
	    }
	
	   
	
	/* changes Ends here  (sapna singh) */
}