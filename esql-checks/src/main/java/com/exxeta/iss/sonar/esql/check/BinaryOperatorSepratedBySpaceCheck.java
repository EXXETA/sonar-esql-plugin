/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.Iterator;
import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;
import com.google.common.collect.ImmutableList;

/**
 * This Java class is created to implement the logic for all binary operators
 * should be separated from their operands by spaces.
 * 
 * @author C50679
 *
 */
@Rule(key = "BinaryOperatorSepratedBySpace")
public class BinaryOperatorSepratedBySpaceCheck extends SubscriptionVisitorCheck {

	private static final String MESSAGE = "All binary operators should be separated from their operands by spaces.";
	private static final List<String> BINARY_OPERATOR = ImmutableList.of("=", ">", "<", "=<", ">=", "<>");

	@Override
	public void visitNode(Tree tree) {
		if (BINARY_OPERATOR.contains(((InternalSyntaxToken) tree).text())) {
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
			if (noSpaceAfter || noSpaceBefore) {
				addIssue(tree, MESSAGE);
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
	public List<Kind> nodesToVisit() {
		return ImmutableList.of(Tree.Kind.TOKEN);
	}

}
