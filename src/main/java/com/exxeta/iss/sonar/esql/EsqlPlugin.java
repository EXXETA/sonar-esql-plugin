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

package com.exxeta.iss.sonar.esql;

import java.util.List;

import org.sonar.api.SonarPlugin;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import com.exxeta.iss.sonar.esql.colorizer.EsqlColorizerFormat;
import com.exxeta.iss.sonar.esql.colorizer.EsqlHighlighter;
import com.exxeta.iss.sonar.esql.core.Esql;
import com.exxeta.iss.sonar.esql.core.EsqlSourceImporter;
import com.exxeta.iss.sonar.esql.cpd.EsqlCpdMapping;
import com.google.common.collect.ImmutableList;

public class EsqlPlugin extends SonarPlugin {

	@Override
  public List  getExtensions() {
    return ImmutableList.of(
    		PropertyDefinition.builder(FILE_SUFFIXES_KEY).name("File Suffixes")
    		.description("Comma-separated list of suffixes of Esql files to analyze.")
    		.category("Esql")
    		.onQualifiers(Qualifiers.PROJECT)
    		.defaultValue("esql")
    		.build(),
        Esql.class,
        EsqlSourceImporter.class,
        //EsqlColorizerFormat.class,
        EsqlProfile.class,
        EsqlCpdMapping.class,
        EsqlRuleRepository.class,
        EsqlCommonRulesEngine.class,
        EsqlSquidSensor.class,
        EsqlHighlighter.class
);
  }

  // Global ESQL constants
  public static final String FALSE = "false";

  public static final String FILE_SUFFIXES_KEY = "sonar.esql.file.suffixes";
  public static final String FILE_SUFFIXES_DEFVALUE = "esql";

  public static final String PROPERTY_PREFIX = "sonar.esql";
  public static final String TEST_FRAMEWORK_KEY = PROPERTY_PREFIX + ".testframework";
  public static final String TEST_FRAMEWORK_DEFAULT = "";

}
