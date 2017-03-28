package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.DataTypeTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;

public class DeclareStatementTreeImpl  extends EsqlTree implements DeclareStatementTree {

	private InternalSyntaxToken declareToken;
	private SeparatedList<InternalSyntaxToken> nameList;
	private InternalSyntaxToken sharedExt;
	private InternalSyntaxToken namesapce;
	private InternalSyntaxToken constantKeyword;
	private DataTypeTreeImpl dataType;
	private ExpressionTree initialValueExpression;
	private InternalSyntaxToken semi;


	public DeclareStatementTreeImpl(InternalSyntaxToken declareToken, SeparatedList<InternalSyntaxToken> nameList,
			InternalSyntaxToken sharedExt, InternalSyntaxToken namesapce, ExpressionTree initialValueExpression,
			InternalSyntaxToken semi) {
		super();
		this.declareToken = declareToken;
		this.nameList = nameList;
		this.sharedExt = sharedExt;
		this.namesapce = namesapce;
		this.initialValueExpression = initialValueExpression;
		this.semi = semi;
	}
	
	

	public DeclareStatementTreeImpl(InternalSyntaxToken declareToken, SeparatedList<InternalSyntaxToken> nameList,
			InternalSyntaxToken sharedExt, InternalSyntaxToken constantKeyword, DataTypeTreeImpl dataType,
			ExpressionTree initialValueExpression, InternalSyntaxToken semi) {
		super();
		this.declareToken = declareToken;
		this.nameList = nameList;
		this.sharedExt = sharedExt;
		this.constantKeyword = constantKeyword;
		this.dataType = dataType;
		this.initialValueExpression = initialValueExpression;
		this.semi = semi;
	}



	@Override
	public InternalSyntaxToken declareToken() {
		return declareToken;
	}



	@Override
	public SeparatedList<InternalSyntaxToken> nameList() {
		return nameList;
	}



	@Override
	public InternalSyntaxToken sharedExt() {
		return sharedExt;
	}



	@Override
	public InternalSyntaxToken namesapce() {
		return namesapce;
	}



	@Override
	public InternalSyntaxToken constantKeyword() {
		return constantKeyword;
	}



	@Override
	public DataTypeTreeImpl dataType() {
		return dataType;
	}



	@Override
	public ExpressionTree initialValueExpression() {
		return initialValueExpression;
	}



	@Override
	public InternalSyntaxToken semi() {
		return semi;
	}



	@Override
	  public Kind getKind() {
	    return Kind.DECLARE_STATEMENT;
	  }

	  @Override
	  public Iterator<Tree> childrenIterator() {
		  return Iterators.concat(
				  	Iterators.singletonIterator(declareToken),
				  	nameList.elementsAndSeparators(Functions.<InternalSyntaxToken>identity()),
				  	Iterators.forArray(sharedExt, namesapce, constantKeyword, dataType, initialValueExpression, semi)
				  );
	  }

	  @Override
	  public void accept(DoubleDispatchVisitor visitor) {
	    visitor.visitDeclareStatement(this);
	  }

}
