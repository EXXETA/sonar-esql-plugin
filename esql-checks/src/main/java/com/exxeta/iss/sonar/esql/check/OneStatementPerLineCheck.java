/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */package com.exxeta.iss.sonar.esql.check;

import java.util.Map;

import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.annotations.NoSqale;
import org.sonar.squidbridge.checks.SquidCheck;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.google.common.collect.Maps;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;

@Rule(key = "OneStatementPerLine", priority = Priority.MAJOR)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
@NoSqale
public class OneStatementPerLineCheck extends SquidCheck<Grammar> {

	private final Map<Integer, Integer> statementsPerLine = Maps.newHashMap();

	@Override
	public void init() {
		subscribeTo(EsqlGrammar.statement);
	}

	@Override
	public void visitFile(AstNode astNode) {
		statementsPerLine.clear();
	}

	@Override
	public void visitNode(AstNode statementNode) {

		int line = statementNode.getTokenLine();
		if (!statementsPerLine.containsKey(line)) {
			statementsPerLine.put(line, 0);
		}
		statementsPerLine.put(line, statementsPerLine.get(line) + 1);
	}

	@Override
	public void leaveFile(AstNode astNode) {
		for (Map.Entry<Integer, Integer> statementsAtLine : statementsPerLine
				.entrySet()) {
			if (statementsAtLine.getValue() > 1) {
				getContext()
						.createLineViolation(
								this,
								"At most one statement is allowed per line, but {0} statements were found on this line.",
								statementsAtLine.getKey(),
								statementsAtLine.getValue());
			}
		}
	}

}