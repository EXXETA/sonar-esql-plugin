package com.exxeta.iss.sonar.esql.tree.impl.declaration;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.PathClauseTree;
import com.exxeta.iss.sonar.esql.api.tree.SchemaNameTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.base.Functions;
import com.google.common.collect.Iterators;

public class SchemaNameTreeImpl  extends EsqlTree implements SchemaNameTree {
	private final SeparatedList<InternalSyntaxToken> schemaElements;
	
	public SchemaNameTreeImpl( SeparatedList<InternalSyntaxToken> schemaElements) {
		this.schemaElements=schemaElements;
	}
	
	@Override
	public SeparatedList<InternalSyntaxToken> schemaElements() {
		return schemaElements;
	}

	@Override
	public Kind getKind() {
		return Kind.PATH_CLAUSE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return schemaElements.elementsAndSeparators(Functions.<InternalSyntaxToken>identity());
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitSchemaName(this);
	}


}
