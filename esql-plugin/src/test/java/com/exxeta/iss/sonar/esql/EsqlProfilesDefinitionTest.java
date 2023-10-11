/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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

import com.exxeta.iss.sonar.esql.check.CheckList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.BuiltInQualityProfile;
import org.sonar.check.Rule;
import org.sonarsource.analyzer.commons.BuiltInQualityProfileJsonLoader;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

import static com.exxeta.iss.sonar.esql.EsqlProfilesDefinition.SONAR_WAY;
import static com.exxeta.iss.sonar.esql.EsqlProfilesDefinition.SONAR_WAY_JSON;
import static org.assertj.core.api.Assertions.assertThat;

class EsqlProfilesDefinitionTest {

    private BuiltInQualityProfilesDefinition.Context context = new BuiltInQualityProfilesDefinition.Context();

    @BeforeEach
    void setUp() {
        new EsqlProfilesDefinition().define(context);
    }

    @Test
    void sonar_way_esql() {
        BuiltInQualityProfile profile = context.profile(EsqlLanguage.KEY, SONAR_WAY);

        assertThat(profile.language()).isEqualTo(EsqlLanguage.KEY);
        assertThat(profile.name()).isEqualTo(SONAR_WAY);
        assertThat(profile.rules()).extracting("repoKey").containsOnly(CheckList.REPOSITORY_KEY);
        assertThat(profile.rules().size()).isGreaterThan(50);
    }

    @Test
    void no_legacy_Key_in_profile_json() {
        Set<String> allKeys = CheckList.getChecks().stream().map(c -> {
            Annotation ruleAnnotation = c.getAnnotation(Rule.class);
            return ((Rule) ruleAnnotation).key();
        }).collect(Collectors.toSet());

        Set<String> sonarWayKeys = BuiltInQualityProfileJsonLoader.loadActiveKeysFromJsonProfile(SONAR_WAY_JSON);

        assertThat(sonarWayKeys).isSubsetOf(allKeys);
    }
}
