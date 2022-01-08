/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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

import com.exxeta.iss.sonar.esql.api.CustomEsqlRulesDefinition;
import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.api.visitors.TreeVisitor;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.rule.RuleKey;

public class EsqlChecks {
	 private final CheckFactory checkFactory;
	  private Set<Checks<EsqlCheck>> checksByRepository = Sets.newHashSet();

	  private EsqlChecks(CheckFactory checkFactory) {
	    this.checkFactory = checkFactory;
	  }

	  public static EsqlChecks createEsqlCheck(CheckFactory checkFactory) {
	    return new EsqlChecks(checkFactory);
	  }

	  public EsqlChecks addChecks(String repositoryKey, Iterable<Class<?>> checkClass) {
	    checksByRepository.add(checkFactory
	      .<EsqlCheck>create(repositoryKey)
	      .addAnnotatedChecks(checkClass));

	    return this;
	  }

	  public EsqlChecks addCustomChecks(@Nullable CustomEsqlRulesDefinition[] customRulesDefinitions) {
	    if (customRulesDefinitions != null) {

	      for (CustomEsqlRulesDefinition rulesDefinition : customRulesDefinitions) {
	        addChecks(rulesDefinition.repositoryKey(), Lists.newArrayList(rulesDefinition.checkClasses()));
	      }
	    }

	    return this;
	  }

	  private List<EsqlCheck> all() {
	    List<EsqlCheck> allVisitors = Lists.newArrayList();

	    for (Checks<EsqlCheck> checks : checksByRepository) {
	      allVisitors.addAll(checks.all());
	    }

	    return allVisitors;
	  }

	  public List<TreeVisitor> visitorChecks() {
	    List<TreeVisitor> checks = new ArrayList<>();
	    for (EsqlCheck check : all()) {
	      if (check instanceof TreeVisitor) {
	        checks.add((TreeVisitor) check);
	      }
	    }

	    return checks;
	  }

	  @Nullable
	  public RuleKey ruleKeyFor(EsqlCheck check) {
	    RuleKey ruleKey;

	    for (Checks<EsqlCheck> checks : checksByRepository) {
	      ruleKey = checks.ruleKey(check);

	      if (ruleKey != null) {
	        return ruleKey;
	      }
	    }
	    return null;
	  }
}
