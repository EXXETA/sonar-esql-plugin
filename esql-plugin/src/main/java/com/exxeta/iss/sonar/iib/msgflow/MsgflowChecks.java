/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
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
 */
package com.exxeta.iss.sonar.iib.msgflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.rule.RuleKey;

import com.exxeta.iss.sonar.msgflow.api.CustomMsgflowRulesDefinition;
import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;
import com.exxeta.iss.sonar.msgflow.api.visitors.MsgflowVisitor;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class MsgflowChecks {
	public static MsgflowChecks createMsgflowCheck(final CheckFactory checkFactory) {
		return new MsgflowChecks(checkFactory);
	}

	private final CheckFactory checkFactory;

	private final Set<Checks<MsgflowCheck>> checksByRepository = Sets.newHashSet();

	private MsgflowChecks(final CheckFactory checkFactory) {
		this.checkFactory = checkFactory;
	}

	public MsgflowChecks addChecks(final String repositoryKey, final Iterable<Class> checkClass) {
		checksByRepository.add(checkFactory.<MsgflowCheck>create(repositoryKey).addAnnotatedChecks(checkClass));

		return this;
	}

	public MsgflowChecks addCustomChecks(@Nullable final CustomMsgflowRulesDefinition[] customRulesDefinitions) {
		if (customRulesDefinitions != null) {

			for (final CustomMsgflowRulesDefinition rulesDefinition : customRulesDefinitions) {
				addChecks(rulesDefinition.repositoryKey(), Lists.newArrayList(rulesDefinition.checkClasses()));
			}
		}

		return this;
	}

	private List<MsgflowCheck> all() {
		final List<MsgflowCheck> allVisitors = Lists.newArrayList();

		for (final Checks<MsgflowCheck> checks : checksByRepository) {
			allVisitors.addAll(checks.all());
		}

		return allVisitors;
	}

	@Nullable
	public RuleKey ruleKeyFor(final MsgflowCheck check) {
		RuleKey ruleKey;

		for (final Checks<MsgflowCheck> checks : checksByRepository) {
			ruleKey = checks.ruleKey(check);

			if (ruleKey != null) {
				return ruleKey;
			}
		}
		return null;
	}

	public List<MsgflowVisitor> visitorChecks() {
		final List<MsgflowVisitor> checks = new ArrayList<>();
		for (final MsgflowCheck check : all()) {
			if (check instanceof MsgflowVisitor) {
				checks.add((MsgflowVisitor) check);
			}
		}

		return checks;
	}
}
