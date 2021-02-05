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
package com.exxeta.iss.sonar.esql.check;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import org.sonar.check.Rule;

import java.util.List;

/**
 * this rule checks if there is a blank line before any block or single line comment.
 *
 * @author Sapna Singh
 */
@Rule(key = "BlankLineBeforeComments")
public class BlankLineBeforeCommentsCheck extends DoubleDispatchVisitorCheck {

    private static final String MESSAGE = "Insert one blank line before this block or single-line comment.";

    @Override
    public void visitProgram(ProgramTree tree) {
        EsqlFile file = getContext().getEsqlFile();
        List<String> lines = CheckUtils.readLines(file);

        for (int i = 1; i < lines.size(); i++) {
            if (isComment(lines.get(i)) && !lines.get(i - 1).trim().isEmpty()) {

                addIssue(new LineIssue(this, i + 1, MESSAGE));

            }
        }
    }

    private boolean isComment(String currentline) {
        return (currentline.trim().startsWith("--")) || (currentline.trim().startsWith("/*"));
    }

}
