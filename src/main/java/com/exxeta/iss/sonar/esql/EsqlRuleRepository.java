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
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.squidbridge.annotations.AnnotationBasedRulesDefinition;

import com.exxeta.iss.sonar.esql.check.CheckList;
import com.exxeta.iss.sonar.esql.core.Esql;

public class EsqlRuleRepository implements RulesDefinition{

  public static final String REPOSITORY_NAME = "SonarQube";


  @Override
  public void define(Context context) {
  NewRepository repository = context
  .createRepository(CheckList.REPOSITORY_KEY, Esql.KEY)
  .setName(REPOSITORY_NAME);
  AnnotationBasedRulesDefinition.load(repository, Esql.KEY, CheckList.getChecks());
  repository.done();
  }
  
  
}
