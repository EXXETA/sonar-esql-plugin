package com.exxeta.iss.sonar.esql.check;

import java.util.List;
import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.lexer.EsqlReservedKeyword;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

@Rule(key = UpperCaseKeywordCheck.CHECK_KEY)
public class UpperCaseKeywordCheck extends SubscriptionVisitorCheck {
	public static final String CHECK_KEY = "UpperCaseKeyword";
	private static final Set<String> nonReservedKeywords = ImmutableSet.copyOf(EsqlNonReservedKeyword.keywordValues());
	private static final Set<String> reservedKeywords = ImmutableSet.copyOf(EsqlReservedKeyword.keywordValues());
	public static final String message = "ESQL keyword \"%s\" should be uppercase, although it is not case sensitive.";

	/*@Override
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
	}*/
	
	


	  @Override
	public void visitNode(Tree tree) {
		super.visitNode(tree);
		if (tree instanceof SyntaxToken && !((InternalSyntaxToken)tree).getParent().is(Kind.FIELD_REFERENCE)){
			String token = ((SyntaxToken)tree).text();
			if (nonReservedKeywords.contains(token.toUpperCase()) || reservedKeywords.contains(token.toUpperCase())){
				if (!token.toUpperCase().equals(token)){
					addIssue(new PreciseIssue(this,
							new IssueLocation(tree, String.format(message, token))));
				}
			}
		}
	}




	@Override
	  public List<Tree.Kind> nodesToVisit() {
	    return ImmutableList.of(Tree.Kind.TOKEN);
	  }
}
