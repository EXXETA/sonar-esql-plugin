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
package com.exxeta.iss.sonar.esql;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nullable;

import org.sonar.api.rule.RuleStatus;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.debt.DebtRemediationFunction;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.utils.Version;
import org.sonar.squidbridge.annotations.AnnotationBasedRulesDefinition;

import com.exxeta.iss.sonar.esql.check.CheckList;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;

public class EsqlRulesDefinition implements RulesDefinition {

  private final Gson gson = new Gson();
  private final Version sonarRuntimeVersion;

  public EsqlRulesDefinition(Version sonarRuntimeVersion) {
    this.sonarRuntimeVersion = sonarRuntimeVersion;
  }

  @Override
  public void define(Context context) {
    NewRepository repository = context
      .createRepository(CheckList.REPOSITORY_KEY, EsqlLanguage.KEY)
      .setName(CheckList.REPOSITORY_NAME);

    new AnnotationBasedRulesDefinition(repository, EsqlLanguage.KEY)
      .addRuleClasses(/* don't fail if no SQALE annotations */ false, CheckList.getChecks());

    for (NewRule rule : repository.rules()) {
      String metadataKey = rule.key();
      // Setting internal key is essential for rule templates (see SONAR-6162), and it is not done by AnnotationBasedRulesDefinition from sslr-squid-bridge version 2.5.1:
      rule.setInternalKey(metadataKey);
      rule.setHtmlDescription(readRuleDefinitionResource(metadataKey + ".html"));
      addMetadata(rule, metadataKey);
    }

    boolean shouldSetupSonarLintProfile = sonarRuntimeVersion.isGreaterThanOrEqual(Version.parse("6.0"));
    if (shouldSetupSonarLintProfile) {
      Set<String> activatedRuleKeys = JsonProfileReader.ruleKeys(EsqlProfile.PATH_TO_JSON);
      for (NewRule rule : repository.rules()) {
        rule.setActivatedByDefault(activatedRuleKeys.contains(rule.key()));
      }
    }

    repository.done();
  }

  @Nullable
  private static String readRuleDefinitionResource(String fileName) {
    URL resource = EsqlRulesDefinition.class.getResource("/org/sonar/l10n/esql/rules/esql/" + fileName);
    if (resource == null) {
      return null;
    }
    try {
      return Resources.toString(resource, Charsets.UTF_8);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read: " + resource, e);
    }
  }


  private void addMetadata(NewRule rule, String metadataKey) {
    String json = readRuleDefinitionResource(metadataKey + ".json");
    if (json != null) {
      RuleMetadata metadata = gson.fromJson(json, RuleMetadata.class);
      rule.setSeverity(metadata.defaultSeverity.toUpperCase(Locale.US));
      rule.setName(metadata.title);
      rule.setTags(metadata.tags);
      rule.setType(RuleType.valueOf(metadata.type));
      rule.setStatus(RuleStatus.valueOf(metadata.status.toUpperCase(Locale.US)));

      if (metadata.remediation != null) {
        // metadata.remediation is null for template rules
        rule.setDebtRemediationFunction(metadata.remediation.remediationFunction(rule.debtRemediationFunctions()));
        rule.setGapDescription(metadata.remediation.linearDesc);
      }
    }
  }

  private static class RuleMetadata {
    String title;
    String status;
    String type;
    @Nullable
    Remediation remediation;

    String[] tags;
    String defaultSeverity;
  }

  private static class Remediation {
    String func;
    String constantCost;
    String linearDesc;
    String linearOffset;
    String linearFactor;

    private DebtRemediationFunction remediationFunction(DebtRemediationFunctions drf) {
      if (func.startsWith("Constant")) {
        return drf.constantPerIssue(constantCost.replace("mn", "min"));
      }
      if ("Linear".equals(func)) {
        return drf.linear(linearFactor.replace("mn", "min"));
      }
      return drf.linearWithOffset(linearFactor.replace("mn", "min"), linearOffset.replace("mn", "min"));
    }
  }
}
