package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;
import java.util.List;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class CreateModuleStatementTreeImpl extends EsqlTree implements CreateModuleStatementTree {

	private final InternalSyntaxToken createKeyword;
	private final InternalSyntaxToken moduleType;
	private final InternalSyntaxToken moduleKeyword;
	private final InternalSyntaxToken moduleName;
	private final List<StatementTree> moduleStatementsList;
	private final InternalSyntaxToken endKeyword;
	private final InternalSyntaxToken moduleKeyword2;

	public CreateModuleStatementTreeImpl(InternalSyntaxToken createKeyword, InternalSyntaxToken moduleType,
			InternalSyntaxToken moduleKeyword, InternalSyntaxToken moduleName,
			List<StatementTree> moduleStatementsList, InternalSyntaxToken endKeyword,
			InternalSyntaxToken moduleKeyword2) {
		super();
		this.createKeyword = createKeyword;
		this.moduleType = moduleType;
		this.moduleKeyword = moduleKeyword;
		this.moduleName = moduleName;
		this.moduleStatementsList = moduleStatementsList;
		this.endKeyword = endKeyword;
		this.moduleKeyword2 = moduleKeyword2;
	}

	@Override
	public InternalSyntaxToken createKeyword() {
		return createKeyword;
	}

	@Override
	public InternalSyntaxToken moduleType() {
		return moduleType;
	}

	@Override
	public InternalSyntaxToken moduleKeyword() {
		return moduleKeyword;
	}

	@Override
	public InternalSyntaxToken moduleName() {
		return moduleName;
	}

	@Override
	public List<StatementTree> moduleStatementsList() {
		return moduleStatementsList;
	}

	@Override
	public InternalSyntaxToken endKeyword() {
		return endKeyword;
	}

	@Override
	public InternalSyntaxToken moduleKeyword2() {
		return moduleKeyword2;
	}

	@Override
	public Kind getKind() {
		return Kind.CREATE_MODULE_STATEMENT;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.concat(Iterators.forArray(createKeyword, moduleType, moduleKeyword, moduleName),
				Iterators.forArray(moduleStatementsList.toArray(new StatementTree[moduleStatementsList.size()])),
				Iterators.forArray(endKeyword, moduleKeyword2));
	}

	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitCreateModuleStatement(this);
	}

}
