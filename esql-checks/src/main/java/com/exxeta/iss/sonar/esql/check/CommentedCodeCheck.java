/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.parser.EsqlParserBuilder;
import com.exxeta.iss.sonar.esql.tree.EsqlCommentAnalyser;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.typed.ActionParser;

@Rule(key = "CommentedCode")
public class CommentedCodeCheck extends SubscriptionVisitorCheck {

	private static final String MESSAGE = "Remove this commented out code.";
	private static final EsqlCommentAnalyser COMMENT_ANALYSER = new EsqlCommentAnalyser();
	private static final ActionParser<Tree> PARSER = EsqlParserBuilder.createParser(Kind.STATEMENTS);

	private static final List<String> PARSING_TRY = ImmutableList.of(
			"%s",
			"IF TRUE THEN %s", 
			"IF TRUE THEN %s END IF;",
			"%s END IF;",
			"CASE A %s END CASE;", 
			"BEGIN %s", 
			"%s END;",
			"%s;");
	
	@Override
	public Set<Kind> nodesToVisit() {
		return ImmutableSet.of(Kind.TOKEN);
	}

	@Override
	public void visitNode(Tree tree) {
		List<List<SyntaxTrivia>> commentGroups = groupComments((SyntaxToken) tree);
		commentGroups.forEach(this::checkCommentGroup);
	}

	private void checkCommentGroup(List<SyntaxTrivia> commentGroup) {
		String uncommentedText = uncomment(commentGroup);

		boolean parseable=false;
		for (String format : PARSING_TRY){
			if (isParseable(String.format(format, uncommentedText))){
				parseable=true;
				break;
			}
		}
		
		if (parseable) {
			IssueLocation primaryLocation = new IssueLocation(commentGroup.get(0),
					commentGroup.get(commentGroup.size() - 1), MESSAGE);
			addIssue(new PreciseIssue(this, primaryLocation));
		}
	}

	private boolean isParseable(String string) {
		try{
			StatementsTree parsedTree = (StatementsTree) PARSER.parse(string);
			return !parsedTree.statements().isEmpty();
		}catch (Exception e){ //NOSONAR
			return false;
		}

	}

	private static String uncomment(List<SyntaxTrivia> triviaGroup) {
		StringBuilder uncommentedText = new StringBuilder();
		for (SyntaxTrivia trivia : triviaGroup) {
			String value = COMMENT_ANALYSER.getContents(trivia.text());
			uncommentedText.append("\n");
			uncommentedText.append(value);
		}
		return uncommentedText.toString().trim();
	}

	/**
	 * Returns comments by groups which come sequentially, without empty lines
	 * between.
	 */
	private static List<List<SyntaxTrivia>> groupComments(SyntaxToken token) {
		List<List<SyntaxTrivia>> groups = new LinkedList<>();
		List<SyntaxTrivia> currentGroup = null;

		for (SyntaxTrivia trivia : token.trivias()) {
			if (currentGroup == null) {
				currentGroup = initNewGroup(trivia);

			} else if (isAdjacent(trivia, currentGroup)) {
				currentGroup.add(trivia);

			} else {
				groups.add(currentGroup);
				currentGroup = initNewGroup(trivia);
			}
		}

		if (currentGroup != null) {
			groups.add(currentGroup);
		}
		return groups;
	}

	private static List<SyntaxTrivia> initNewGroup(SyntaxTrivia trivia) {
		List<SyntaxTrivia> group = new LinkedList<>();
		group.add(trivia);
		return group;
	}

	private static boolean isAdjacent(SyntaxTrivia trivia, List<SyntaxTrivia> currentGroup) {
		return currentGroup.get(currentGroup.size() - 1).line() + 1 == trivia.line();
	}
}
