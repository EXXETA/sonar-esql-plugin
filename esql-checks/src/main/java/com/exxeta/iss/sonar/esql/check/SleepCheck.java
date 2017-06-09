package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

@Rule(key="Sleep")
public class SleepCheck extends AbstractDoNotUseFunctinCheck {

	@Override
	public String getMessage() {
		return "SLEEP should not be used because it blocks the executing thread.";
	}

	@Override
	public String getFunctionName() {
		return "SLEEP";
	}

}
