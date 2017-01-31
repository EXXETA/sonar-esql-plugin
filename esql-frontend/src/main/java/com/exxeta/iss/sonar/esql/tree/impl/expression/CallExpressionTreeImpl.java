package com.exxeta.iss.sonar.esql.tree.impl.expression;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.symbols.Type;
import com.exxeta.iss.sonar.esql.api.symbols.TypeSet;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.function.FunctionTree;
import com.exxeta.iss.sonar.esql.api.tree.symbols.type.TypableTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.ParameterListTreeImpl;
import com.google.common.collect.Iterators;

public class CallExpressionTreeImpl extends EsqlTree implements CallExpressionTree, TypableTree{

	private FunctionTree function;
	private FieldReferenceTreeImpl functionName;
	private ParameterListTreeImpl parameters;
	  private TypeSet types = TypeSet.emptyTypeSet();


	public CallExpressionTreeImpl(FunctionTree function) {
		this.function=function; 
	}

	public CallExpressionTreeImpl(FieldReferenceTreeImpl functionName, ParameterListTreeImpl parameters) {
		this.functionName=functionName;
		this.parameters=parameters;
	}

	public CallExpressionTreeImpl(FieldReferenceTreeImpl functionName) {
		this.functionName=functionName;
	}

	@Override
	public FunctionTree function() {
		return function;
	}

	@Override
	public FieldReferenceTreeImpl functionName() {
		return functionName;
	}

	@Override
	public ParameterListTreeImpl parameters() {
		return parameters;
	}

	@Override
	public TypeSet types() {
		return types;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCallExpression(this);
		
	}

	@Override
	public Kind getKind() {
		return Kind.CALL_EXPRESSION;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(function, functionName, parameters);
	}
	
	  @Override
	  public void add(Type type) {
	    types.add(type);
	  }	

}
