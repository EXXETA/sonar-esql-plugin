package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.NoSqale;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;

@Rule(key = UseBrokerSchemaCheck.CHECK_KEY)
@NoSqale
public class UseBrokerSchemaCheck extends DoubleDispatchVisitorCheck {
	public static final String CHECK_KEY = "UseBrokerSchema";


	@Override
	public void visitProgram(ProgramTree tree) {
		
		if (tree.brokerSchemaStatement()==null){
			String message = "The default Schema should not be used. Move the file to a broker schema.";
			addIssue(new FileIssue(this, message));
		}
		
		super.visitProgram(tree);
		
	}

}
