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
import java.util.List;
import java.util.Map;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.function.PassthruFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeleteFromStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PassthruStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;

@Rule(key = "DeleteFromWithoutWhere")
public class DeleteFromWithoutWhereCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Add a where caluse to this DELETE FROM statement.";

	private Map<Tree, List<LiteralTree>> passthruStack = new HashMap<>();
	Tree currentPassthru = null;
	
	
	@Override
	public void visitProgram(ProgramTree tree) {
		passthruStack.clear();
		currentPassthru=null;
		super.visitProgram(tree);
	}
	
	
	@Override
	public void visitDeleteFromStatement(DeleteFromStatementTree tree) {
		if (tree.whereExpression() == null) {
			addIssue(tree, MESSAGE);
		}
	}

	@Override
	public void visitPassthruStatement(PassthruStatementTree tree) {
		Tree prevPassthru = currentPassthru;
		currentPassthru = tree;
		passthruStack.put(tree,new ArrayList<>());
		super.visitPassthruStatement(tree);
		List<LiteralTree> literals = passthruStack.remove(tree);
		checkLiterals(tree, literals);
		currentPassthru=prevPassthru;
	}
	
	@Override
	public void visitPassthruFunction(PassthruFunctionTree tree) {
		Tree prevPassthru = currentPassthru;
		currentPassthru = tree;
		passthruStack.put(tree,new ArrayList<>());
		super.visitPassthruFunction(tree);
		List<LiteralTree> literals = passthruStack.remove(tree);
		checkLiterals(tree, literals);
		currentPassthru=prevPassthru;
		
	}
	
	@Override
	public void visitLiteral(LiteralTree tree) {
		passthruStack.get(currentPassthru).add(tree);
		super.visitLiteral(tree);
	}
	
	private void checkLiterals(Tree tree, List<LiteralTree> literals) {
		if (!literals.isEmpty() && literals.get(0).value().toUpperCase().matches("'.*DELETE\\s+FROM.*")){
			boolean hasWhereClause=false;
			for (LiteralTree literal:literals){
				if (literal.value().toUpperCase().matches(".*WHERE.*")){
					hasWhereClause=true;
				}
						
			}
			if (!hasWhereClause){
				addIssue(tree, MESSAGE);
			}
		}
	}

}
