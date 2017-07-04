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
 */
package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="EmptyMainFunction")
public class EmptyMainFunctionCheck extends DoubleDispatchVisitorCheck{

	@Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		if ("Main".equalsIgnoreCase(tree.identifier().name())
				&& tree.routineBody().statement().is(Kind.BEGIN_END_STATEMENT)){
				BeginEndStatementTree beginEnd = (BeginEndStatementTree) tree.routineBody().statement();
				StatementsTree statements = beginEnd.statements();
				if (statements.statements().size()==1 && statements.statements().get(0).is(Kind.RETURN_STATEMENT) ){
					ReturnStatementTree returnStatement = (ReturnStatementTree) statements.statements().get(0);
					if (returnStatement.expression().is(Kind.BOOLEAN_LITERAL)){
						addIssue(tree, "Remove this empty Main function, or fill it with code.");
					}
				}
		}
	}
	
}
