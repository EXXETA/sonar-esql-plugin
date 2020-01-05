/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.iib.esql;

import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

import com.exxeta.iss.sonar.iib.IibPlugin;

public class EsqlLanguage extends AbstractLanguage {

	public static final String KEY = "esql";

	public static final String FILE_SUFFIXES_KEY = "sonar.esql.file.suffixes";
	public static final String FILE_SUFFIXES_DEFVALUE = ".esql";

	private Configuration configuration;

	public EsqlLanguage(Configuration configuration) {
		super(KEY, "Esql");
		this.configuration = configuration;
	}

	@Override
	public String[] getFileSuffixes() {
		return configuration.getStringArray(FILE_SUFFIXES_KEY);
	}
}
