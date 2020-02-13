/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.metrics;

import java.util.Set;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitor;
import com.exxeta.iss.sonar.esql.tree.EsqlCommentAnalyser;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class CommentLineVisitor extends SubscriptionVisitor {

	private Set<Integer> comments = Sets.newHashSet();
	private Set<Integer> noSonarLines = Sets.newHashSet();

	// seenFirstToken is required to track header comments (header comments are
	// saved as trivias of first non-trivia token)
	private boolean seenFirstToken;

	private boolean ignoreHeaderComments;
	private EsqlCommentAnalyser commentAnalyser = new EsqlCommentAnalyser();

	public CommentLineVisitor(Tree tree, boolean ignoreHeaderComments) {
		this.ignoreHeaderComments = ignoreHeaderComments;

		this.comments.clear();
		this.noSonarLines.clear();
		this.seenFirstToken = false;
		scanTree(tree);
	}

	@Override
	public Set<Kind> nodesToVisit() {
		return ImmutableSet.of(Tree.Kind.TOKEN);
	}

	@Override
	public void visitNode(Tree tree) {
		for (SyntaxTrivia trivia : ((SyntaxToken) tree).trivias()) {
			if ((ignoreHeaderComments && seenFirstToken) || !ignoreHeaderComments) {
				String[] commentLines = commentAnalyser.getContents(trivia.text()).split("(\r)?\n|\r", -1);
				int lineNumber = trivia.line();
				for (String commentLine : commentLines) {
					if (commentLine.contains("NOSONAR")) {
						noSonarLines.add(lineNumber);
					} else if (!commentAnalyser.isBlank(commentLine)) {
						comments.add(lineNumber);
					}
					lineNumber++;
				}
			} else {
				seenFirstToken = true;
			}
		}
		seenFirstToken = true;
	}

	public Set<Integer> noSonarLines() {
		return noSonarLines;
	}

	public Set<Integer> getCommentLines() {
		return comments;
	}

	public int getCommentLineNumber() {
		return comments.size();
	}
}
