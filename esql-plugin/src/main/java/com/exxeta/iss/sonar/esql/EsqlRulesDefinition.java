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
import org.sonar.api.server.rule.RulesDefinition;
import org.sonarsource.analyzer.commons.RuleMetadataLoader;

public class EsqlRulesDefinition implements RulesDefinition {

    private static final String METADATA_LOCATION = "org/sonar/l10n/esql/rules/esql";

    @Override
    public void define(Context context) {
        NewRepository repository = context
                .createRepository(CheckList.REPOSITORY_KEY, EsqlLanguage.KEY)
                .setName(CheckList.REPOSITORY_NAME);

        RuleMetadataLoader ruleMetadataLoader = new RuleMetadataLoader(METADATA_LOCATION, EsqlProfilesDefinition.SONAR_WAY_JSON);
        ruleMetadataLoader.addRulesByAnnotatedClass(repository, CheckList.getChecks());

        NewRule commentRegularExpression = repository.rule("CommentRegularExpression");
        if (commentRegularExpression != null) {
            commentRegularExpression.setTemplate(true);
        }


        repository.done();
    }

}
