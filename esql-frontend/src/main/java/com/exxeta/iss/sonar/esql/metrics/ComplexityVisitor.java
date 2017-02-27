package com.exxeta.iss.sonar.esql.metrics;

import java.util.ArrayList;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.function.FunctionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;


public class ComplexityVisitor extends DoubleDispatchVisitor {

  private boolean mustAnalyseNestedFunctions;

  private List<Tree> complexityTrees;

  private boolean isInsideFunction;

  public ComplexityVisitor(boolean mustAnalyseNestedFunctions) {
    this.mustAnalyseNestedFunctions = mustAnalyseNestedFunctions;
  }

  public int getComplexity(Tree tree) {
    return complexityTrees(tree).size();
  }

  public List<Tree> complexityTrees(Tree tree) {
    this.complexityTrees = new ArrayList<>();
    this.isInsideFunction = false;
    scan(tree);
    return this.complexityTrees;
  }

  private void visitFunction(FunctionTree functionTree, Tree complexityTree) {
    if (mustAnalyse()) {
      add(complexityTree);

      isInsideFunction = true;
      scanChildren(functionTree);
      isInsideFunction = false;
    }
  }

  @Override
  public void visitMethodDeclaration(MethodDeclarationTree tree) {
    visitFunction(tree, tree.name());
  }

  @Override
  public void visitFunctionDeclaration(FunctionDeclarationTree tree) {
    visitFunction(tree, tree.functionKeyword());
  }

  @Override
  public void visitFunctionExpression(FunctionExpressionTree tree) {
    visitFunction(tree, tree.functionKeyword());
  }

  @Override
  public void visitArrowFunction(ArrowFunctionTree tree) {
    visitFunction(tree, tree.doubleArrow());
  }

  @Override
  public void visitIfStatement(IfStatementTree tree) {
    add(tree.ifKeyword());
    super.visitIfStatement(tree);
  }

  @Override
  public void visitWhileStatement(WhileStatementTree tree) {
    add(tree.whileKeyword());
    super.visitWhileStatement(tree);
  }

  @Override
  public void visitDoWhileStatement(DoWhileStatementTree tree) {
    add(tree.doKeyword());
    super.visitDoWhileStatement(tree);
  }

  @Override
  public void visitForStatement(ForStatementTree tree) {
    add(tree.forKeyword());
    super.visitForStatement(tree);
  }

  @Override
  public void visitForObjectStatement(ForObjectStatementTree tree) {
    add(tree.forKeyword());
    super.visitForObjectStatement(tree);
  }

  @Override
  public void visitCaseClause(CaseClauseTree tree) {
    add(tree.keyword());
    super.visitCaseClause(tree);
  }

  @Override
  public void visitConditionalExpression(ConditionalExpressionTree tree) {
    add(tree.query());
    super.visitConditionalExpression(tree);
  }

  @Override
  public void visitBinaryExpression(BinaryExpressionTree tree) {
    if (tree.is(Kind.CONDITIONAL_AND, Kind.CONDITIONAL_OR)) {
      add(tree.operator());
    }
    super.visitBinaryExpression(tree);
  }

  private boolean mustAnalyse() {
    return mustAnalyseNestedFunctions || !isInsideFunction;
  }

  private void add(Tree tree) {
    complexityTrees.add(tree);
  }

}
