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
 * this java class is created to implement the logic for checking the blan lines before the block or single line comment.
 * @author C50679
 *
 */
@Rule(key = "BlankLineBeforeComments")
public class BlankLineBeforeCommentsCheck extends DoubleDispatchVisitorCheck{

	private static final String MESSAGE = "Insert one blank line before a block or single-line comment.";



	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String>  lines = CheckUtils.readLines(file);
		
		int linecounter = 0;
		int i = 0;
			for (i=0; i<lines.size(); i++){
				linecounter = linecounter + 1;
		    String currentline = lines.get(i);
			
			if ((currentline.trim().startsWith("--")) ||( currentline.trim().startsWith("/*") ) ){
				
				if (! lines.get(linecounter-1).matches("\\s*") ){
				
                 addIssue(new LineIssue(this, linecounter, MESSAGE));
				}
				
			}
		} 
	}
}


	

