/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.metrics;

import java.util.HashSet;
import java.util.Set;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.ImmutableSet;

public class ExecutableLineVisitor extends SubscriptionVisitorCheck {

  private final Set<Integer> executableLines = new HashSet<>();

  public ExecutableLineVisitor(Tree tree) {
    scanTree(tree);
  }

  @Override
  public Set<Kind> nodesToVisit() {

	  return ImmutableSet.of(
				Kind.IF_STATEMENT, 
				Kind.DECLARE_STATEMENT, 
				Kind.THE_FUNCTION, 
//				Kind.CREATE_FUNCTION_STATEMENT, 
//				Kind.CREATE_MODULE_STATEMENT, 
//				Kind.CREATE_PROCEDURE_STATEMENT, 
				Kind.PROPAGATE_STATEMENT, 
				Kind.BEGIN_END_STATEMENT, 
				Kind.SET_STATEMENT,
				Kind.ITERATE_STATEMENT, 
				Kind.CALL_STATEMENT, 
				Kind.CASE_STATEMENT, 
				Kind.LEAVE_STATEMENT, 
				Kind.LOOP_STATEMENT, 
				Kind.REPEAT_STATEMENT, 
				Kind.RETURN_STATEMENT,
				Kind.THROW_STATEMENT, 
				Kind.WHILE_STATEMENT, 
				Kind.ATTACH_STATEMENT, 
				Kind.CREATE_STATEMENT, 
				Kind.DELETE_STATEMENT, 
				Kind.DETACH_STATEMENT, 
				Kind.RESIGNAL_STATEMENT, 
				Kind.FOR_STATEMENT, 
				Kind.MOVE_STATEMENT, 
				Kind.DELETE_FROM_STATEMENT, 
				Kind.INSERT_STATEMENT, 
				Kind.PASSTHRU_STATEMENT, 
				Kind.UPDATE_STATEMENT, 
				Kind.DECLARE_HANDLER_STATEMENT, 
				Kind.EVAL_STATEMENT, 
				Kind.LOG_STATEMENT
			  );
  }
  
  
  @Override
  public void visitNode(Tree tree) {
	  if (tree instanceof DeclareStatementTree){
		  if (tree.parent() instanceof CreateModuleStatementTree){
			  //ignore since the trace log doesn't contain this command
		  } else {
			  executableLines.add(((EsqlTree) tree).getLine());
		  }
	  } else {
		  executableLines.add(((EsqlTree) tree).getLine());
	  }
  }

  public Set<Integer> getExecutableLines() {
    return executableLines;
  }

}
