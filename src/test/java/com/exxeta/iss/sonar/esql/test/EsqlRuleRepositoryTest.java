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


import org.junit.Test;
import org.sonar.api.rules.AnnotationRuleParser;
import org.sonar.api.rules.Rule;

import com.exxeta.iss.sonar.esql.EsqlRuleRepository;
import com.exxeta.iss.sonar.esql.check.CheckList;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class EsqlRuleRepositoryTest {

  @Test
  public void test() {
    EsqlRuleRepository ruleRepository = new EsqlRuleRepository(new AnnotationRuleParser());
    assertThat(ruleRepository.getKey()).isEqualTo("esql");
    assertThat(ruleRepository.getName()).isEqualTo("Sonar");
    List<Rule> rules = ruleRepository.createRules();
    assertThat(rules.size()).isEqualTo(CheckList.getChecks().size());
  }

}
