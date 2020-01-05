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
package com.exxeta.iss.sonar.iib.msgflow;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Configuration;
import org.sonar.api.resources.AbstractLanguage;

import com.exxeta.iss.sonar.iib.IibPlugin;

public class MsgflowLanguage extends AbstractLanguage {

	public static final String KEY = "msgflow";
	public static final String FILE_SUFFIXES_KEY = "sonar.msgflow.file.suffixes";
	public static final String FILE_SUFFIXES_DEFVALUE = ".msgflow,.subflow";

	private  Configuration configuration;

	public MsgflowLanguage(Configuration configuration) {
		super(KEY, "Msgflow");
		this.configuration = configuration;
	}

	@Override
	public String[] getFileSuffixes() {
		String[] suffixes = configuration.getStringArray(FILE_SUFFIXES_KEY);
		if (suffixes == null || suffixes.length == 0) {
			suffixes = StringUtils.split(FILE_SUFFIXES_DEFVALUE, ",");
		}
		return suffixes;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
