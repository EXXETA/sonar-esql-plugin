package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CaseStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.parser.TreeFactory.Tuple;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Optional;

public class CaseStatementTreeImpl extends EsqlTree implements CaseStatementTree {

	private final InternalSyntaxToken caseKeyword;
	private final ExpressionTree mainExpression;
	private final List<WhenClauseTreeImpl> whenClauses;
	private final InternalSyntaxToken elseKeyword;
	private final List<StatementTree> elseSatements;
	private final InternalSyntaxToken endKeyword;
	private final InternalSyntaxToken caseKeyword2;
	private final InternalSyntaxToken semi;

	public CaseStatementTreeImpl(InternalSyntaxToken caseKeyword, ExpressionTree mainExpression,
			List<WhenClauseTreeImpl> whenClauses, InternalSyntaxToken elseKeyword, List<StatementTree> elseStatements,
			InternalSyntaxToken endKeyword, InternalSyntaxToken caseKeyword2, InternalSyntaxToken semi) {
		super();
		this.caseKeyword = caseKeyword;
		this.mainExpression = mainExpression;
		this.whenClauses = whenClauses;
		this.elseKeyword = elseKeyword;
		this.elseSatements = elseStatements;
		this.endKeyword = endKeyword;
		this.caseKeyword2 = caseKeyword2;
		this.semi = semi;
	}

	public InternalSyntaxToken caseKeyword() {
		return caseKeyword;
	}

	public ExpressionTree mainExpression() {
		return mainExpression;
	}

	public List<WhenClauseTreeImpl> whenClauses() {
		return whenClauses;
	}

	public InternalSyntaxToken elseKeyword() {
		return elseKeyword;
	}

	public List<StatementTree> elseSatements() {
		return elseSatements;
	}

	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	public InternalSyntaxToken caseKeyword2() {
		return caseKeyword2;
	}

	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCaseStatement(this);

	}

	@Override
	public Kind getKind() {
		return Kind.CASE_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(caseKeyword, mainExpression), whenClauses.iterator(),
				Iterators.singletonIterator(elseKeyword), elseSatements.iterator(),
				Iterators.forArray(endKeyword, caseKeyword2, semi));
	}

}
