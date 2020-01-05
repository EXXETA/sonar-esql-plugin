/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.iib.esql;

import com.exxeta.iss.sonar.esql.check.CheckList;
import com.exxeta.iss.sonar.iib.esql.EsqlLanguage;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.BuiltInQualityProfileJsonLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EsqlProfilesDefinition implements BuiltInQualityProfilesDefinition {
    static final String SONAR_WAY = "Sonar way";

    public static final String RESOURCE_PATH = "org/sonar/l10n/esql/rules/esql";
    public static final String SONAR_WAY_JSON = RESOURCE_PATH + "/Sonar_way_profile.json";

    private static final Map<String, String> PROFILES = new HashMap<>();

    static {
        PROFILES.put(SONAR_WAY, SONAR_WAY_JSON);
    }

    private static final Map<String, String> REPO_BY_LANGUAGE = new HashMap<>();

    static {
        REPO_BY_LANGUAGE.put(EsqlLanguage.KEY, CheckList.REPOSITORY_KEY);
    }

    @Override
    public void define(Context context) {
        Set<String> javaScriptRuleKeys = ruleKeys(CheckList.getChecks());
        createProfile(SONAR_WAY, EsqlLanguage.KEY, javaScriptRuleKeys, context);
    }

    private static void createProfile(String profileName, String language, Set<String> keys, Context context) {
        NewBuiltInQualityProfile newProfile = context.createBuiltInQualityProfile(profileName, language);
        String jsonProfilePath = PROFILES.get(profileName);
        String repositoryKey = REPO_BY_LANGUAGE.get(language);
        Set<String> activeKeysForBothLanguages = BuiltInQualityProfileJsonLoader.loadActiveKeysFromJsonProfile(jsonProfilePath);

        keys.stream()
                .filter(activeKeysForBothLanguages::contains)
                .forEach(key -> newProfile.activateRule(repositoryKey, key));

        newProfile.done();
    }

    private static Set<String> ruleKeys(List<Class> checks) {
        return checks.stream()
                .map(c -> ((Rule) c.getAnnotation(Rule.class)).key())
                .collect(Collectors.toSet());
    }
}
