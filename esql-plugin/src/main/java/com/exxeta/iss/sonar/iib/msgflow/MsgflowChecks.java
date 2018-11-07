/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.iib.msgflow;

import com.exxeta.iss.sonar.msgflow.api.CustomMsgflowRulesDefinition;
import com.exxeta.iss.sonar.msgflow.api.MsgflowCheck;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.rule.RuleKey;

public class MsgflowChecks {
	 private final CheckFactory checkFactory;
	  private Set<Checks<MsgflowCheck>> checksByRepository = Sets.newHashSet();

	  private MsgflowChecks(CheckFactory checkFactory) {
	    this.checkFactory = checkFactory;
	  }

	  public static MsgflowChecks createMsgflowCheck(CheckFactory checkFactory) {
	    return new MsgflowChecks(checkFactory);
	  }

	  public MsgflowChecks addChecks(String repositoryKey, Iterable<Class> checkClass) {
	    checksByRepository.add(checkFactory
	      .<MsgflowCheck>create(repositoryKey)
	      .addAnnotatedChecks(checkClass));

	    return this;
	  }

	  public MsgflowChecks addCustomChecks(@Nullable CustomMsgflowRulesDefinition[] customRulesDefinitions) {
	    if (customRulesDefinitions != null) {

	      for (CustomMsgflowRulesDefinition rulesDefinition : customRulesDefinitions) {
	        addChecks(rulesDefinition.repositoryKey(), Lists.newArrayList(rulesDefinition.checkClasses()));
	      }
	    }

	    return this;
	  }

	  private List<MsgflowCheck> all() {
	    List<MsgflowCheck> allVisitors = Lists.newArrayList();

	    for (Checks<MsgflowCheck> checks : checksByRepository) {
	      allVisitors.addAll(checks.all());
	    }

	    return allVisitors;
	  }

	  public List<TreeVisitor> visitorChecks() {
	    List<TreeVisitor> checks = new ArrayList<>();
	    for (MsgflowChecks check : all()) {
	      if (check instanceof TreeVisitor) {
	        checks.add((TreeVisitor) check);
	      }
	    }

	    return checks;
	  }

	  @Nullable
	  public RuleKey ruleKeyFor(MsgflowCheck check) {
	    RuleKey ruleKey;

	    for (Checks<MsgflowCheck> checks : checksByRepository) {
	      ruleKey = checks.ruleKey(check);

	      if (ruleKey != null) {
	        return ruleKey;
	      }
	    }
	    return null;
	  }
}
