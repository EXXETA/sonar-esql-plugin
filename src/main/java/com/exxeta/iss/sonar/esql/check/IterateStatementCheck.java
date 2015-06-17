package com.exxeta.iss.sonar.esql.check;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

@Rule(key = "IterateStatement", name = "\"ITERATE\" should not be used", priority = Priority.CRITICAL)
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNIT_TESTABILITY)
@SqaleConstantRemediation("30min")
public class IterateStatementCheck extends SquidCheck<Grammar> {
	@Override
	public void init() {
		subscribeTo(EsqlGrammar.iterateStatement);
	}

	@Override
	public void visitNode(AstNode astNode) {
		getContext().createLineViolation(this, "Avoid using ITERATE statement.", astNode);
	}
}