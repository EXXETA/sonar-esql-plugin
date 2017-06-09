package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

@Rule(key="Bitstream")
public class BitstreamCheck extends AbstractDoNotUseFunctinCheck {

	@Override
	public String getMessage() {
		return "Do not use BITSTREAM it is deprecated.";
	}

	@Override
	public String getFunctionName() {
		return "BITSTREAM";
	}

}
