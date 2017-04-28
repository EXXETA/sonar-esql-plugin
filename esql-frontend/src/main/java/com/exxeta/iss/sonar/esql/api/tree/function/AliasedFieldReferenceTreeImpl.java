package com.exxeta.iss.sonar.esql.api.tree.function;

import java.util.Iterator;

import org.sonar.api.internal.google.common.collect.Iterators;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.parser.TreeFactory.Tuple;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.function.AliasedFieldReferenceTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.sonar.sslr.api.typed.Optional;

public class AliasedFieldReferenceTreeImpl extends EsqlTree implements AliasedFieldReferenceTree{
	private FieldReferenceTreeImpl fieldRefernce;
	private InternalSyntaxToken asKeyword;
	private InternalSyntaxToken alias;
	public AliasedFieldReferenceTreeImpl(FieldReferenceTreeImpl fieldRefernce, InternalSyntaxToken asKeyword,
			InternalSyntaxToken alias) {
		super();
		this.fieldRefernce = fieldRefernce;
		this.asKeyword = asKeyword;
		this.alias = alias;
	}
	@Override
	public FieldReferenceTreeImpl fieldRefernce() {
		return fieldRefernce;
	}
	@Override
	public InternalSyntaxToken asKeyword() {
		return asKeyword;
	}
	@Override
	public InternalSyntaxToken alias() {
		return alias;
	}
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitAliasedFieldReference(this);
	}
	@Override
	public Kind getKind() {
		return Kind.ALIASED_FIELD_REFERENCE;
	}
	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(fieldRefernce, asKeyword, alias);
	}
	
	
	
	
}
