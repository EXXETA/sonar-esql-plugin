package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class ElseifClauseTreeImpl extends EsqlTree implements ElseifClauseTree {

	  private InternalSyntaxToken elseifKeyword;
	  private ExpressionTree expression;
	  private InternalSyntaxToken thenKeyword;
	  private final List<StatementTree> statements;

	  public ElseifClauseTreeImpl(InternalSyntaxToken elseifKeyword, ExpressionTree expression, InternalSyntaxToken thenToken, List<StatementTree> statements) {
		    this.elseifKeyword = elseifKeyword;
		    this.expression=expression;
		    this.thenKeyword=thenToken;
		    this.statements = statements;

		  }

	  public ElseifClauseTreeImpl(InternalSyntaxToken elseifKeyword) {
		    this.elseifKeyword = elseifKeyword;
		    this.statements = Collections.emptyList();

		  }

	  @Override
	  public SyntaxToken elseifKeyword() {
	    return elseifKeyword;
	  }
	  
	  @Override
	  public ExpressionTree expression() {
		return expression;
	}
	  
	  @Override
	  public InternalSyntaxToken thenKeyword() {
		return thenKeyword;
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
		  
		  return Iterators.<Tree>concat(
			      Iterators.forArray(elseifKeyword, expression, thenKeyword),
			      statements.iterator());
	  }

	  @Override
	  public void accept(DoubleDispatchVisitor visitor) {
	    visitor.visitElseifClause(this);
	  }
	}
