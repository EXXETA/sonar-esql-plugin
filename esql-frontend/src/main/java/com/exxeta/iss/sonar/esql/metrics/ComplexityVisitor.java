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
package com.exxeta.iss.sonar.esql.metrics;

import java.util.ArrayList;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;


public class ComplexityVisitor extends DoubleDispatchVisitor {

  private List<Tree> complexityTrees;

  public int getComplexity(Tree tree) {
    return complexityTrees(tree).size();
  }

  public List<Tree> complexityTrees(Tree tree) {
    this.complexityTrees = new ArrayList<>();
    scan(tree);
    return this.complexityTrees;
  }

  @Override
	public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
		add(tree.createKeyword());
		super.visitCreateFunctionStatement(tree);
	}
  
  @Override
	public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
		add(tree.createKeyword());
		super.visitCreateProcedureStatement(tree);
	}
  
  @Override
	public void visitCreateModuleStatement(CreateModuleStatementTree tree) {
		add(tree.createKeyword());
		super.visitCreateModuleStatement(tree);
	}

  @Override
  public void visitIfStatement(IfStatementTree tree) {
    add(tree.ifKeyword());
    super.visitIfStatement(tree);
  }
  
  @Override
	public void visitRepeatStatement(RepeatStatementTree tree) {
		add(tree.repeatKeyword());
		super.visitRepeatStatement(tree);
	}
  @Override
	public void visitLoopStatement(LoopStatementTree tree) {
		add(tree.loopKeyword());
		super.visitLoopStatement(tree);
	}

  @Override
	public void visitCaseStatement(CaseStatementTree tree) {
		add(tree.caseKeyword());
		super.visitCaseStatement(tree);
	}

  @Override
  public void visitBinaryExpression(BinaryExpressionTree tree) {
    if (tree.is(Kind.CONDITIONAL_AND, Kind.CONDITIONAL_OR)) {
      add(tree.operator());
    }
    super.visitBinaryExpression(tree);
  }

  private void add(Tree tree) {
    complexityTrees.add(tree);
  }

}
