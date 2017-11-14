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
 * This java class is created to implement the logic for checking the length of
 * the function or procedure
 * 
 * @author Arjav Shah
 *
 */
@Rule(key = "FunctionProcedureLength")
public class FunctionProcedureLengthCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = " lines, which is higher than the allowed threshold.";

	private static final int LENGTH_THRESHOLD = 150;

	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String> lines = CheckUtils.readLines(file);
		int i = 0;
		boolean commentSection = false;
		ArrayList<ArrayList<String>> modules = new ArrayList<ArrayList<String>>();
		ArrayList<String> moduleLines = new ArrayList<String>();
		boolean isInsideModule = false;
		for (String line : lines) {
			i = i + 1;

			if (!line.trim().startsWith("--") && !line.trim().startsWith("/*") && !commentSection) {
				if (!isBeginStatement(line) && !isEndStatement(line) && isInsideModule && !line.trim().isEmpty()) {
					moduleLines.add(line);
				}
				if (isBeginStatement(line.trim())) {
					isInsideModule = true;
					moduleLines = new ArrayList<String>();
					moduleLines.add(String.valueOf(i));
					moduleLines.add(line);
				}
				if (isEndStatement(line.trim())) {
					moduleLines.add(line);
					isInsideModule = false;
					modules.add(moduleLines);

				}
			} else if (line.trim().startsWith("/*") && !commentSection && !line.trim().endsWith("*/")) {
				commentSection = true;
			} else if (commentSection && line.trim().endsWith("*/")) {
				commentSection = false;
			}
		}
		for (ArrayList<String> module : modules) {

			if (module.size()-1 > LENGTH_THRESHOLD) {
				addIssue(new LineIssue(this,  Integer.parseInt(module.get(0)), ExtractFunctionProcedureName(module.get(1))+ "\" is of length "+String.valueOf(module.size()-1) + MESSAGE+"(Threshold : "+LENGTH_THRESHOLD+")"));
			}
		}

	}

	public static boolean isEndStatement(String s) {
		String withoutSpace = s.replace(" ", "").toUpperCase();
		return withoutSpace.contains("END;");
	}

	public static boolean isBeginStatement(String s) {
		String withoutSpace = s.replace(" ", "").toUpperCase();
		return withoutSpace.startsWith("CREATEPROCEDURE") || withoutSpace.startsWith("CREATEFUNCTION");
	}
	 public static  String ExtractFunctionProcedureName(String declareStatement ){

		 declareStatement = declareStatement.substring(0,declareStatement.indexOf("("));
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
			name = "Function \""+name.replace(" ", "").replace("CREATEFUNCTION", ""); 
		 }
		 else if(name.replace(" ", "").startsWith("CREATEPROCEDURE")){
			 name = "Procedure \""+name.replace(" ", "").replace("CREATEPROCEDURE", "");
			 
		 }
		   
			return name;
			
	}

}
