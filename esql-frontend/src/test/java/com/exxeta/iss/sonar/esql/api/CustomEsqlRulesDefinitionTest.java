package com.exxeta.iss.sonar.esql.api;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Param;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomEsqlRulesDefinitionTest {

  private static final String REPOSITORY_NAME = "Custom Rule Repository";
  private static final String REPOSITORY_KEY = "CustomRuleRepository";

  private static final String RULE_NAME = "This is my custom rule";
  private static final String RULE_KEY = "MyCustomRule";

  @Test
  public void test() {
    MyCustomJavaScriptRulesDefinition rulesDefinition = new MyCustomJavaScriptRulesDefinition();
    RulesDefinition.Context context = new RulesDefinition.Context();
    rulesDefinition.define(context);
    RulesDefinition.Repository repository = context.repository(REPOSITORY_KEY);

    assertThat(repository.name()).isEqualTo(REPOSITORY_NAME);
    assertThat(repository.language()).isEqualTo("esql");
    assertThat(repository.rules()).hasSize(1);

    RulesDefinition.Rule alertUseRule = repository.rule(RULE_KEY);
    assertThat(alertUseRule).isNotNull();
    assertThat(alertUseRule.name()).isEqualTo(RULE_NAME);

    for (RulesDefinition.Rule rule : repository.rules()) {
      for (Param param : rule.params()) {
        assertThat(param.description()).as("description for " + param.key()).isNotEmpty();
      }
    }
  }

  @Rule(
    key = RULE_KEY,
    name = RULE_NAME,
    description = "desc",
    tags = {"bug"})
  public class MyCustomRule extends DoubleDispatchVisitor {
    @RuleProperty(
      key = "customParam",
      description = "Custome parameter",
      defaultValue = "value")
    public String customParam = "value";
  }

  public static class MyCustomJavaScriptRulesDefinition extends CustomEsqlRulesDefinition {

    @Override
    public String repositoryName() {
      System.out.println(REPOSITORY_NAME);
      return REPOSITORY_NAME;
    }

    @Override
    public String repositoryKey() {
      return REPOSITORY_KEY;
    }

    @Override
    public Class[] checkClasses() {
      return new Class[]{MyCustomRule.class};
    }
  }

}
