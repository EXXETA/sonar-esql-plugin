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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.internal.MapSettings;

public class MsgflowLanguageTest {
	  private MapSettings settings;
	  private MsgflowLanguage msgflowLanguage;

	  @Before
	  public void setUp() {
	    settings = new MapSettings();
	    msgflowLanguage = new MsgflowLanguage(settings.asConfig());
	  }

	  @Test
	  public void defaultSuffixes() {
	    settings.setProperty(EsqlPlugin.MSGFLOW_FILE_SUFFIXES_KEY, "");
	    assertThat(msgflowLanguage.getFileSuffixes()).containsOnly(".msgflow", ".subflow");
	  }

	  @Test
	  public void customSuffixes() {
	    settings.setProperty(EsqlPlugin.MSGFLOW_FILE_SUFFIXES_KEY, "abc");
	    assertThat(msgflowLanguage.getFileSuffixes()).containsOnly("abc");
	  }

}
