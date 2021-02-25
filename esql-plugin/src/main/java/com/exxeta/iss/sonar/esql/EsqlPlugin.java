/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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

import org.sonar.api.Plugin;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;

import com.exxeta.iss.sonar.esql.metrics.EsqlMetrics;

public class EsqlPlugin implements Plugin {

	private static final String GENERAL = "General";
	private static final String ESQL_CATEGORY = "Esql";

	// Global ESQL constants
	public static final String PROPERTY_PREFIX = "sonar.esql";

	public static final String IGNORE_HEADER_COMMENTS = PROPERTY_PREFIX + ".ignoreHeaderComments";
	public static final Boolean IGNORE_HEADER_COMMENTS_DEFAULT_VALUE = true;

	public static final String ESQL_EXCLUSIONS_KEY = PROPERTY_PREFIX + ".exclusions";
	public static final String ESQL_EXCLUSIONS_DEFAULT_VALUE = "";

	public static final String TRACE_PATHS_PROPERTY = "sonar.esql.trace.reportPaths";
	public static final String TRACE_PATHS_DEFAULT_VALUE = "target/iibTrace";
	public static final String FILE_SUFFIXES_NAME = "File Suffixes";
	public static final String FILE_SUFFIXES_DESCRIPTION = "List of suffixes for files to analyze.";

	@Override
	public void define(Context context) {
		context.addExtensions(
				EsqlLanguage.class,
				EsqlSensor.class,
				EsqlExclusionsFileFilter.class,
				EsqlRulesDefinition.class,
				EsqlProfilesDefinition.class,
				EsqlMetrics.class);

		  context.addExtensions(
			PropertyDefinition.builder(EsqlLanguage.FILE_SUFFIXES_KEY)
			        .defaultValue(EsqlLanguage.FILE_SUFFIXES_DEFVALUE)
			        .name(FILE_SUFFIXES_NAME)
			        .description(FILE_SUFFIXES_DESCRIPTION)
			        .subCategory(GENERAL)
			        .category(ESQL_CATEGORY)
			        .multiValues(true)
			        .onQualifiers(Qualifiers.PROJECT)
			        .build(),
			      
	      PropertyDefinition.builder(EsqlPlugin.IGNORE_HEADER_COMMENTS)
	        .defaultValue(EsqlPlugin.IGNORE_HEADER_COMMENTS_DEFAULT_VALUE.toString())
	        .name("Ignore header comments")
	        .description("True to not count file header comments in comment metrics.")
	        .onQualifiers(Qualifiers.PROJECT)
	        .subCategory(GENERAL)
	        .category(ESQL_CATEGORY)
	        .type(PropertyType.BOOLEAN)
	        .build(),

	       PropertyDefinition.builder(TRACE_PATHS_PROPERTY)
			      .defaultValue(TRACE_PATHS_DEFAULT_VALUE)
			      .category("Esql")
			      .subCategory("Trace")
			      .name("IIB trace file")
			      .description("Path to the IIB trace files containing coverage data. The path may be absolute or relative to the project base directory.")
			      .onQualifiers(Qualifiers.PROJECT)
			      .multiValues(true)
			      .build(),

		  PropertyDefinition.builder(EsqlPlugin.ESQL_EXCLUSIONS_KEY)
				  .defaultValue(ESQL_EXCLUSIONS_DEFAULT_VALUE)
				  .name("ESQL Exclusions")
				  .description("List of file path patterns to be excluded from analysis of ESQL files.")
				  .onQualifiers(Qualifiers.PROJECT)
				  .subCategory(GENERAL)
				  .multiValues(true)
				  .category(ESQL_CATEGORY)
				  .build()
			);

	}

}
