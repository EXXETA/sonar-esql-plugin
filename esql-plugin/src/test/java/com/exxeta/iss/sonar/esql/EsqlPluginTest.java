package com.exxeta.iss.sonar.esql;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.assertj.core.api.Assertions.assertThat;

public class EsqlPluginTest {

  @Test
  public void count_extensions_for_sonarqube_server_5_6() throws Exception {
    Plugin.Context context = setupContext(SonarRuntimeImpl.forSonarQube(Version.create(5, 6), SonarQubeSide.SERVER));

    assertThat(context.getExtensions()).hasSize(7);
  }

  @Test
  public void should_contain_right_properties_number() throws Exception {
    assertThat(properties()).hasSize(2);
  }

  @Test
  public void should_have_Esql_as_category_for_properties() throws Exception {

    List<PropertyDefinition> properties = properties();

    assertThat(properties).isNotEmpty();

    for (PropertyDefinition propertyDefinition : properties) {
      assertThat(propertyDefinition.category()).isEqualTo("Esql");
    }
  }

  @Test
  public void count_extensions_for_sonarqube_server_6_0() throws Exception {
    Plugin.Context context = setupContext(SonarRuntimeImpl.forSonarQube(Version.create(6, 0), SonarQubeSide.SERVER));

    assertThat(context.getExtensions()).hasSize(7);
  }

  @Test
  public void count_extensions_for_sonarqube_server_6_2() throws Exception {
    Plugin.Context context = setupContext(SonarRuntimeImpl.forSonarQube(Version.create(6, 2), SonarQubeSide.SERVER));

    assertThat(context.getExtensions()).hasSize(7);
  }

  @Test
  public void count_extensions_for_sonarlint() throws Exception {
    Plugin.Context context = setupContext(SonarRuntimeImpl.forSonarLint(Version.create(6, 0)));

    assertThat(context.getExtensions()).hasSize(7);
  }

  private List<PropertyDefinition> properties() {
    List<PropertyDefinition> propertiesList = new ArrayList<>();
    List extensions = setupContext(SonarRuntimeImpl.forSonarQube(Version.create(5, 6), SonarQubeSide.SERVER)).getExtensions();

    for (Object extension : extensions) {
      if (extension instanceof PropertyDefinition) {
        propertiesList.add((PropertyDefinition) extension);
      }
    }

    return propertiesList;
  }

  private Plugin.Context setupContext(SonarRuntime runtime) {
    Plugin.Context context = new Plugin.Context(runtime);
    new EsqlPlugin().define(context);
    return context;
  }

}
