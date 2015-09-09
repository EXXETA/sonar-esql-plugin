package com.exxeta.iss.sonar.esql.check;

import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

@Rule(key = "TooManyIterateOrLeaveInLoop", name = "Loops should not contain more than a single \"ITERATE\" or \"LEAVE\" statement", priority = Priority.MAJOR, tags = { Tags.BRAIN_OVERLOAD })
@ActivatedByDefault
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.UNDERSTANDABILITY)
@SqaleConstantRemediation("20min")
@BelongsToProfile(title=CheckList.SONAR_WAY_PROFILE,priority=Priority.MAJOR)
public class TooManyIterateOrLeaveInLoopCheck extends SquidCheck<Grammar> {

	@Override
	public void init() {
		subscribeTo(EsqlGrammar.beginEndStatement, EsqlGrammar.whileStatement, EsqlGrammar.repeatStatement,
				EsqlGrammar.loopStatement);
	}


	@Override
	public void visitNode(AstNode astNode) {
		if (astNode.getFirstChild(EsqlGrammar.LABEL)!=null){
			String labelName = astNode.getFirstChild(EsqlGrammar.LABEL).getTokenOriginalValue();
			int countIterate = 0;
			int countLeave = 0;
			for (AstNode statement: astNode.getDescendants(EsqlGrammar.iterateStatement)){
				String innerLabel = statement.getFirstChild(EsqlGrammar.LABEL).getTokenOriginalValue();
				if (innerLabel.equals(labelName)){
					countIterate++;
				}
			}
			for (AstNode statement: astNode.getDescendants(EsqlGrammar.leaveStatement)){
				String innerLabel = statement.getFirstChild(EsqlGrammar.LABEL).getTokenOriginalValue();
				if (innerLabel.equals(labelName)){
					countLeave++;
				}
			}
			if (countIterate>1 || countLeave>1){
				getContext().createLineViolation(this, "Loops should not contain more than a single \"ITERATE\" or \"LEAVE\" statement.", astNode);
			}
		}
	}
}