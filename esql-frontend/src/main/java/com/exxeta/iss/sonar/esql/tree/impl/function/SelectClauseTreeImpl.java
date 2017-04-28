package com.exxeta.iss.sonar.esql.tree.impl.function;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.AliasedFieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.function.SelectClauseTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class SelectClauseTreeImpl extends EsqlTree implements SelectClauseTree {

	private SeparatedList<AliasedFieldReferenceTree> aliasedFieldReferenceList;
	private InternalSyntaxToken itemKeyword;
	private ExpressionTree itemExpression;
	private InternalSyntaxToken aggregationType;
	private InternalSyntaxToken openingParenthesis;
	private ExpressionTree aggregationExpression;
	private InternalSyntaxToken closingParenthesis;

	public SelectClauseTreeImpl(SeparatedList<AliasedFieldReferenceTree> aliasedFieldReferenceList) {
		this.aliasedFieldReferenceList = aliasedFieldReferenceList;
	}

	public SelectClauseTreeImpl(InternalSyntaxToken itemKeyword, ExpressionTree itemExpression) {
		super();
		this.itemKeyword = itemKeyword;
		this.itemExpression = itemExpression;
	}

	public SelectClauseTreeImpl(InternalSyntaxToken aggregationType, InternalSyntaxToken openingParenthesis,
			ExpressionTree aggregationExpression, InternalSyntaxToken closingParenthesis) {
		super();
		this.aggregationType = aggregationType;
		this.openingParenthesis = openingParenthesis;
		this.aggregationExpression = aggregationExpression;
		this.closingParenthesis = closingParenthesis;
	}

	@Override
	public SeparatedList<AliasedFieldReferenceTree> aliasedFieldReferenceList() {
		return aliasedFieldReferenceList;
	}

	@Override
	public InternalSyntaxToken itemKeyword() {
		return itemKeyword;
	}

	@Override
	public ExpressionTree itemExpression() {
		return itemExpression;
	}

	@Override
	public InternalSyntaxToken aggregationType() {
		return aggregationType;
	}

	@Override
	public InternalSyntaxToken openingParenthesis() {
		return openingParenthesis;
	}

	@Override
	public ExpressionTree aggregationExpression() {
		return aggregationExpression;
	}

	@Override
	public InternalSyntaxToken closingParenthesis() {
		return closingParenthesis;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSelectClause(this);
	}

	@Override
	public Kind getKind() {
		return Kind.SELECT_CLAUSE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(
				aliasedFieldReferenceList.elementsAndSeparators(Functions.<AliasedFieldReferenceTree>identity()),
				Iterators.forArray(itemKeyword, itemExpression, aggregationType, openingParenthesis,
						aggregationExpression, closingParenthesis));
	}

}
