/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;



import java.util.ArrayList;
import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;



/**
 * This java class is created to implement the logic for checking cyclomatic complexity of the code.
 * cyclomatic complexity should be less than the threshold value
 * @author C50679 (sapna.singh@infosys.com)
 *
 */
@Rule(key="CyclomaticComplexity")
public class CyclomaticComplexityCheck extends DoubleDispatchVisitorCheck{
	
	private static final String MESSAGE = "Cyclomatic Complexity is higher then the threshold.";
	
	private static final int COMPLEXITY_THRESHOLD =10;
	int totalComplexity = 0;
    int startingLine = 1;
    


@Override
public void visitProgram(ProgramTree tree) {
	EsqlFile file = getContext().getEsqlFile();
	List<String> lines = CheckUtils.readLines(file);
	int i = 0;
	boolean commentSection = false;
	ArrayList <ArrayList<String>> modules = new ArrayList<ArrayList<String>>();
	ArrayList<String> moduleLines = new ArrayList<String>();
	boolean isInsideModule = false;
	for (String line : lines) {
		i = i + 1;
		
		if (!line.trim().startsWith("--") && !line.trim().startsWith("/*") && !commentSection) {
				if(!isBeginStatement(line) && !isEndStatement(line) && isInsideModule){					
					moduleLines.add(line);
				}
				if(isBeginStatement(line.trim())){
					isInsideModule = true;
					moduleLines = new ArrayList<String>();
					moduleLines.add(String.valueOf(i));
					moduleLines.add(line);
				}
				if(isEndStatement(line.trim())){
					moduleLines.add(line);
					isInsideModule=false;
					modules.add(moduleLines);
					
				}
		} else if (line.trim().startsWith("/*") && !commentSection && !line.trim().endsWith("*/")) {
			commentSection = true;
		} else if (commentSection && line.trim().endsWith("*/")) {
			commentSection = false;
		}
	}
		for(ArrayList<String> module : modules){
			
			if(CalculateComplexity(module)>COMPLEXITY_THRESHOLD){
				
				addIssue(new LineIssue(this,  Integer.parseInt(module.get(0)), "Check function \"" + ExtractFunctionProcedureName(module.get(1))+ "\". " + MESSAGE));
				//addIssue(new LineIssue(this,  Integer.parseInt(module.get(0)), "Check function \"" + extractProcedureName(module.get(1))+ "\". " + MESSAGE));
			}
		}
//		addIssue(new LineIssue(this, i, "Check keyword \"" + trimmed + "\". " + MESSAGE));

	
}

public static boolean isEndStatement(String s)
{
    String withoutSpace = s.replace(" ", "").toUpperCase();
    return withoutSpace.contains("END;");
}

public static boolean isBeginStatement(String s)
{
    String withoutSpace = s.replace(" ", "").toUpperCase();
    return withoutSpace.startsWith("CREATEPROCEDURE") || withoutSpace.startsWith("CREATEFUNCTION");
}

//public static boolean isModuleCreationStatement(String s)
//{
//    String withoutSpace = s.replace(" ", "").toUpperCase();
//    return withoutSpace.startsWith("CREATECOMPUTEMODULE");
//}
public static int CalculateComplexity(ArrayList<String> lines) {
	
	
	int complexity = 1;
	for (String l : lines) {
		
		String upperCase = l.toUpperCase();
		String quotedContentRemoved = removeQuotedContent(upperCase);
		
		if (quotedContentRemoved.contains("ELSEIF")) {
			int conditionComplexity = calculateConditionESQLComplexity(quotedContentRemoved);
			complexity += conditionComplexity;
		} else if (quotedContentRemoved.contains("IF ")) {
			int conditionComplexity = calculateConditionESQLComplexity(quotedContentRemoved);
			complexity += conditionComplexity;
		} else if (quotedContentRemoved.contains("FOR "))
			complexity++;
		else if (quotedContentRemoved.contains("WHILE ") && !quotedContentRemoved.contains("END")) {
			int conditionComplexity = calculateConditionESQLComplexity(quotedContentRemoved);
			complexity += conditionComplexity;
		} else if (quotedContentRemoved.contains("WHEN "))
			complexity++;
		else if (quotedContentRemoved.contains("REPEAT"))
			complexity++;
		else if (quotedContentRemoved.contains("LEAVE "))
			complexity++;
		else if (quotedContentRemoved.contains("HANDLER "))
			complexity++;
		else if (quotedContentRemoved.contains("ELSE ") || quotedContentRemoved.endsWith("ELSE")) {
			int conditionComplexity = calculateConditionESQLComplexity(quotedContentRemoved);
			complexity += conditionComplexity;
		}
		
	}
	return complexity;
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
	
	 public static  String ExtractFunctionProcedureName(String declareStatement ){

		 declareStatement = declareStatement.substring(0,declareStatement.indexOf("()")+2);
		 String name = "";
		 for(String tmp : declareStatement.split(" ")){
			if(tmp.equalsIgnoreCase("create")||tmp.equalsIgnoreCase("function")||tmp.equalsIgnoreCase("procedure")){
				name = name+tmp.toUpperCase(); 
			}
			else{
				name +=tmp;
			}
		}
		 
		 if(name.replace(" ", "").startsWith("CREATEFUNCTION")){
			name = name.replace(" ", "").replace("CREATEFUNCTION", ""); 
		 }
		 else if(name.replace(" ", "").startsWith("CREATEPROCEDURE")){
			 name =name.replace(" ", "").replace("CREATEPROCEDURE", "");
			 
		 }
		   
			return name;
			
	}
	 
	
	

	public static int countCharacters(String s, String ch) {
		int cnt = 0;
		String lines[] = s.split(ch);
		cnt = lines.length - 1;
		return cnt;
	}

	protected static int calculatleSimple(String line) {
		int complexity = 0;
		complexity += countCharacters(line, "AND");
		complexity += countCharacters(line, "OR");
		return Math.max(1, complexity);
	}
	

	protected static int calculateConditionESQLComplexity(String line) {
		return calculatleSimple(line);
	}

}

	
	
	
	
	
