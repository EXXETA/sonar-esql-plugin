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
