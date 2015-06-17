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
 */
package com.exxeta.iss.sonar.esql.test;

import com.exxeta.iss.sonar.esql.check.CheckList;
import com.google.common.collect.Sets;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleParam;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

public class CheckListTest {

	/**
	 * Enforces that each check declared in list.
	 */
	@Test
	public void count() {
		int count = 0;
		List<File> files = (List<File>) FileUtils.listFiles(new File("src/main/java/com/exxeta/iss/sonar/esql/check/"),
				new String[] { "java" }, false);
		for (File file : files) {
			if (file.getName().endsWith("Check.java") && !file.getName().startsWith("Abstract")) {
				count++;
			}
		}
		assertThat(CheckList.getChecks().size()).isEqualTo(count);
	}

	/**
	 * Enforces that each check has test, name and description.
	 */
	@Test
	public void test() {
		List<Class> checks = CheckList.getChecks();
		for (Class cls : checks) {
			String testName = '/' + cls.getName().replace('.', '/') + "Test.class";
			assertThat(getClass().getResource(testName)).overridingErrorMessage("No test for " + cls.getSimpleName())
					.isNotNull();
		}
		ResourceBundle resourceBundle = ResourceBundle.getBundle("org.sonar.l10n.esql", Locale.ENGLISH);
		Set<String> keys = Sets.newHashSet();
		ActiveRules activeRules = (new ActiveRulesBuilder()).build();
		CheckFactory checkFactory = new CheckFactory(activeRules);
		Collection<Rule> rules = checkFactory.<Rule> create("repositoryKey").addAnnotatedChecks(CheckList.getChecks())
				.all();
		for (Rule rule : rules) {
			assertThat(keys).as("Duplicate key " + rule.getKey()).excludes(rule.getKey());
			keys.add(rule.getKey());
			resourceBundle.getString("rule." + CheckList.REPOSITORY_KEY + "." + rule.getKey() + ".name");
			assertThat(getClass().getResource("/org/sonar/l10n/esql/rules/esql/" + rule.getKey() + ".html"))
					.overridingErrorMessage("No description for " + rule.getKey()).isNotNull();
			assertThat(rule.getDescription()).overridingErrorMessage(
					"Description of " + rule.getKey() + " should be in separate file").isNull();
			for (RuleParam param : rule.getParams()) {
				resourceBundle.getString("rule." + CheckList.REPOSITORY_KEY + "." + rule.getKey() + ".param."
						+ param.getKey());
				assertThat(param.getDescription()).overridingErrorMessage(
						"Description for param " + param.getKey() + " of " + rule.getKey()
								+ " should be in separate file").isEmpty();
			}
		}
	}
}
