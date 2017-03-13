package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

public class CreateStatementTreeImpl extends EsqlTree implements CreateStatementTree {

	private InternalSyntaxToken createKeyword;
	private InternalSyntaxToken qualifierName;
	private InternalSyntaxToken qualifierOfKeyword;
	private FieldReferenceTreeImpl target;
	
	private InternalSyntaxToken asKeyword;
	private FieldReferenceTreeImpl aliasFieldReference;

	private InternalSyntaxToken domainKeyword;
	private ExpressionTree domainExpression;

	private RepeatClauseTreeImpl repeatClause;
	private ValuesClauseTreeImpl valuesClause;
	private FromClauseTreeImpl fromClause;
	private ParseClauseTreeImpl parseClause;
	private InternalSyntaxToken semi;

	public CreateStatementTreeImpl(InternalSyntaxToken createKeyword, InternalSyntaxToken qualifierName,
			InternalSyntaxToken qualifierOfKeyword, FieldReferenceTreeImpl target, InternalSyntaxToken asKeyword,
			FieldReferenceTreeImpl aliasFieldReference, InternalSyntaxToken domainKeyword,
			ExpressionTree domainExpression, RepeatClauseTreeImpl repeatClause, ValuesClauseTreeImpl valuesClause,
			FromClauseTreeImpl fromClause, ParseClauseTreeImpl parseClause, InternalSyntaxToken semi) {
		super();
		this.createKeyword = createKeyword;
		this.qualifierName = qualifierName;
		this.qualifierOfKeyword = qualifierOfKeyword;
		this.target = target;
		this.asKeyword = asKeyword;
		this.aliasFieldReference = aliasFieldReference;
		this.domainKeyword = domainKeyword;
		this.domainExpression = domainExpression;
		this.repeatClause = repeatClause;
		this.valuesClause = valuesClause;
		this.fromClause = fromClause;
		this.parseClause = parseClause;
		this.semi = semi;
	}

	public InternalSyntaxToken createKeyword() {
		return createKeyword;
	}

	public InternalSyntaxToken qualifierName() {
		return qualifierName;
	}

	public InternalSyntaxToken qualifierOfKeyword() {
		return qualifierOfKeyword;
	}
	
	public FieldReferenceTreeImpl target() {
		return target;
	}

	public InternalSyntaxToken asKeyword() {
		return asKeyword;
	}

	public FieldReferenceTreeImpl aliasFieldReference() {
		return aliasFieldReference;
	}

	public InternalSyntaxToken domainKeyword() {
		return domainKeyword;
	}

	public ExpressionTree domainExpression() {
		return domainExpression;
	}

	public RepeatClauseTreeImpl repeatClause() {
		return repeatClause;
	}

	public ValuesClauseTreeImpl valuesClause() {
		return valuesClause;
	}

	public FromClauseTreeImpl fromClause() {
		return fromClause;
	}

	public ParseClauseTreeImpl parseClause() {
		return parseClause;
	}

	public InternalSyntaxToken semi() {
		return semi;
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCreateFunctionStatement(this);

	}

	@Override
	public Kind getKind() {
		return Kind.CREATE_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(createKeyword, qualifierName, qualifierOfKeyword, target, asKeyword, aliasFieldReference,
				domainKeyword, domainExpression, repeatClause, valuesClause, fromClause, parseClause, semi);
	}

}
