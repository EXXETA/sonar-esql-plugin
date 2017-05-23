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
package com.exxeta.iss.sonar.esql.tree.symbols;

import java.util.Map;

import com.exxeta.iss.sonar.esql.api.symbols.Symbol;
import com.exxeta.iss.sonar.esql.api.symbols.SymbolModelBuilder;
import com.exxeta.iss.sonar.esql.api.symbols.Usage;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.BlockTree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.Scope;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;

/**
 * This visitor creates new symbols for not hoisted variables (like class name) and implicitly declared variables (declared without keyword).
 * Also it creates usages for all known symbols.
 */
public class SymbolVisitor extends DoubleDispatchVisitor {

  private SymbolModelBuilder symbolModel;
  private Scope currentScope;
  private Map<Tree, Scope> treeScopeMap;

  public SymbolVisitor(Map<Tree, Scope> treeScopeMap) {
    this.treeScopeMap = treeScopeMap;
  }

  @Override
  public void visitProgram(ProgramTree tree) {
    this.symbolModel = (SymbolModelBuilder) getContext().getSymbolModel();
    this.currentScope = null;

    enterScope(tree);
    super.visitProgram(tree);
    leaveScope();
  }


  @Override
  public void visitBlock(BlockTree tree) {
    if (isScopeAlreadyEntered(tree)) {
      super.visitBlock(tree);

    } else {
      enterScope(tree);
      super.visitBlock(tree);
      leaveScope();
    }
  }


  private void leaveScope() {
    if (currentScope != null) {
      currentScope = currentScope.outer();
    }
  }

  private void enterScope(Tree tree) {
    currentScope = treeScopeMap.get(tree);
    if (currentScope == null) {
      throw new IllegalStateException("No scope found for the tree");
    }
  }

  /**
   * @return true if symbol found and usage recorded, false otherwise.
   */
  private boolean addUsageFor(IdentifierTree identifier, Usage.Kind kind) {
    Symbol symbol = currentScope.lookupSymbol(identifier.name());
    if (symbol != null) {
      symbol.addUsage(Usage.create(identifier, kind));
      return true;
    }
    return false;
  }

  private boolean isScopeAlreadyEntered(BlockTree tree) {
    return !treeScopeMap.containsKey(tree);
  }

  private Scope getFunctionScope() {
    Scope scope = currentScope;
    while (scope.isBlock()) {
      scope = scope.outer();
    }
    return scope;
  }

 
}
