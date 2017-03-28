package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;
import java.util.List;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.WhileStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class WhileStatementTreeImpl extends EsqlTree implements WhileStatementTree{
	LabelTreeImpl label;
	InternalSyntaxToken colon;
	InternalSyntaxToken whileKeyword;
	ExpressionTree condition;
	InternalSyntaxToken doKeyword;
	List<StatementTree> statements;
	InternalSyntaxToken endKeyword;
	InternalSyntaxToken whileKeyword2;
	LabelTreeImpl label2;
	InternalSyntaxToken semi;
	public WhileStatementTreeImpl(LabelTreeImpl label, InternalSyntaxToken colon, InternalSyntaxToken whileKeyword,
			ExpressionTree condition, InternalSyntaxToken doKeyword, List<StatementTree> statements,
			InternalSyntaxToken endKeyword, InternalSyntaxToken whileKeyword2, LabelTreeImpl label2,
			InternalSyntaxToken semi) {
		super();
		this.label = label;
		this.colon = colon;
		this.whileKeyword = whileKeyword;
		this.condition = condition;
		this.doKeyword = doKeyword;
		this.statements = statements;
		this.endKeyword = endKeyword;
		this.whileKeyword2 = whileKeyword2;
		this.label2 = label2;
		this.semi = semi;
	}
	public WhileStatementTreeImpl(InternalSyntaxToken whileKeyword, ExpressionTree condition,
			InternalSyntaxToken doKeyword, List<StatementTree> statements, InternalSyntaxToken endKeyword,
			InternalSyntaxToken whileKeyword2, InternalSyntaxToken semi) {
		super();
		this.whileKeyword = whileKeyword;
		this.condition = condition;
		this.doKeyword = doKeyword;
		this.statements = statements;
		this.endKeyword = endKeyword;
		this.whileKeyword2 = whileKeyword2;
		this.semi = semi;
	}
	
	public LabelTreeImpl label() {
		return label;
	}

	public InternalSyntaxToken colon() {
		return colon;
	}

	public InternalSyntaxToken whileKeyword() {
		return whileKeyword;
	}

	public ExpressionTree condition() {
		return condition;
	}
	
	public InternalSyntaxToken doKeyword() {
		return doKeyword;
	}

	public List<StatementTree> statements() {
		return statements;
	}
	
	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	public InternalSyntaxToken whileKeyword2() {
		return whileKeyword2;
	}

	public LabelTreeImpl label2() {
		return label2;
	}

	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitWhileStatement(this);

	}

	@Override
	public Kind getKind() {
		return Kind.WHILE_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(label, colon, whileKeyword, condition, doKeyword), statements.iterator(),
				Iterators.forArray(endKeyword, whileKeyword2, label2, semi));
	}


}
