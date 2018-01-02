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
package com.exxeta.iss.sonar.esql.codecoverage;

import java.util.List;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import com.google.common.collect.ImmutableList;

public class CodeCoverageExtension {

	  public static final Logger LOG = Loggers.get(CodeCoverageExtension.class.getName());

	  private CodeCoverageExtension(){}

	  public static List<Object> getExtensions() {
		    ImmutableList.Builder<Object> extensions = ImmutableList.builder();

		    extensions.add(
		    		TraceSensor.class);
		    
		    return extensions.build();

	  }

}
