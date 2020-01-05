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

import com.exxeta.iss.sonar.iib.IibPlugin;
import com.exxeta.iss.sonar.iib.esql.EsqlExclusionsFileFilter;
import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.config.internal.MapSettings;

import static org.assertj.core.api.Assertions.assertThat;


public class EsqlExclusionsFileFilterTest {
   

    @Test
    public void should_exclude_using_custom_path_regex() throws Exception {
        MapSettings settings = new MapSettings();
        settings.setProperty(
                IibPlugin.ESQL_EXCLUSIONS_KEY, IibPlugin.ESQL_EXCLUSIONS_DEFAULT_VALUE + "," + "**/libs/**");

        EsqlExclusionsFileFilter filter = new EsqlExclusionsFileFilter(settings.asConfig());

        assertThat(filter.accept(inputFile("some_app.esql"))).isTrue();
        assertThat(filter.accept(inputFile("libs/some_lib.esql"))).isFalse();
    }

    @Test
    public void should_ignore_empty_path_regex() throws Exception {
        MapSettings settings = new MapSettings();
        settings.setProperty(IibPlugin.ESQL_EXCLUSIONS_KEY, "," + IibPlugin.ESQL_EXCLUSIONS_DEFAULT_VALUE + ",");

        EsqlExclusionsFileFilter filter = new EsqlExclusionsFileFilter(settings.asConfig());

        assertThat(filter.accept(inputFile("some_app.esql"))).isTrue();
    }

    private DefaultInputFile inputFile(String file) {
        return new TestInputFileBuilder("test","test_node_modules/" + file)
                .setLanguage(language(file))
                .setContents("foo();")
                .build();
    }

    private static String language(String filename) {
        String[] parts = filename.split("\\.");
        return parts[parts.length - 1];
    }
}
