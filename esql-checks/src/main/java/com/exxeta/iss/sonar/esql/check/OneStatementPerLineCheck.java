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
 */package com.exxeta.iss.sonar.esql.check;

import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;

@Rule(key = "OneStatementPerLine")
public class OneStatementPerLineCheck extends SubscriptionVisitorCheck {

	
	
	private static final String MESSAGE = "Reformat the code to have only one statement per line.";

	  private ListMultimap<Integer, StatementTree> statementsPerLine = ArrayListMultimap.create();

	  @Override
	  public List<Kind> nodesToVisit() {
	    return ImmutableList.of(
	        Kind.IF_STATEMENT,
            Kind.DECLARE_STATEMENT,
            Kind.BROKER_SCHEMA_STATEMENT,
            Kind.CREATE_FUNCTION_STATEMENT,
            Kind.CREATE_MODULE_STATEMENT,
            Kind.CREATE_PROCEDURE_STATEMENT,
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
            Kind.LOG_STATEMENT,
	        Kind.PROGRAM);
	  }

	  @Override
	  public void visitFile(Tree scriptTree) {
	    statementsPerLine.clear();
	  }

	  @Override
	  public void visitNode(Tree tree) {
	   

		  if (!tree.is(Kind.PROGRAM)){
		      statementsPerLine.put(((EsqlTree) tree).getLine(), (StatementTree) tree);
		    }
	  }



	  @Override
	  public void leaveNode(Tree tree) {
	    if (tree.is(Kind.PROGRAM)){
	      for (int line : statementsPerLine.keys().elementSet()) {
	        List<StatementTree> statementsAtLine = statementsPerLine.get(line);

	        if (statementsAtLine.size() > 1) {
	          addIssue(statementsAtLine);
	        }
	      }
	    }
	  }

	  private void addIssue(List<StatementTree> statementsAtLine) {
	    PreciseIssue issue = addIssue(statementsAtLine.get(1), MESSAGE);

	    for (int i = 2; i < statementsAtLine.size(); i++) {
	      issue.secondary(new IssueLocation(statementsAtLine.get(i)));
	    }
	  }
	
	

}