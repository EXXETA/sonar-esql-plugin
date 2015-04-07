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
package com.exxeta.iss.sonar.esql;


import org.sonar.api.rules.AnnotationRuleParser;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RuleRepository;

import com.exxeta.iss.sonar.esql.check.CheckList;
import com.exxeta.iss.sonar.esql.core.Esql;

import java.util.List;

public class EsqlRuleRepository extends RuleRepository {

  private final AnnotationRuleParser annotationRuleParser;

  public EsqlRuleRepository(AnnotationRuleParser annotationRuleParser) {
    super(CheckList.REPOSITORY_KEY, Esql.KEY);
    setName(CheckList.REPOSITORY_NAME);
    this.annotationRuleParser = annotationRuleParser;
  }

  @Override
  public List<Rule> createRules() {
	  List<Rule> rules = annotationRuleParser.parse(CheckList.REPOSITORY_KEY, CheckList.getChecks());
    return rules;
  }

}
