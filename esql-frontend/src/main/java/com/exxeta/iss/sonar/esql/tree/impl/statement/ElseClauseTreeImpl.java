package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ElseClauseTreeImpl extends EsqlTree implements ElseClauseTree {

	  private SyntaxToken elseKeyword;
	  private final List<StatementTree> statements;

	  public ElseClauseTreeImpl(InternalSyntaxToken elseKeyword, List<StatementTree> statements) {
	    this.elseKeyword = elseKeyword;
	    this.statements = statements;

	  }

	  @Override
	  public SyntaxToken elseKeyword() {
	    return elseKeyword;
	  }

	  @Override
	  public List<StatementTree> statements() {
	    return statements;
	  }

	  @Override
	  public Kind getKind() {
	    return Kind.ELSE_CLAUSE;
	  }

	  @Override
	  public Iterator<Tree> childrenIterator() {
	    return Iterators.<Tree>concat(Iterators.singletonIterator(elseKeyword), statements.iterator());
	  }

	  @Override
	  public void accept(DoubleDispatchVisitor visitor) {
	    visitor.visitElseClause(this);
	  }
	}
