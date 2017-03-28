package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LabelTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class LabelTreeImpl extends EsqlTree implements LabelTree {

	private final InternalSyntaxToken name;

	public LabelTreeImpl(InternalSyntaxToken labelName) {
		this.name = labelName;
	}

	@Override
	public InternalSyntaxToken name() {
		return name;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitLabel(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.LABEL;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.singletonIterator(name);
	}
	
	

}
