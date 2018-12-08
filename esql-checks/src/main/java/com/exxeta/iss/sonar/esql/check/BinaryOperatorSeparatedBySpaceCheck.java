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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import static com.exxeta.iss.sonar.esql.api.tree.Tree.Kind.INDEX;
/**
 * This Java class is created to implement the logic for all binary operators
 * should be separated from their operands by spaces.
 * 
 * @author C50679
 *
 */
@Rule(key = "BinaryOperatorSeparatedBySpace")
public class BinaryOperatorSeparatedBySpaceCheck extends SubscriptionVisitorCheck {

	private static final String MESSAGE = "This binary operators should be separated from it's operands by spaces.";
	private static final String NO_SPACE_AFTER_MESSAGE = "This token should be followed by a space.";
	private static final List<String> BINARY_OPERATOR = ImmutableList.of("=", ">", "<", "=<", ">=", "<>");
	private static final List<String> SPACE_AFTER = ImmutableList.of(",");

	@Override
	public void visitNode(Tree tree) {
		String token =((InternalSyntaxToken)tree).text();
		if ((BINARY_OPERATOR.contains(token) || SPACE_AFTER.contains(token)) && !tree.parent().is(INDEX)) {
			Iterator<Tree> childIterator = tree.parent().childrenStream().iterator();
			Tree prevChild = null;
			while (childIterator.hasNext()) {

				Tree child = childIterator.next();
				if (child == tree) {
					break;
				}
				prevChild = child;
			}
			Tree nextChild = null;
			if (childIterator.hasNext()) {
				nextChild = childIterator.next();
			}
			boolean noSpaceBefore = prevChild != null
					&& !isSpaceBetween(prevChild.lastToken(), (InternalSyntaxToken) tree);
			boolean noSpaceAfter = nextChild != null
					&& !isSpaceBetween((InternalSyntaxToken) tree, nextChild.firstToken());
			if (BINARY_OPERATOR.contains(token) && (noSpaceAfter || noSpaceBefore)) {
				addIssue(tree, MESSAGE);
			} else if (SPACE_AFTER.contains(token) && noSpaceAfter ) {
				addIssue(tree, NO_SPACE_AFTER_MESSAGE);
			}

		}
		super.visitNode(tree);
	}

	private boolean isSpaceBetween(SyntaxToken firstTree, SyntaxToken secondTree) {

		if (firstTree.endLine() != secondTree.line()) {
			return true;
		}

		return firstTree.endColumn() != secondTree.column();

	}

	@Override
	public Set<Kind> nodesToVisit() {
		return ImmutableSet.of(Tree.Kind.TOKEN);
	}

}
