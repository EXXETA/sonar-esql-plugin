/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

@Rule(key = "IfConditionalAlwaysTrueOrFalse")
public class IfConditionalAlwaysTrueOrFalseCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Remove this \"IF\" statement.";

	@Override
	public void visitIfStatement(IfStatementTree tree) {
		checkTree(tree.condition());  

		super.visitIfStatement(tree);
	}

	private void checkTree(Tree tree) {
		if (tree.is(Kind.BOOLEAN_LITERAL)) {
			addIssue(tree, MESSAGE);
		} else if (! (tree instanceof InternalSyntaxToken)) {
			List<Tree> list = tree.childrenStream().filter(Objects::nonNull).collect(Collectors.toList());
			if (list.size()==1){
				if (list.get(0).is(Kind.BOOLEAN_LITERAL)){
					addIssue(list.get(0), MESSAGE);
				} else {
					checkTree(list.get(0));
				}
			}
			
			
		}
	}

	@Override
	public void visitElseifClause(ElseifClauseTree tree) {
		checkTree (tree.condition()) ;

		super.visitElseifClause(tree);
	}

}
