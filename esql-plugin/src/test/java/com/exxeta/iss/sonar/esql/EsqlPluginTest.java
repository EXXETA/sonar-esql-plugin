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

    assertThat(context.getExtensions()).hasSize(9);
  }

  @Test
  public void should_contain_right_properties_number() throws Exception {
    assertThat(properties()).hasSize(4);
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

    assertThat(context.getExtensions()).hasSize(9);
  }

  @Test
  public void count_extensions_for_sonarqube_server_6_2() throws Exception {
    Plugin.Context context = setupContext(SonarRuntimeImpl.forSonarQube(Version.create(6, 2), SonarQubeSide.SERVER));

    assertThat(context.getExtensions()).hasSize(9);
  }

  @Test
  public void count_extensions_for_sonarlint() throws Exception {
    Plugin.Context context = setupContext(SonarRuntimeImpl.forSonarLint(Version.create(6, 0)));

    assertThat(context.getExtensions()).hasSize(9);
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
