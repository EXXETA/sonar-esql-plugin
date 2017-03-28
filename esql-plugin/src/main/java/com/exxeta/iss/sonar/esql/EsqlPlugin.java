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

import org.sonar.api.Plugin;

public class EsqlPlugin implements Plugin {


  // Global ESQL constants
  public static final String FALSE = "false";

  public static final String FILE_SUFFIXES_KEY = "sonar.esql.file.suffixes";
  public static final String FILE_SUFFIXES_DEFVALUE = ".esql";

  public static final String PROPERTY_PREFIX = "sonar.esql";
  public static final String TEST_FRAMEWORK_KEY = PROPERTY_PREFIX + ".testframework";
  public static final String TEST_FRAMEWORK_DEFAULT = "";
@Override
public void define(Context context) {
    context.addExtensions(
    	      EsqlLanguage.class,
    	      EsqlSquidSensor.class,
    	      new EsqlRulesDefinition(context.getSonarQubeVersion()),
    	      EsqlProfile.class);
	
}

}
