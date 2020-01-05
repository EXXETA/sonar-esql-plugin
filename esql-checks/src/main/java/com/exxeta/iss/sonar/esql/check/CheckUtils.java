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
package com.exxeta.iss.sonar.esql.check;

import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

public class CheckUtils {
    private CheckUtils() {
    }


    public static List<String> readLines(EsqlFile file) {
        try (BufferedReader reader = newBufferedReader(file)) {
            return reader.lines().collect(Collectors.toList());

        } catch (IOException e) {
            throw new IllegalStateException("Unable to read file " + file.toString(), e);
        }
    }

    private static BufferedReader newBufferedReader(EsqlFile file) throws IOException {
        return new BufferedReader(new StringReader(file.contents()));
    }

    /* changes Strats from here  (Sapna Singh) */


    public static String removeQuotedContent(String s) {
        String res = removeQuotedContentByChar(s, '\'');
        res = removeQuotedContentByChar(res, '"');
        return res;
    }

    public static String removeQuotedContentByChar(String s, char c) {
        StringBuilder removeQuotedComment = new StringBuilder();
        boolean quote = false;
        for (int i = 0; i < s.length(); i++) {
            if (!quote) {
                if (s.charAt(i) == c)
                    quote = true;
                removeQuotedComment.append(s.charAt(i));
                continue;
            }
            if (s.charAt(i) == c) {
                quote = false;
                removeQuotedComment.append(s.charAt(i));
            }
        }

        return removeQuotedComment.toString();
    }





    /* changes Ends here  (sapna singh) */

}
