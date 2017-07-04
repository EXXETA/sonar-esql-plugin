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
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.IdentifierTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.VariableReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;

@Rule(key = "HardCodedCredentials")
public class HardCodedCredentialsCheck extends DoubleDispatchVisitorCheck {

	private static final String PWD_TRANSLATION = "password|passwd|pwd|achinsinsi|adgangskode|codice|contrasena|contrasenya|contrasinal|cynfrinair|facal-faire|facalfaire|"
			+ "fjaleklaim|focalfaire|geslo|haslo|heslo|iphasiwedi|jelszo|kalmarsirri|katalaluan|katasandi|kennwort|kode|kupuhipa|loluszais|losen|losenord|lozinka|"
			+ "lykilorth|mathkau|modpas|motdepasse|olelohuna|oroigbaniwole|parol|parola|parole|parool|pasahitza|pasiwedhi|passe|passord|passwort|"
			+ "passwuert|paswoodu|phasewete|salasana|sandi|senha|sifre|sifreya|slaptazois|tenimiafina|upufaalilolilo|wachtwoord|wachtwurd|wagwoord";

	private static final Pattern PASSWORD_LITERAL_PATTERN = Pattern.compile("(" + PWD_TRANSLATION + ")=\\S.",
			Pattern.CASE_INSENSITIVE);
	private static final Pattern PASSWORD_VARIABLE_PATTERN = Pattern.compile("(" + PWD_TRANSLATION + ")",
			Pattern.CASE_INSENSITIVE);

	@Override
	public void visitSetStatement(SetStatementTree tree) {
		if (isPasswordVariableName(tree.variableReference()) && isStringLiteral(tree.expression())){
			reportIssue(tree.expression());
		}
		super.visitSetStatement(tree);
	}


	private boolean isPasswordVariableName(VariableReferenceTree variableReference) {
		if (variableReference instanceof FieldReferenceTree){
			return isPasswordVariableName((FieldReferenceTree)variableReference);
		} else {
			return isPasswordVariableName((IdentifierTree)variableReference);
		}
	}


	private boolean isPasswordVariableName(FieldReferenceTree fieldReference) {
		if (fieldReference==null) {
			return false;
		}
		return isPasswordVariableName(fieldReference.pathElement().name().name().text()) || isPasswordVariableName(fieldReference.pathElements());
	}

	private boolean isPasswordVariableName(IdentifierTree identifier) {
		if (identifier==null){
			return false;
		}
		return isPasswordVariableName(identifier.name());
	}


	@Override
	public void visitDeclareStatement(DeclareStatementTree tree) {
		boolean isExternal = tree.sharedExt() != null && "EXTERNAL".equalsIgnoreCase(tree.sharedExt().text());
		if (!isExternal && isStringLiteral(tree.initialValueExpression()) && isPasswordVaribleName(tree.nameList())) {
			reportIssue(tree.initialValueExpression());
		}
	}

	private boolean isPasswordVariableName(SeparatedList<PathElementTree> pathElements) {
		Iterator<PathElementTree> nameListIter = pathElements.iterator();
		while (nameListIter.hasNext()) {
			PathElementTree pathElement= nameListIter.next();
			if (pathElement.name()!=null && pathElement.name().name()!=null && 
					isPasswordVariableName(pathElement.name().name().text())) {
				return true;
			}
		}
		return false;
	}

	private boolean isPasswordVaribleName(SeparatedList<IdentifierTree> nameList) {
		Iterator<IdentifierTree> nameListIter = nameList.iterator();
		while (nameListIter.hasNext()) {
			IdentifierTree name = nameListIter.next();
			if (isPasswordVariableName(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void visitLiteral(LiteralTree tree) {
		if (isStringLiteral(tree) && PASSWORD_LITERAL_PATTERN.matcher(tree.value()).find()){
			reportIssue(tree);
		}
	}

	private static boolean isStringLiteral(@Nullable ExpressionTree initializer) {
		return initializer != null && initializer.is(Tree.Kind.STRING_LITERAL);
	}

	private static boolean isPasswordVariableName(String token) {
		return PASSWORD_VARIABLE_PATTERN.matcher(token).find();
	}

	private void reportIssue(Tree tree) {
		addIssue(tree, "Remove this hard-coded password.");
	}
}
