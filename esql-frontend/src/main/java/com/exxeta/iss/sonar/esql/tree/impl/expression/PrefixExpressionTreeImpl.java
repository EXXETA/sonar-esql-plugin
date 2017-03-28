package com.exxeta.iss.sonar.esql.tree.impl.expression;

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.UnaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import java.util.Iterator;


public class PrefixExpressionTreeImpl extends EsqlTree implements UnaryExpressionTree, TypableTree {

  private final Kind kind;
  private final InternalSyntaxToken operator;
  private final ExpressionTree expression;
  private TypeSet types = TypeSet.emptyTypeSet();

  public PrefixExpressionTreeImpl(Kind kind, InternalSyntaxToken operator, ExpressionTree expression) {
    this.kind = Preconditions.checkNotNull(kind);
    this.operator = operator;
    this.expression = Preconditions.checkNotNull(expression);
  }

  @Override
  public Kind getKind() {
    return kind;
  }

  @Override
  public SyntaxToken operator() {
    return operator;
  }

  @Override
  public ExpressionTree expression() {
    return expression;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.forArray(operator, expression);
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitUnaryExpression(this);
  }

  @Override
  public TypeSet types() {
    return types.immutableCopy();
  }

  @Override
  public void add(Type type) {
    this.types.add(type);
  }

}
