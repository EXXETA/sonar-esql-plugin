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

import com.exxeta.iss.sonar.esql.colorizer.EsqlColorizerFormat;
import com.exxeta.iss.sonar.esql.core.Esql;
import com.exxeta.iss.sonar.esql.core.EsqlSourceImporter;
import com.exxeta.iss.sonar.esql.cpd.EsqlCpdMapping;
import com.google.common.collect.ImmutableList;

import org.sonar.api.Extension;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.SonarPlugin;

import java.util.List;

@Properties({
  // Global Esql settings
  @Property(
    key = EsqlPlugin.FILE_SUFFIXES_KEY,
    defaultValue = EsqlPlugin.FILE_SUFFIXES_DEFVALUE,
    name = "File suffixes",
    description = "Comma-separated list of suffixes for files to analyze.",
    global = true,
    project = true)
 

})
public class EsqlPlugin extends SonarPlugin {

  public List<Class<? extends Extension>> getExtensions() {
    return ImmutableList.of(
        Esql.class,
        EsqlSourceImporter.class,
        EsqlColorizerFormat.class,
        EsqlProfile.class,
        EsqlCpdMapping.class,
        EsqlRuleRepository.class,
        EsqlCommonRulesEngineProvider.class,
        EsqlSquidSensor.class
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
