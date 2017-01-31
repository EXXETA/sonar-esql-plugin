package com.exxeta.iss.sonar.esql.tree.impl.statement;

import java.util.Iterator;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.LanguageTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.Iterators;

public class LanguageTreeImpl extends EsqlTree implements LanguageTree{

	
	private InternalSyntaxToken languageKeyword;
	private InternalSyntaxToken languageName;

	public LanguageTreeImpl(InternalSyntaxToken languageKeyword, InternalSyntaxToken languageName){
		this.languageKeyword=languageKeyword;
		this.languageName=languageName;
	}
	
	public InternalSyntaxToken languageKeyword() {
		return languageKeyword;
	}
	
	public InternalSyntaxToken languageName() {
		return languageName;
	}
	
	@Override
	public void accept(DoubleDispatchVisitor visitor) {
		visitor.visitLanguage(this);

	}

	@Override
	public Kind getKind() {
		return Kind.LANGUAGE;
	}

	@Override
	public Iterator<Tree> childrenIterator() {
		return Iterators.forArray(languageKeyword, languageName);
	}


}
