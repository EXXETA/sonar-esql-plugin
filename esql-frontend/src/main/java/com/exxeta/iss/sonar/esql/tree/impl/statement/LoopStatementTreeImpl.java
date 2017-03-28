package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;
import java.util.List;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LoopStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class LoopStatementTreeImpl extends EsqlTree implements LoopStatementTree {

	private LabelTreeImpl label;
	private InternalSyntaxToken colon;
	private InternalSyntaxToken loopKeyword;
	private List<StatementTree> statements;
	private InternalSyntaxToken endKeyword;
	private InternalSyntaxToken loopKeyword2;
	private LabelTreeImpl label2;
	private InternalSyntaxToken semi;

	public LoopStatementTreeImpl(LabelTreeImpl label, InternalSyntaxToken colon, InternalSyntaxToken loopKeyword,
			List<StatementTree> statements, InternalSyntaxToken endKeyword, InternalSyntaxToken loopKeyword2,
			LabelTreeImpl label2, InternalSyntaxToken semi) {
		super();
		this.label = label;
		this.colon = colon;
		this.loopKeyword = loopKeyword;
		this.statements = statements;
		this.endKeyword = endKeyword;
		this.loopKeyword2 = loopKeyword2;
		this.label2 = label2;
		this.semi = semi;
	}

	public LoopStatementTreeImpl(InternalSyntaxToken loopKeyword, List<StatementTree> statements,
			InternalSyntaxToken endKeyword, InternalSyntaxToken loopKeyword2, InternalSyntaxToken semi) {
		super();
		this.loopKeyword = loopKeyword;
		this.statements = statements;
		this.endKeyword = endKeyword;
		this.loopKeyword2 = loopKeyword2;
		this.semi = semi;
	}

	public LabelTreeImpl label() {
		return label;
	}

	public InternalSyntaxToken colon() {
		return colon;
	}

	public InternalSyntaxToken loopKeyword() {
		return loopKeyword;
	}

	public List<StatementTree> statements() {
		return statements;
	}

	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	public InternalSyntaxToken loopKeyword2() {
		return loopKeyword2;
	}

	public LabelTreeImpl label2() {
		return label2;
	}

	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitLoopStatement(this);
	}

	@Override
	public Kind getKind() {
		return Kind.LOOP_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(label, colon, loopKeyword), statements.iterator(),
				Iterators.forArray(endKeyword, loopKeyword2, semi));
	}

}
