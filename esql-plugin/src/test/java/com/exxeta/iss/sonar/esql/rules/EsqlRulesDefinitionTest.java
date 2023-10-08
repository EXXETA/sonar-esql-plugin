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
package com.exxeta.iss.sonar.esql.rules;

import org.junit.jupiter.api.Test;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction.Type;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Param;
import org.sonar.api.server.rule.RulesDefinition.Repository;
import org.sonar.api.server.rule.RulesDefinition.Rule;
import org.sonar.api.utils.Version;

import com.exxeta.iss.sonar.esql.EsqlRulesDefinition;
import com.exxeta.iss.sonar.esql.check.CheckList;

import static org.assertj.core.api.Assertions.assertThat;

class EsqlRulesDefinitionTest {

  @Test
  void test() {
    RulesDefinition.Repository repository = buildRepository();

    assertThat(repository.name()).isEqualTo("SonarQube");
    assertThat(repository.language()).isEqualTo("esql");
    assertThat(repository.rules()).hasSize(CheckList.getChecks().size());

    assertRuleProperties(repository);
    assertParameterProperties(repository);
    assertAllRuleParametersHaveDescription(repository);
  }

  private RulesDefinition.Repository buildRepository() {
    EsqlRulesDefinition rulesDefinition = new EsqlRulesDefinition();
    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    RulesDefinition.Repository repository = context.repository("esql");
    return repository;
  }

  private void assertParameterProperties(Repository repository) {
    // TooManyLinesInFunctionCheck
    Param max = repository.rule("TooManyLinesInFile").param("maximum");
    assertThat(max).isNotNull();
    assertThat(max.defaultValue()).isEqualTo("2000");
    assertThat(max.description()).isEqualTo("the maximum authorized lines");
    assertThat(max.type()).isEqualTo(RuleParamType.INTEGER);
  }

  private void assertRuleProperties(Repository repository) {
    Rule rule = repository.rule("UnusedRoutine");
    assertThat(rule).isNotNull();
    assertThat(rule.name()).isEqualTo("Unused routines should be removed");
    assertThat(rule.debtRemediationFunction().type()).isEqualTo(Type.CONSTANT_ISSUE);
    assertThat(rule.type()).isEqualTo(RuleType.CODE_SMELL);
    assertThat(repository.rule("CommentRegularExpression").template()).isTrue();
  }

  private void assertAllRuleParametersHaveDescription(Repository repository) {
    for (Rule rule : repository.rules()) {
      for (Param param : rule.params()) {
        assertThat(param.description()).as("description for " + param.key()).isNotEmpty();
      }
    }
  }

}
