/**
 * 
 */
package com.exxeta.iss.sonar.esql.check;

import java.util.Iterator;
import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
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
			for (Tree child = childIterator.next(); childIterator.hasNext(); child = childIterator.next()) {
				if (child == tree) {
					break;
				}
				prevChild = child;
			}
			Tree nextChild = null;
			if (childIterator.hasNext()) {
				nextChild = childIterator.next();
			}
			boolean noSpaceBefore = false;
			boolean noSpaceAfter = false;
			if (prevChild!=null && prevChild.lastToken().endLine() == ((InternalSyntaxToken) tree).endLine()) {
				if (prevChild.lastToken().endColumn() == ((InternalSyntaxToken) tree).column()){
					noSpaceBefore=true;
				}
			}
			if (nextChild!=null && ((InternalSyntaxToken) tree).endLine() == nextChild.firstToken().line()) {
				if (((InternalSyntaxToken) tree).endColumn()==nextChild.firstToken().column()){
					noSpaceAfter = true;
				}
			}
			if (noSpaceAfter || noSpaceBefore){
				addIssue(tree, MESSAGE);
			}
		}
		super.visitNode(tree);
	}

	@Override
	public List<Kind> nodesToVisit() {
		return ImmutableList.of(Tree.Kind.TOKEN);
	}

}
