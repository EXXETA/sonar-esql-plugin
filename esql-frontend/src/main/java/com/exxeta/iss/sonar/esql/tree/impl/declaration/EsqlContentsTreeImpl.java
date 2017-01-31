package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.EsqlContentsTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.Iterators;

public class EsqlContentsTreeImpl extends EsqlTree implements EsqlContentsTree{

	  private final List<StatementTree> items;

	  public EsqlContentsTreeImpl(List<StatementTree> list) {

	    this.items = list;

	  }

	  @Override
	  public List<StatementTree> items() {
	    return items;
	  }

	  @Override
	  public Kind getKind() {
	    return Kind.ESQL_CONTENTS;
	  }

	  @Override
	  public Iterator<Tree> childrenIterator() {
		  return Iterators.forArray(items.toArray(new Tree[items.size()]));
	  }

	  @Override
	  public void accept(DoubleDispatchVisitor visitor) {
	    visitor.visitEsqlContents(this);
	  }

	
}
