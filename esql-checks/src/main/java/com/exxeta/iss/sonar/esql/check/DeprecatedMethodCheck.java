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
package com.exxeta.iss.sonar.esql.check;

import java.util.ArrayList;
import java.util.List;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;


/**
 * This Java class is created to implement the logic to avoid the deprecated methods.
 * @author Sapna Singh
 *
 */
@Rule(key = "DeprecatedMethod")
public class DeprecatedMethodCheck extends AbstractDoNotUseFunctionCheck {
	private static final String MESSAGE = "Do not use %s it is deprecated.";

	private static final String DEFAULT_DEPRECATED_METHODS = "BITSTREAM";
	@RuleProperty(
			key = "DeprecatedMethod",
			description = "Deprecated methods should not be used.",
			defaultValue = DEFAULT_DEPRECATED_METHODS)
	public String deprecatedMethods = DEFAULT_DEPRECATED_METHODS;

	
	
	@Override
	public String getMessage(String functionName) {
		return String.format(MESSAGE, functionName);
	}

	@Override
	public List<String> getFunctionNames() {
		List<String> functionNames = new ArrayList<>(); 
		for (String method : deprecatedMethods.split(",")){
			functionNames.add(method.trim());
		}
		return functionNames;
	}


}

