/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.function.PassthruFunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PassthruStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;

public abstract class AbstractPassthruCheck extends DoubleDispatchVisitorCheck{

	private Map<Tree, List<LiteralTree>> passthruStack = new HashMap<>();
	Tree currentPassthru = null;
	

	
	@Override
	public void visitProgram(ProgramTree tree) {
		passthruStack.clear();
		currentPassthru=null;
		super.visitProgram(tree);
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
	
	protected abstract  void checkLiterals(Tree tree, List<LiteralTree> literals) ;


	@Override
	public void visitLiteral(LiteralTree tree) {
		if (currentPassthru!=null){
			passthruStack.get(currentPassthru).add(tree);
		}
		super.visitLiteral(tree);
	}
	

	
}
