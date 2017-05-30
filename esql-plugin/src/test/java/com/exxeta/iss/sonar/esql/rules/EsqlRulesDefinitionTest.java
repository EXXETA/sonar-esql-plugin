package com.exxeta.iss.sonar.esql.rules;

import org.junit.Test;
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

public class EsqlRulesDefinitionTest {

  @Test
  public void test() {
    RulesDefinition.Repository repository = buildRepository(Version.parse("5.6"));

    assertThat(repository.name()).isEqualTo("SonarAnalyzer");
    assertThat(repository.language()).isEqualTo("esql");
    assertThat(repository.rules()).hasSize(CheckList.getChecks().size());

    assertRuleProperties(repository);
    assertParameterProperties(repository);
    assertAllRuleParametersHaveDescription(repository);
  }

  private RulesDefinition.Repository buildRepository(Version sonarRuntimeVersion) {
    EsqlRulesDefinition rulesDefinition = new EsqlRulesDefinition(sonarRuntimeVersion);
    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    RulesDefinition.Repository repository = context.repository("esql");
    return repository;
  }

  private void assertParameterProperties(Repository repository) {
    // TooManyLinesInFunctionCheck
    Param max = repository.rule("NestedIfDepth").param("maximumNestingLevel");
    assertThat(max).isNotNull();
    assertThat(max.defaultValue()).isEqualTo("5");
    assertThat(max.description()).isEqualTo("Allowed nesting depth");
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
