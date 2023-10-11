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
package com.exxeta.iss.sonar.esql.check;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionAnnotationLoader;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.check.Rule;

import static org.assertj.core.api.Fail.fail;

class CheckListTest {
    private static final String ARTIFICIAL_DESCRIPTION = "-1";

    /**
     * Enforces that each check declared in list.
     */
    @Test
    void count() {
        int count = 0;
        List<File> files = (List<File>) FileUtils.listFiles(new File("src/main/java/com/exxeta/iss/sonar/esql/check/"),
                new String[]{"java"}, true);
        for (File file : files) {
            String name = file.getName();
            if (name.endsWith("Check.java") && !name.startsWith("Abstract")) {
                count++;
            }
        }
        assertThat(CheckList.getChecks().size()).isEqualTo(count);
    }

    private static class CustomRulesDefinition implements RulesDefinition {

        @Override
        public void define(Context context) {
            String language = "esql";
            NewRepository repository = context
                    .createRepository(CheckList.REPOSITORY_KEY, language)
                    .setName("SonarQube");

            List<Class<?>> checks = CheckList.getChecks();
            new RulesDefinitionAnnotationLoader().load(repository, checks.toArray(new Class[checks.size()]));

            for (NewRule rule : repository.rules()) {
                try {
                    rule.setName("Artificial Name (set via JSON files, no need to test it)");
                    rule.setMarkdownDescription(ARTIFICIAL_DESCRIPTION);
                } catch (IllegalStateException e) {
                    // it means that the html description was already set in Rule annotation
                    fail("Description of " + rule.key() + " should be in separate file");
                }
            }
            repository.done();
        }
    }

    /**
     * Enforces that each check has test, name and description.
     */
    @Test
    void test() {

        Map<String, String> keyMap = new HashMap<>();
        for (Class<?> cls : CheckList.getChecks()) {
            String testName = '/' + cls.getName().replace('.', '/') + "Test.class";
            String simpleName = cls.getSimpleName();
            Rule ruleAnnotation = AnnotationUtils.getAnnotation(cls, Rule.class);
            String key = getKey(cls, ruleAnnotation);
            keyMap.put(ruleAnnotation.key(), key);
            assertThat(getClass().getResource(testName))
                    .overridingErrorMessage("No test for " + simpleName)
                    .isNotNull();
        }

        Set<String> keys = new HashSet<>();
        Set<String> names = new HashSet<>();
        CustomRulesDefinition definition = new CustomRulesDefinition();
        RulesDefinition.Context context = new RulesDefinition.Context();
        definition.define(context);
        List<RulesDefinition.Rule> rules = context.repository(CheckList.REPOSITORY_KEY).rules();

        for (RulesDefinition.Rule rule : rules) {
            assertThat(keys).as("Duplicate key " + rule.key()).doesNotContain(rule.key());
            keys.add(rule.key());
            names.add(rule.name());
            assertThat(getClass().getResource("/org/sonar/l10n/esql/rules/" + CheckList.REPOSITORY_KEY + "/" + keyMap.get(rule.key()) + ".html"))
                    .overridingErrorMessage("No description for " + rule.key() + " " + keyMap.get(rule.key()))
                    .isNotNull();
            assertThat(getClass().getResource("/org/sonar/l10n/esql/rules/" + CheckList.REPOSITORY_KEY + "/" + keyMap.get(rule.key()) + ".json"))
                    .overridingErrorMessage("No json metadata file for " + rule.key() + " " + keyMap.get(rule.key()))
                    .isNotNull();

            assertThat(rule.htmlDescription()).isNull();
            assertThat(rule.markdownDescription()).isEqualTo(ARTIFICIAL_DESCRIPTION);

            for (RulesDefinition.Param param : rule.params()) {
                assertThat(param.description()).overridingErrorMessage(rule.key() + " missing description for param " + param.key()).isNotEmpty();
            }
        }

        assertThat(keys).doesNotHaveDuplicates();
    }

    private static String getKey(Class<?> cls, Rule ruleAnnotation) {
        String key = ruleAnnotation.key();
        Rule rspecKeyAnnotation = AnnotationUtils.getAnnotation(cls, Rule.class);
        if (rspecKeyAnnotation != null) {
            return rspecKeyAnnotation.key();
        }
        return key;
    }
}
