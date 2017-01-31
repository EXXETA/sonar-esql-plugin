package com.exxeta.iss.sonar.esql.check;

import java.util.Set;

import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.NoSqale;
import org.sonar.squidbridge.checks.SquidCheck;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.lexer.EsqlReservedKeyword;
import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Grammar;

@Rule(key = UpperCaseKeywordCheck.CHECK_KEY, priority = Priority.MINOR, name = "ESQL keywords should be uppercase", tags = Tags.CONVENTION)
@NoSqale
@BelongsToProfile(title=CheckList.SONAR_WAY_PROFILE,priority=Priority.MINOR)
public class UpperCaseKeywordCheck extends SquidCheck<Grammar> {
	public static final String CHECK_KEY = "UpperCaseKeyword";
	private static final Set<String> nonResewrvedKeywords = ImmutableSet.copyOf(EsqlNonReservedKeyword.keywordValues());
	@Override
	public void init() {
		subscribeTo(GenericTokenType.IDENTIFIER);
		subscribeTo(EsqlReservedKeyword.values());
	}

	@Override
	public void visitNode(AstNode astNode) {
		boolean check = false;
		if (astNode.is(EsqlReservedKeyword.values())){
			check=true;
		}else if (!astNode.getParent().is(EsqlGrammar.NAME,EsqlGrammar.FIELD_NAME,EsqlGrammar.primaryExpression) && nonResewrvedKeywords.contains(astNode.getTokenValue())){
			check = true;
		}
		
		if (check && !astNode.getTokenOriginalValue().equals(astNode.getTokenOriginalValue().toUpperCase())) {
				String message = "ESQL keyword \"{0}\" should be uppercase, although it is not case sensitive.";
				getContext().createLineViolation(this, message, astNode, astNode.getTokenOriginalValue());
		}
	}

}
