/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

/**
 * This Java class is created to implement the logic to find all the unused variables.
 * @author Sapna Singh
 *
 */
@Rule(key = "UnusedVariable")
public class UnusedVariableCheck extends DoubleDispatchVisitorCheck {
	
	private static final String MESSAGE = "Remove the unused Variable.";

	private Set<String> calledRoutines = new HashSet<>();
	private List<String> variables = new ArrayList<>();
	private HashMap<String, DeclareStatementTree> declaredVariable = new HashMap<>();
	
	@Override
	public void visitDeclareStatement(DeclareStatementTree tree) {
		
		for (int i = 0; i < tree.nameList().size(); i++) {
		declaredVariable.put(tree.nameList().get(i).name(), tree);
		
		variables.add(tree.nameList().get(i).name());
		}
		super.visitDeclareStatement(tree);
	}
	
	@Override
	public void visitProgram(ProgramTree tree) {
		
		//calledRoutines.clear();
		//declaredVariable.clear();
		super.visitProgram(tree);
		
		
		
		EsqlFile file = getContext().getEsqlFile();
		List<String> lines = CheckUtils.readLines(file);
		for (String line : lines) {
			
		    String upperCaseTheLine = line.toUpperCase();
		    
			for (String Vars : variables) {
				
				if(line.contains(Vars) && !upperCaseTheLine.contains("DECLARE")){
					calledRoutines.add(Vars);	
				}
				
		}
			
	}
		for (String variable : calledRoutines) {
			declaredVariable.remove(variable);
		}
		for (Entry<String, DeclareStatementTree> variable : declaredVariable.entrySet()) {
			addIssue(new PreciseIssue(this, new IssueLocation(variable.getValue(),
					variable.getValue(), String.format(MESSAGE, variable.getKey()))));
		}
		
		
		
		
}

	
	
	
}


