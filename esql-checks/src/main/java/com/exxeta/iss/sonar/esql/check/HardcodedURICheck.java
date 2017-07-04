/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.BinaryExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.VariableReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.SyntacticEquivalence;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
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
		if (isFileNameFieldReference(tree.variableReference()) ) {
			checkExpression(tree.expression());
		}
		super.visitSetStatement(tree);
	}

	private boolean isFileNameFieldReference(VariableReferenceTree variableReference) {
		if (variableReference instanceof FieldReferenceTree){
			return isFileNameFieldReference((FieldReferenceTree)variableReference);
		} else {
			return isFileNameFieldReference((IdentifierTree)variableReference);
		}
	}

	private boolean isFileNameFieldReference(IdentifierTree identifier) {
		if (identifier==null){
			return false;
		}
		return VARIABLE_NAME_PATTERN.matcher(identifier.name()).find();
	}

	private boolean isFileNameFieldReference(FieldReferenceTree fieldReference) {
		if (fieldReference==null){
			return false;
		}
		if (VARIABLE_NAME_PATTERN.matcher(fieldReference.pathElement().name().name().text()).find()) {
			return true;
		}
		Iterator<PathElementTree> iter = fieldReference.pathElements().iterator();
		while (iter.hasNext()) {
			PathElementTree pathElement = iter.next();
			if (pathElement.name()!=null && VARIABLE_NAME_PATTERN.matcher(pathElement.name().name().text()).find()) {
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
