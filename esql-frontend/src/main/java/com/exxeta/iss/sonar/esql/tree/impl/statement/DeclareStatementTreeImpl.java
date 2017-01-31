package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ElseifClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.IfStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.Iterators;

public class DeclareStatementTreeImpl  extends EsqlTree implements DeclareStatementTree {

	private final SyntaxToken ifKeyword;
	private final ExpressionTree condition;
	private final SyntaxToken thenKeyword;
	private final List<StatementTree> statements;
	private final List<ElseifClauseTree> elseifClauses;
	private final ElseClauseTree elseClause;
	private final SyntaxToken endKeyword;
	private final SyntaxToken ifKeyword2;

	
	  public DeclareStatementTreeImpl(SyntaxToken ifKeyword, ExpressionTree condition, SyntaxToken thenKeyword,
			List<StatementTree> statements, List<ElseifClauseTree> elseifClauses, ElseClauseTree elseClause,
			SyntaxToken endKeyword, SyntaxToken ifKeyword2) {
		super();
		this.ifKeyword = ifKeyword;
		this.condition = condition;
		this.thenKeyword = thenKeyword;
		this.statements = statements==null?Collections.emptyList():statements;
		this.elseifClauses = elseifClauses==null?Collections.emptyList():elseifClauses;
		this.elseClause = elseClause;
		this.endKeyword = endKeyword;
		this.ifKeyword2 = ifKeyword2;
	}




	@Override
	  public SyntaxToken ifKeyword() {
	    return ifKeyword;
	  }

	
	  @Override
	  public ExpressionTree condition() {
	    return condition;
	  }

	  @Override
	  public SyntaxToken thenToken() {
		  return thenKeyword;
	  }
	
	  @Override
	  public List<StatementTree> statements() {
	    return statements;
	  }

	  @Override
	  public List<ElseifClauseTree> elseifClauses() {
	    return elseifClauses;
	  }

	  @Nullable
	  @Override
	  public ElseClauseTree elseClause() {
	    return elseClause;
	  }
	  
	  @Override
	  public SyntaxToken endKeyword(){
		  return endKeyword;
	  }

	  @Override
	  public SyntaxToken ifKeyword2(){
		  return ifKeyword2;
	  }

	  public boolean hasElse() {
	    return elseClause != null;
	  }

	  @Override
	  public Kind getKind() {
	    return Kind.IF_STATEMENT;
	  }

	  @Override
	  public Iterator<Tree> childrenIterator() {
		  return Iterators.<Tree>concat(
				  Iterators.forArray(ifKeyword,condition,thenKeyword),
			      statements.iterator(),
			      elseifClauses.iterator(),
			      Iterators.forArray(elseClause,endKeyword,ifKeyword2)
			      );
	  }

	  @Override
	  public void accept(DoubleDispatchVisitor visitor) {
	    visitor.visitDeclareStatement(this);
	  }

}
