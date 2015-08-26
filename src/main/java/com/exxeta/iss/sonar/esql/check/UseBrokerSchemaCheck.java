package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.NoSqale;
import org.sonar.squidbridge.checks.SquidCheck;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

@Rule(key = UseBrokerSchemaCheck.CHECK_KEY, priority = Priority.MINOR, name = "Files should not be in the DEFAULT schema", tags = Tags.CONVENTION)
@NoSqale
public class UseBrokerSchemaCheck extends SquidCheck<Grammar> {
	public static final String CHECK_KEY = "UseBrokerSchema";
	private boolean brokerSchemaStatementFound = false;
	//private boolean violationCreated = false;

	@Override
	public void init() {
		brokerSchemaStatementFound = false;
		//violationCreated = false;
		subscribeTo(EsqlGrammar.BROKER_SCHEMA_STATEMENT);
	}

	@Override
	public void leaveFile(AstNode astNode) {
		if (!brokerSchemaStatementFound /*&& !violationCreated*/) {
			String message = "The default Schema should not be used. Move the file to a broker schema.";
			getContext().createFileViolation(this, message);
			//violationCreated = true;
		}
	}

	@Override
	public void visitNode(AstNode astNode) {
		brokerSchemaStatementFound = true;
	}

}
