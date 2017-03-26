package com.exxeta.iss.sonar.esql.check;

import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableSet;

@Rule(key = NonReservedKeywordCheck.CHECK_KEY)
public class NonReservedKeywordCheck extends DoubleDispatchVisitorCheck {
	public static final String CHECK_KEY = "NonReservedKeyword";
	private static final Set<String> nonReservedKeywords = ImmutableSet.copyOf(EsqlNonReservedKeyword.keywordValues());
	public static final String MESSAGE = "ESQL keyword \"%s\" should not be used as an identifier.";

	@Override
	public void visitDeclareStatement(DeclareStatementTree tree) {
		for (int i=0;i<tree.nameList().size();i++){
			InternalSyntaxToken variableName = tree.nameList().get(i);
			if (nonReservedKeywords.contains(variableName.text())){
				addIssue(variableName, String.format(MESSAGE, variableName.text()));
			}
		}
		super.visitDeclareStatement(tree);
		
	}

}
