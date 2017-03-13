package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;
import org.sonar.api.measures.AverageFormula;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;

public class FieldReferenceTreeImpl extends EsqlTree implements FieldReferenceTree {

	private final ExpressionTree primaryExpression;
	private final InternalSyntaxToken variable;
	private final PathElementTree pathElement;
	private final SeparatedList<PathElementTree> pathElements;

	public FieldReferenceTreeImpl(ExpressionTree primaryExpression, SeparatedList<PathElementTree> pathElements) {
		this.primaryExpression = primaryExpression;
		this.variable=null;
		this.pathElements = pathElements;
		this.pathElement=null;
	}

	public FieldReferenceTreeImpl(InternalSyntaxToken variable, SeparatedList<PathElementTree> pathElements) {
		this.primaryExpression=null;
		this.variable = variable;
		this.pathElements = pathElements;
		this.pathElement=null;
	}

	public FieldReferenceTreeImpl(PathElementTree pathElement, SeparatedList<PathElementTree> pathElements) {
		this.primaryExpression=null;
		this.variable = null;
		this.pathElements = pathElements;
		this.pathElement=pathElement;
	}

	public ExpressionTree primaryExpression() {
		return primaryExpression;
	}
	
	public InternalSyntaxToken variable() {
		return variable;
	}
	
	public PathElementTree pathElement() {
		return pathElement;
	}

	public SeparatedList<PathElementTree> pathElements() {
		return pathElements;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitFieldReference(this);

	}

	@Override
	public Kind getKind() {

		return Kind.FIELD_REFERENCE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(primaryExpression, variable, pathElement),
				pathElements.elementsAndSeparators(Functions.<PathElementTree>identity()));
	}

}
