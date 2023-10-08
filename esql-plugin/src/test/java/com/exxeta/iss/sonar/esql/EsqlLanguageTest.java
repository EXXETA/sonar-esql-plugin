/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sonar.api.config.internal.MapSettings;

class EsqlLanguageTest {
	  private MapSettings settings;
	  private EsqlLanguage esqlLanguage;

	  @BeforeEach
	  void setUp() {
	    settings = new MapSettings();
	    esqlLanguage = new EsqlLanguage(settings.asConfig());
	  }

	  @Test
	  void defaultSuffixes() {
		  MapSettings mapSettings = new MapSettings();
		  mapSettings.setProperty(EsqlLanguage.FILE_SUFFIXES_KEY, EsqlLanguage.FILE_SUFFIXES_DEFVALUE);
		  EsqlLanguage esqlLanguage = new EsqlLanguage(mapSettings.asConfig());
		  assertThat(esqlLanguage.getFileSuffixes()).containsOnly(".esql");

	  }

	  @Test
	  void customSuffixes() {

		  MapSettings mapSettings = new MapSettings();
		  mapSettings.setProperty(EsqlLanguage.FILE_SUFFIXES_KEY, "esql");
		  EsqlLanguage esqlLanguage = new EsqlLanguage(mapSettings.asConfig());
		  assertThat(esqlLanguage.getFileSuffixes()).containsOnly("esql");

	  }

}
