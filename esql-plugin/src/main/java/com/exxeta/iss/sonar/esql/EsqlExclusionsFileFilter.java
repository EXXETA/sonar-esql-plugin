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
package com.exxeta.iss.sonar.esql;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFileFilter;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.WildcardPattern;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class EsqlExclusionsFileFilter implements InputFileFilter {

    private final Configuration configuration;

    private static final Logger LOG = Loggers.get(EsqlExclusionsFileFilter.class);

    public EsqlExclusionsFileFilter(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public boolean accept(InputFile inputFile) {
        return !isExcludedWithProperty(inputFile, EsqlPlugin.ESQL_EXCLUSIONS_KEY);
    }

    private boolean isExcludedWithProperty(InputFile inputFile, String property) {
        String[] excludedPatterns = this.configuration.getStringArray(property);
        String relativePath = inputFile.uri().toString();
        return WildcardPattern.match(WildcardPattern.create(excludedPatterns), relativePath);
    }
}
