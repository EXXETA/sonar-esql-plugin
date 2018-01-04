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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
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
	private List<String> variables = new ArrayList<String>();
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
			
	        String  thelines = line.toString();
		    String upperCaseTheLine = thelines.toUpperCase();
		    
			for (String Vars : variables) {
				
				if(thelines.contains(Vars) && !upperCaseTheLine.contains("DECLARE")){
					calledRoutines.add(Vars);	
				}
				
		}
			
	}
		for (String variable : calledRoutines) {
			declaredVariable.remove(variable);
		}
		int i = 0;
		for (Entry<String, DeclareStatementTree> variable : declaredVariable.entrySet()) {
			i = i + 1;
			String Variabl =variable.getKey();
			
			addIssue(new LineIssue(this, i, "Check Variable \"" + Variabl + "\". " + MESSAGE));
		}
		
		
		
		
}

	
	
	
}

