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
import org.sonar.api.config.Settings;

import com.exxeta.iss.sonar.esql.EsqlPlugin;
import com.exxeta.iss.sonar.esql.core.Esql;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

public class EsqlTest {

  private Settings settings;
  private Esql esql;

  @Before
  public void setUp() {
    settings = new Settings();
    esql = new Esql(settings);
  }

  @Test
  public void defaultSuffixes() {
    settings.setProperty(EsqlPlugin.FILE_SUFFIXES_KEY, "");
    assertArrayEquals(esql.getFileSuffixes(), new String[] {"esql"});
    assertArrayEquals(esql.getFileSuffixes(), new String[] {"esql"});
    assertSame(settings, esql.getSettings());
  }

  @Test
  public void customSuffixes() {
    settings.setProperty(EsqlPlugin.FILE_SUFFIXES_KEY, "esql");
    assertArrayEquals(esql.getFileSuffixes(), new String[] {"esql"});
  }

}
