package com.exxeta.iss.sonar.esql.check;

import java.io.File;
import java.io.FilenameFilter;

import org.junit.jupiter.api.Test;

import com.exxeta.iss.sonar.esql.parser.EsqlGrammar;
import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;
import com.exxeta.iss.sonar.esql.parser.EsqlNodeBuilder;
import com.exxeta.iss.sonar.esql.parser.TreeFactory;
import com.google.common.base.Charsets;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.api.typed.ActionParser;

class ParserTest {

    @Test
    void parseAllFiles() {

        String path = "src/test/resources/";
        File baseDir = new File(path);
        for (File esql : baseDir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".esql") && !"parsingError.esql".equals(name);
            }
        }))

            parse(esql);

    }

    private void parse(File esql) {
        ActionParser parser = new ActionParser<>(Charsets.UTF_8, EsqlLegacyGrammar.createGrammarBuilder(), EsqlGrammar.class,
                new TreeFactory(), new EsqlNodeBuilder(), EsqlLegacyGrammar.PROGRAM);
        try {
            parser.parse(esql);
        } catch (RecognitionException e) {
            System.err.println("Cannot parse " + esql.toPath());
            throw e;
        }


    }

}
