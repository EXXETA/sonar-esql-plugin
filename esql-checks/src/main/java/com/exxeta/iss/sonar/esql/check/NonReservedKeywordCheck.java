package com.exxeta.iss.sonar.esql.check;

import java.util.Set;

import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.NoSqale;
import org.sonar.squidbridge.checks.SquidCheck;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

@Rule(key = NonReservedKeywordCheck.CHECK_KEY, priority = Priority.MAJOR, name = "ESQL keywords should not be identifiers", tags = Tags.CONVENTION)
@NoSqale
@BelongsToProfile(title=CheckList.SONAR_WAY_PROFILE,priority=Priority.MAJOR)
public class NonReservedKeywordCheck extends SquidCheck<Grammar> {
	public static final String CHECK_KEY = "NonReservedKeyword";
	private static final Set<String> nonResewrvedKeywords = ImmutableSet.copyOf(EsqlNonReservedKeyword.keywordValues());
	@Override
	public void init() {
		subscribeTo(EsqlGrammar.NAME);
	}

	@Override
	public void visitNode(AstNode astNode) {
		if (nonResewrvedKeywords.contains(astNode.getTokenValue())){
				String message = "ESQL keyword \"{0}\" should not be used as an identifier.";
				getContext().createLineViolation(this, message, astNode, astNode.getTokenOriginalValue());
		}
	}

}
