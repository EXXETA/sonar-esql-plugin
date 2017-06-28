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

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.rules.RulePriority;
import org.sonar.api.utils.ValidationMessages;

import com.exxeta.iss.sonar.esql.check.CheckList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EsqlProfileTest {

  @Test
  public void should_create_sonar_way_recommended_profile() {
    ValidationMessages validation = ValidationMessages.create();

    RuleFinder ruleFinder = ruleFinder();
    EsqlProfile definition = new EsqlProfile(ruleFinder);
    RulesProfile profile = definition.createProfile(validation);

    assertThat(profile.getLanguage()).isEqualTo(EsqlLanguage.KEY);
    assertThat(profile.getName()).isEqualTo(EsqlProfile.PROFILE_NAME);
    assertThat(profile.getActiveRules()).extracting("repositoryKey").containsOnly("common-esql", CheckList.REPOSITORY_KEY);
    assertThat(validation.hasErrors()).isFalse();
    assertThat(profile.getActiveRules().size()).isGreaterThan(10);
  }

  @Test
  public void should_not_include_common_rules_for_sonarlint() {
    ValidationMessages validation = ValidationMessages.create();
    RuleFinder ruleFinder = sonarLintRuleFinder();
    EsqlProfile definition = new EsqlProfile(ruleFinder);
    RulesProfile profile = definition.createProfile(validation);

    // no "common-esql" here
    assertThat(profile.getActiveRules()).extracting("repositoryKey").containsOnly(CheckList.REPOSITORY_KEY);

    assertThat(profile.getLanguage()).isEqualTo(EsqlLanguage.KEY);
    assertThat(profile.getName()).isEqualTo(EsqlProfile.PROFILE_NAME);
    assertThat(validation.hasErrors()).isFalse();
    assertThat(profile.getActiveRules().size()).isGreaterThan(10);
  }

  static RuleFinder ruleFinder() {
    return when(mock(RuleFinder.class).findByKey(anyString(), anyString())).thenAnswer(new Answer<Rule>() {
      @Override
      public Rule answer(InvocationOnMock invocation) {
        Object[] arguments = invocation.getArguments();
        return Rule.create((String) arguments[0], (String) arguments[1], (String) arguments[1]);
      }
    }).getMock();
  }

  /**
   * SonarLint will inject a rule finder containing only the rules coming from the javascript repository
   */
  private RuleFinder sonarLintRuleFinder() {
    return when(mock(RuleFinder.class).findByKey(anyString(), anyString())).thenAnswer(invocation -> {
      Object[] arguments = invocation.getArguments();
      if (CheckList.REPOSITORY_KEY.equals(arguments[0])) {
        Rule rule = Rule.create((String) arguments[0], (String) arguments[1], (String) arguments[1]);
        return rule.setSeverity(RulePriority.MINOR);
      }
      return null;
    }).getMock();
  }


}
