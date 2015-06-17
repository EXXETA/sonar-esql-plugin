package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.NoSqale;
import org.sonar.squidbridge.checks.SquidCheck;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

@Rule(
		key = "ElseIfWithoutElse",
		name = "\"IF ... ELSEIF\" constructs should be terminated with an \"ELSE\" clause",
		priority = Priority.MAJOR, 
		tags = { Tags.CERT })
@NoSqale
public class ElseIfWithoutElseCheck extends SquidCheck<Grammar> {

	@Override
	public void init() {
		subscribeTo(EsqlGrammar.ifStatement);
	}

	@Override
	public void visitNode(AstNode node) {
		if (!node.getChildren(EsqlGrammar.ELSEIF_CLAUSE).isEmpty() && node.getChildren(EsqlGrammar.ELSE_CLAUSE).isEmpty()) {
			getContext().createLineViolation(this, "Add the missing \"ELSE\" clause.", node);
		}
	}

}
