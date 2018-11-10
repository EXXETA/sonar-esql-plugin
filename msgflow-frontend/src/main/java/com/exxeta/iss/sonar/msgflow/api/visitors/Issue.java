package com.exxeta.iss.sonar.msgflow.api.visitors;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;

public interface Issue {
	MsgflowCheck check();

	@Nullable
	Double cost();

	Issue cost(double cost);

}
