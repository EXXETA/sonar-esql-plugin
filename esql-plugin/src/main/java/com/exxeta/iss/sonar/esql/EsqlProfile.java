/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;

import com.exxeta.iss.sonar.esql.check.CheckList;

public class EsqlProfile extends ProfileDefinition {

	public static final String PROFILE_NAME = "Sonar way Recommended";
	public static final String PATH_TO_JSON = "/org/sonar/l10n/esql/rules/esql/Sonar_way_profile.json";

	private final RuleFinder ruleFinder;

	public EsqlProfile(RuleFinder ruleFinder) {
		this.ruleFinder = ruleFinder;
	}

	@Override
	public RulesProfile createProfile(ValidationMessages messages) {
		RulesProfile profile = RulesProfile.create(CheckList.SONAR_WAY_PROFILE, EsqlLanguage.KEY);

		loadFromCommonRepository(profile);
		loadActiveKeysFromJsonProfile(profile);
		return profile;
	}

	private void loadFromCommonRepository(RulesProfile profile) {
		Rule duplicatedBlocksRule = ruleFinder.findByKey("common-" + EsqlLanguage.KEY, "DuplicatedBlocks");
		// in SonarLint duplicatedBlocksRule == null
		if (duplicatedBlocksRule != null) {
			profile.activateRule(duplicatedBlocksRule, null);
		}
	}

	private void loadActiveKeysFromJsonProfile(RulesProfile rulesProfile) {
		for (String ruleKey : JsonProfileReader.ruleKeys(PATH_TO_JSON)) {
			Rule rule = ruleFinder.findByKey(CheckList.REPOSITORY_KEY, ruleKey);
			rulesProfile.activateRule(rule, null);
		}
	}

}
