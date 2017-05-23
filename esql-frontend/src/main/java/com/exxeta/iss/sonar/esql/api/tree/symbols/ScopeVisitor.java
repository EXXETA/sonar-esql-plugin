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
package com.exxeta.iss.sonar.esql.api.tree.symbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.exxeta.iss.sonar.esql.api.symbols.SymbolModelBuilder;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.BlockTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;

/**
 * This visitor creates scopes.
 */
public class ScopeVisitor extends DoubleDispatchVisitor {

  private SymbolModelBuilder symbolModel;
  private Scope currentScope;
  private Map<Tree, Scope> treeScopeMap;

  // List of block trees for which scope is created for another tree (e.g. function declaration or for statement)
  private List<BlockTree> skippedBlocks;

  public Map<Tree, Scope> getTreeScopeMap() {
    return treeScopeMap;
  }

  @Override
  public void visitProgram(ProgramTree tree) {
    this.symbolModel = (SymbolModelBuilder) getContext().getSymbolModel();
    this.currentScope = null;
    this.skippedBlocks = new ArrayList<>();
    this.treeScopeMap = new HashMap<>();

    newFunctionScope(tree);
    super.visitProgram(tree);
    leaveScope();
  }

  @Override
  public void visitBlock(BlockTree tree) {
    if (isScopeAlreadyCreated(tree)) {
      super.visitBlock(tree);

    } else {
      newBlockScope(tree);
      super.visitBlock(tree);
      leaveScope();
    }
  }


  private void leaveScope() {
    if (currentScope != null) {
      currentScope = currentScope.outer();
    }
  }

  private void newFunctionScope(Tree tree) {
    newScope(tree, false);
  }

  private void newBlockScope(Tree tree) {
    newScope(tree, true);
  }

  private void newScope(Tree tree, boolean isBlock) {
    currentScope = new Scope(currentScope, tree, isBlock);
    treeScopeMap.put(tree, currentScope);
    symbolModel.addScope(currentScope);
  }

/*  private void skipBlock(Tree tree) {
    if (tree.is(Kind.BLOCK)) {
      skippedBlocks.add((BlockTree) tree);
    }
  }*/

  private boolean isScopeAlreadyCreated(BlockTree tree) {
    return skippedBlocks.contains(tree);
  }

}
