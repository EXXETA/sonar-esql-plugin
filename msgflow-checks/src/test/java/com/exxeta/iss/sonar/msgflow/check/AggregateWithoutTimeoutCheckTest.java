package com.exxeta.iss.sonar.msgflow.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;
import com.exxeta.iss.sonar.msgflow.check.verifier.MsgflowCheckVerifier;

public class AggregateWithoutTimeoutCheckTest {
	@Test
	public void test() {
		final MsgflowCheck check = new AggregateWithoutTimeoutCheck();
		MsgflowCheckVerifier.issues(check, new File("src/test/resources/AggregateWithoutTimeout.msgflow")).next()
				.atLine(9)
				.withMessage(
						"'timeoutInterval' property for Aggregate Control Node 'Aggregate Control' is set to infinite(value = 0).")
				.noMore();
	}
}
