/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;

/**
 * This java class is created to implement the logic for checking there should be one blank line between procedure and function.
 * @author C50679
 *
 */
@Rule(key = "InsertBlankLineBetweenFuncProc")
public class InsertBlankLineBetweenFuncProcCheck extends DoubleDispatchVisitorCheck{

	private static final String MESSAGE = "Insert one blank line between functions and procedures.";



	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String>  lines = CheckUtils.readLines(file);
		
		int linecounter = 0;
		for (String line : lines) {
			linecounter = linecounter + 1;

			if(isEndStatement(line)){

				if(! line.startsWith("\\n")){
					addIssue(new LineIssue(this, linecounter, MESSAGE));
				}
			}
		} 
	}

	public static boolean isEndStatement(String s) {
		String withoutSpace = s.replace(" ", "").toUpperCase();
		return withoutSpace.contains("END;");
	}

}


