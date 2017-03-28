package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.IndexTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class IndexTreeImpl extends EsqlTree implements IndexTree {

	private InternalSyntaxToken openBracket;
	private InternalSyntaxToken direction;
	private ExpressionTree index;
	private InternalSyntaxToken closeBracket;

	public IndexTreeImpl(InternalSyntaxToken openBracket, InternalSyntaxToken direction, ExpressionTree index,
			InternalSyntaxToken closeBracket) {
		super();
		this.openBracket = openBracket;
		this.direction = direction;
		this.index = index;
		this.closeBracket = closeBracket;
	}

	public InternalSyntaxToken openBracket() {
		return openBracket;
	}

	public InternalSyntaxToken direction() {
		return direction;
	}

	public ExpressionTree index() {
		return index;
	}

	public InternalSyntaxToken closeBracket() {
		return closeBracket;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitIndex(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.INDEX;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(openBracket, direction, index, closeBracket);
	}
	
	

}
