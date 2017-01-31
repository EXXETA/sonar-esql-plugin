package com.exxeta.iss.sonar.esql.tree.impl.expression;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;

public class LiteralTreeImpl extends EsqlTree implements LiteralTree, TypableTree {

	  private final Kind kind;
	  private final SyntaxToken token;
	  private TypeSet types = TypeSet.emptyTypeSet();

	  public LiteralTreeImpl(Kind kind, SyntaxToken token) {
	    this.kind = Preconditions.checkNotNull(kind);
	    this.token = token;
	  }

	  @Override
	  public Kind getKind() {
	    return kind;
	  }

	  @Override
	  public String value() {
	    return token.text();
	  }


	  @Override
	  public TypeSet types() {
	    return types.immutableCopy();
	  }

	  @Override
	  public void add(Type type) {
	    this.types.add(type);
	  }

	  @Override
	  public SyntaxToken token() {
	    return token;
	  }

	  @Override
	  public Iterator<Tree> childrenIterator() {
	    return Iterators.<Tree>singletonIterator(token);
	  }

	  @Override
	  public void accept(DoubleDispatchVisitor visitor) {
	    visitor.visitLiteral(this);
	  }

	  @Override
	  public String toString() {
	    return token.text();
	  }
	}
