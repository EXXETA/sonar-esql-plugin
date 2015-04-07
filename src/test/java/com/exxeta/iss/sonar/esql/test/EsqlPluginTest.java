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


import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.PropertyDefinitions;
import org.sonar.api.config.Settings;

import com.exxeta.iss.sonar.esql.EsqlPlugin;

import static org.fest.assertions.Assertions.assertThat;

public class EsqlPluginTest {

  private EsqlPlugin plugin;

  @Before
  public void setUp() throws Exception {
    plugin = new EsqlPlugin();
  }

  @Test
  public void testGetExtensions() throws Exception {
    assertThat(plugin.getExtensions().size()).isEqualTo(8);
  }

  @Test
  public void testProperties() {
    Settings settings = new Settings(new PropertyDefinitions(plugin));
    // SONARPLUGINS-2524
    assertThat(settings.getString(EsqlPlugin.TEST_FRAMEWORK_KEY)).isNull();
  }

}
