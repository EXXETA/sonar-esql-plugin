package com.exxeta.iss.sonar.esql.check;

import java.util.Iterator;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.SyntacticEquivalence;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.utils.LiteralUtils;

@Rule(key = "HardcodedURI")

public class HardcodedURICheck extends DoubleDispatchVisitorCheck {
	public static final String SCHEME = "[a-zA-Z][a-zA-Z\\+\\.\\-]+";
	public static final String FOLDER_NAME = "[^/?%*:\\|\"<>]+";
	public static final String URI_REGEX = String.format("^%s://.+", SCHEME);
	public static final String LOCAL_URI = String.format("^(~/|/|//[\\w-]+/|%s:/)(%s/)*%s/?", SCHEME, FOLDER_NAME,
			FOLDER_NAME);
	public static final String BACKSLASH_LOCAL_URI = String.format(
			"^(~\\\\|\\\\\\\\[\\w-]+\\\\|%s:\\\\)(%s\\\\)*%s(\\\\)?", SCHEME, FOLDER_NAME,
			FOLDER_NAME);
	public static final String DISK_URI = "^[A-Za-z]:(/|\\\\)";//Only one backslash?

	public static final Pattern URI_PATTERN = Pattern
			.compile(URI_REGEX + "|" + LOCAL_URI + "|" + DISK_URI + "|" + BACKSLASH_LOCAL_URI);

	public static final Pattern VARIABLE_NAME_PATTERN = Pattern.compile("filename|path", Pattern.CASE_INSENSITIVE);
	public static final Pattern PATH_DELIMETERS_PATTERN = Pattern
			.compile("'/'|'//'|'\\\\'|'\\\\\\\\'");

	@Override
	public void visitSetStatement(SetStatementTree tree) {
		if (isFileNameFieldReference(tree.fieldReference())) {
			checkExpression(tree.expression());
		}
		super.visitSetStatement(tree);
	}

	private boolean isFileNameFieldReference(FieldReferenceTreeImpl fieldReference) {
		if (VARIABLE_NAME_PATTERN.matcher(fieldReference.pathElement().name().name().text()).find()) {
			return true;
		}
		Iterator<PathElementTree> iter = fieldReference.pathElements().iterator();
		while (iter.hasNext()) {
			PathElementTree pathElement = iter.next();
			if (VARIABLE_NAME_PATTERN.matcher(pathElement.name().name().text()).find()) {
				return true;
			}
		}
		return false;
	}

	private void checkExpression(@Nullable ExpressionTree expr) {
		if (expr != null) {
			if (isHardcodedURI(expr)) {
				reportHardcodedURI(expr);
			} else {
				reportStringConcatenationWithPathDelimiter(expr);
			}
		}
	}

	private static boolean isHardcodedURI(ExpressionTree expr) {
		ExpressionTree newExpr = SyntacticEquivalence.skipParentheses(expr);
		if (!newExpr.is(Tree.Kind.STRING_LITERAL)) {
			return false;
		}
		String stringLiteral = LiteralUtils.trimQuotes(((LiteralTree) newExpr).value());
		return URI_PATTERN.matcher(stringLiteral).find();
	}

	private void reportHardcodedURI(ExpressionTree hardcodedURI) {
		addIssue(hardcodedURI, "Refactor your code to get this URI from a customizable parameter.");
	}

	private void reportStringConcatenationWithPathDelimiter(ExpressionTree expr) {
		expr.accept(new StringConcatenationVisitor());
	}

	private class StringConcatenationVisitor extends DoubleDispatchVisitor {
		@Override
		public void visitBinaryExpression(BinaryExpressionTree tree) {
			if (tree.is(Tree.Kind.CONCAT)) {
				checkPathDelimiter(tree.leftOperand());
				checkPathDelimiter(tree.rightOperand());
			}
			super.visitBinaryExpression(tree);
		}

		private void checkPathDelimiter(ExpressionTree expr) {
			ExpressionTree newExpr = SyntacticEquivalence.skipParentheses(expr);
			if (newExpr.is(Tree.Kind.STRING_LITERAL)
					&& PATH_DELIMETERS_PATTERN.matcher(((LiteralTree) newExpr).value()).find()) {
				addIssue(newExpr, "Remove this hard-coded path-delimiter.");
			}
		}
	}

}
