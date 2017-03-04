package com.exxeta.iss.sonar.esql.check;

import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.google.common.collect.ImmutableSet;

@Rule(key = NonReservedKeywordCheck.CHECK_KEY)
public class NonReservedKeywordCheck extends DoubleDispatchVisitorCheck {
	public static final String CHECK_KEY = "NonReservedKeyword";
	private static final Set<String> nonResewrvedKeywords = ImmutableSet.copyOf(EsqlNonReservedKeyword.keywordValues());
	@Override
	public void visitIdentifier(IdentifierTree tree) {
			if (nonResewrvedKeywords.contains(tree.name().toUpperCase())){
				super.addIssue(tree, "ESQL keyword \""+tree.name()+"\" should not be used as an identifier.");
			}
		super.visitIdentifier(tree);
	}

}
