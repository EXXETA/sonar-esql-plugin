package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;
import java.util.List;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.RepeatStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class RepeatStatementTreeImpl extends EsqlTree implements RepeatStatementTree {
	LabelTreeImpl label;
	InternalSyntaxToken colon;
	InternalSyntaxToken repeatKeyword;
	List<StatementTree> statements;
	InternalSyntaxToken untilKeyword;
	ExpressionTree condition;
	InternalSyntaxToken endKeyword;
	InternalSyntaxToken repeatKeyword2;
	LabelTreeImpl label2;
	InternalSyntaxToken semi;

	public RepeatStatementTreeImpl(LabelTreeImpl label, InternalSyntaxToken colon, InternalSyntaxToken repeatKeyword,
			List<StatementTree> statements, InternalSyntaxToken untilKeyword, ExpressionTree condition,
			InternalSyntaxToken endKeyword, InternalSyntaxToken repeatKeyword2, LabelTreeImpl label2,
			InternalSyntaxToken semi) {
		super();
		this.label = label;
		this.colon = colon;
		this.repeatKeyword = repeatKeyword;
		this.statements = statements;
		this.untilKeyword = untilKeyword;
		this.condition = condition;
		this.endKeyword = endKeyword;
		this.repeatKeyword2 = repeatKeyword2;
		this.label2 = label2;
		this.semi = semi;
	}

	public RepeatStatementTreeImpl(InternalSyntaxToken repeatKeyword, List<StatementTree> statements,
			InternalSyntaxToken untilKeyword, ExpressionTree condition, InternalSyntaxToken endKeyword,
			InternalSyntaxToken repeatKeyword2, InternalSyntaxToken semi) {
		super();
		this.repeatKeyword = repeatKeyword;
		this.statements = statements;
		this.untilKeyword = untilKeyword;
		this.condition = condition;
		this.endKeyword = endKeyword;
		this.repeatKeyword2 = repeatKeyword2;
		this.semi = semi;
	}

	public LabelTreeImpl label() {
		return label;
	}

	public InternalSyntaxToken colon() {
		return colon;
	}

	public InternalSyntaxToken repeatKeyword() {
		return repeatKeyword;
	}

	public List<StatementTree> statements() {
		return statements;
	}

	public InternalSyntaxToken untilKeyword() {
		return untilKeyword;
	}

	public ExpressionTree condition() {
		return condition;
	}

	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	public InternalSyntaxToken repeatKeyword2() {
		return repeatKeyword2;
	}

	public LabelTreeImpl label2() {
		return label2;
	}

	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitRepeatStatement(this);

	}

	@Override
	public Kind getKind() {
		return Kind.REPEAT_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(label, colon, repeatKeyword), statements.iterator(),
				Iterators.forArray(untilKeyword, condition, endKeyword, repeatKeyword2, label2, semi));
	}

}
