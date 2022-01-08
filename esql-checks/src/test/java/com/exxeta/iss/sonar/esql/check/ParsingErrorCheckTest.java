package com.exxeta.iss.sonar.esql.check;

import com.exxeta.iss.sonar.esql.parser.EsqlGrammar;
import com.exxeta.iss.sonar.esql.parser.EsqlLegacyGrammar;
import com.exxeta.iss.sonar.esql.parser.EsqlNodeBuilder;
import com.exxeta.iss.sonar.esql.parser.TreeFactory;
import com.google.common.base.Charsets;
import com.sonar.sslr.api.RecognitionException;
import com.sonar.sslr.api.typed.ActionParser;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParsingErrorCheckTest {
    @Test
    void test() {
        String relativePath = "src/test/resources/parsingError.esql";
        ActionParser parser = new ActionParser<>(Charsets.UTF_8, EsqlLegacyGrammar.createGrammarBuilder(), EsqlGrammar.class,
                new TreeFactory(), new EsqlNodeBuilder(), EsqlLegacyGrammar.PROGRAM);
        RecognitionException thrown = assertThrows(RecognitionException.class, () -> {
            parser.parse(new File(relativePath));
        });
        assertThat(thrown.getMessage()).contains("Parse error");

    }

}
