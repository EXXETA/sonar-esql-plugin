package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetColumnTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.UpdateStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class UpdateStatementTreeImpl extends EsqlTree implements UpdateStatementTree {
	private InternalSyntaxToken updateKeyword;

	private FieldReferenceTreeImpl tableReference;

	private InternalSyntaxToken asKeyword;

	private InternalSyntaxToken alias;

	private InternalSyntaxToken setKeyword;

	private SeparatedList<SetColumnTree> setColumns;

	private InternalSyntaxToken whereKeyword;

	private ExpressionTree whereExpression;

	private InternalSyntaxToken semi;

	public UpdateStatementTreeImpl(InternalSyntaxToken updateKeyword, FieldReferenceTreeImpl tableReference,
			InternalSyntaxToken asKeyword, InternalSyntaxToken alias, InternalSyntaxToken setKeyword,
			SeparatedList<SetColumnTree> setColumns, InternalSyntaxToken whereKeyword, ExpressionTree whereExpression,
			InternalSyntaxToken semi) {
		super();
		this.updateKeyword = updateKeyword;
		this.tableReference = tableReference;
		this.asKeyword = asKeyword;
		this.alias = alias;
		this.setKeyword = setKeyword;
		this.setColumns = setColumns;
		this.whereKeyword = whereKeyword;
		this.whereExpression = whereExpression;
		this.semi = semi;
	}

	public InternalSyntaxToken updateKeyword() {
		return updateKeyword;
	}

	public FieldReferenceTreeImpl tableReference() {
		return tableReference;
	}

	public InternalSyntaxToken asKeyword() {
		return asKeyword;
	}

	public InternalSyntaxToken alias() {
		return alias;
	}

	public InternalSyntaxToken setKeyword() {
		return setKeyword;
	}

	public SeparatedList<SetColumnTree> setColumns() {
		return setColumns;
	}

	public InternalSyntaxToken whereKeyword() {
		return whereKeyword;
	}

	public ExpressionTree whereExpression() {
		return whereExpression;
	}

	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitUpdateStatement(this);
	}

	@Override
	public Kind getKind() {
		return Kind.UPDATE_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(updateKeyword, tableReference, asKeyword, alias, setKeyword),
				setColumns.elementsAndSeparators(Functions.<SetColumnTree>identity()),
				Iterators.forArray(whereKeyword, whereExpression, semi));
	}

}
