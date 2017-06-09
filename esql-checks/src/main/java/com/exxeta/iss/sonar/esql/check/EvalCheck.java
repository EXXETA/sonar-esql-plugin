package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

@Rule(key="Eval")
public class EvalCheck extends AbstractDoNotUseFunctinCheck {

	@Override
	public String getMessage() {
		return "EVAL should not be used because untested code could be injected.";
	}

	@Override
	public String getFunctionName() {
		return "EVAL";
	}

}
