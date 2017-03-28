package com.exxeta.iss.sonar.esql.tree.impl.expression;

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import java.util.Iterator;


public class BinaryExpressionTreeImpl extends EsqlTree implements BinaryExpressionTree, TypableTree {

  private final ExpressionTree leftOperand;
  private final SyntaxToken operand;
  private final ExpressionTree rightOperand;
  private final Kind kind;
  private TypeSet types = TypeSet.emptyTypeSet();

  public BinaryExpressionTreeImpl(Kind kind, ExpressionTree leftOperand, InternalSyntaxToken operand, ExpressionTree rightOperand) {
    this.leftOperand = Preconditions.checkNotNull(leftOperand);
    this.operand = operand;
    this.rightOperand = Preconditions.checkNotNull(rightOperand);
    this.kind = Preconditions.checkNotNull(kind);

  }

  @Override
  public ExpressionTree leftOperand() {
    return leftOperand;
  }

  @Override
  public SyntaxToken operator() {
    return operand;
  }

  @Override
  public ExpressionTree rightOperand() {
    return rightOperand;
  }

  @Override
  public Kind getKind() {
    return kind;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.forArray(leftOperand, operand, rightOperand);
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitBinaryExpression(this);
  }

  @Override
  public TypeSet types() {
    return types.immutableCopy();
  }

  @Override
  public void add(Type type) {
    types.add(type);
  }

}
