package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.LeaveStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class LeaveStatementTreeImpl extends EsqlTree implements LeaveStatementTree{

	private final  InternalSyntaxToken leaveKeyword;
	private final LabelTreeImpl label;
	private final InternalSyntaxToken semi;
	public LeaveStatementTreeImpl(InternalSyntaxToken LeaveKeyword, LabelTreeImpl label, InternalSyntaxToken semi) {
		super();
		this.leaveKeyword = LeaveKeyword;
		this.label = label;
		this.semi = semi;
	}
	public InternalSyntaxToken leaveKeyword() {
		return leaveKeyword;
	}
	public LabelTreeImpl label() {
		return label;
	}
	public InternalSyntaxToken semi() {
		return semi;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitLeaveStatement(this);
		
	}
	@Override
	public Kind getKind() {
		return Kind.LEAVE_STATEMENT;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(leaveKeyword, label, semi);
	}
	
	
	
	
	

}
