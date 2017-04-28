package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedFieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.function.FromClauseExpressionTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class FromClauseExpressionTreeImpl extends EsqlTree implements FromClauseExpressionTree {

	private InternalSyntaxToken fromKeyword;
	private SeparatedList<AliasedFieldReferenceTree> aliasedFieldReferences;

	public FromClauseExpressionTreeImpl(InternalSyntaxToken fromKeyword,
			SeparatedList<AliasedFieldReferenceTree> aliasedFieldReferences) {
		super();
		this.fromKeyword = fromKeyword;
		this.aliasedFieldReferences = aliasedFieldReferences;
	}

	@Override
	public InternalSyntaxToken fromKeyword() {
		return fromKeyword;
	}

	@Override
	public SeparatedList<AliasedFieldReferenceTree> aliasedFieldReferences() {
		return aliasedFieldReferences;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitFromClauseExpression(this);
	}

	@Override
	public Kind getKind() {
		return Kind.ALIASED_FIELD_REFERENCE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.singletonIterator(fromKeyword),
				aliasedFieldReferences.elementsAndSeparators(Functions.<AliasedFieldReferenceTree>identity()));
	}

}
