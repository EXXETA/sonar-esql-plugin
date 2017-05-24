package com.exxeta.iss.sonar.esql.check;

import java.util.Iterator;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.CallExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.tree.statement.CallStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.exxeta.iss.sonar.esql.tree.impl.SeparatedList;
import com.exxeta.iss.sonar.esql.tree.impl.declaration.FieldReferenceTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.lexical.InternalSyntaxToken;

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
		if (isPasswordVariableName(tree.fieldReference()) && isStringLiteral(tree.expression())){
			reportIssue(tree.expression());
		}
		super.visitSetStatement(tree);
	}


	private boolean isPasswordVariableName(FieldReferenceTreeImpl fieldReference) {
		return isPasswordVariableName(fieldReference.pathElement().name().name()) || isPasswordVariableName(fieldReference.pathElements());
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
			if (isPasswordVariableName(pathElement.name().name())) {
				return true;
			}
		}
		return false;
	}

	private boolean isPasswordVaribleName(SeparatedList<InternalSyntaxToken> nameList) {
		Iterator<InternalSyntaxToken> nameListIter = nameList.iterator();
		while (nameListIter.hasNext()) {
			InternalSyntaxToken name = nameListIter.next();
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

	private static boolean isPasswordVariableName(SyntaxToken token) {
		return PASSWORD_VARIABLE_PATTERN.matcher(token.text()).find();
	}

	private void reportIssue(Tree tree) {
		addIssue(tree, "Remove this hard-coded password.");
	}
}
