package com.exxeta.iss.sonar.esql.check;

import java.util.List;
import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.PathElementNameTree;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.lexer.EsqlReservedKeyword;
import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * This Java Class Is created to ensure that all the keywords in esql file
 * should be in UPPER CASE
 * 
 * @author sapna singh
 *
 */

@Rule(key = "KeyWordCaseCheck")
public class KeyWordCaseCheck extends SubscriptionVisitorCheck {
	private static final Set<String> nonReservedKeywords = ImmutableSet.copyOf(EsqlNonReservedKeyword.keywordValues());
	private static final Set<String> reservedKeywords = ImmutableSet.copyOf(EsqlReservedKeyword.keywordValues());
	private static final String MESSAGE = "This keyword should be in uppercase.";

	@Override
	public List<Kind> nodesToVisit() {
		return ImmutableList.of(Tree.Kind.TOKEN);
	}

	@Override
	public void visitNode(Tree tree) {
		String value = ((InternalSyntaxToken) tree).text();
		String upperCase = value.toUpperCase();
		if (!value.equals(upperCase)
				&& (reservedKeywords.contains(upperCase) || nonReservedKeywords.contains(upperCase))
				&& !(tree.parent() instanceof PathElementNameTree)) {
			addIssue(tree, MESSAGE);
		}
		super.visitNode(tree);
	}
}
