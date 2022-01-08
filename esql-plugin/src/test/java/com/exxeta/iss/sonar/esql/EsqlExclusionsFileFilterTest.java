package com.exxeta.iss.sonar.esql;

import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.config.internal.MapSettings;

import static org.assertj.core.api.Assertions.assertThat;


class EsqlExclusionsFileFilterTest {
   

    @Test
    void should_exclude_using_custom_path_regex() throws Exception {
        MapSettings settings = new MapSettings();
        settings.setProperty(
                EsqlPlugin.ESQL_EXCLUSIONS_KEY, EsqlPlugin.ESQL_EXCLUSIONS_DEFAULT_VALUE + "," + "**/libs/**");

        EsqlExclusionsFileFilter filter = new EsqlExclusionsFileFilter(settings.asConfig());

        assertThat(filter.accept(inputFile("some_app.esql"))).isTrue();
        assertThat(filter.accept(inputFile("libs/some_lib.esql"))).isFalse();
    }

    @Test
    void should_ignore_empty_path_regex() throws Exception {
        MapSettings settings = new MapSettings();
        settings.setProperty(EsqlPlugin.ESQL_EXCLUSIONS_KEY, "," + EsqlPlugin.ESQL_EXCLUSIONS_DEFAULT_VALUE + ",");

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
