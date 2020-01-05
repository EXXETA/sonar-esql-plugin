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

import com.exxeta.iss.sonar.esql.api.tree.RoutineDeclarationTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxTrivia;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import org.apache.commons.lang.StringUtils;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

/**
 * This Java class is created to check function or procedure header comments
 *
 * @author
 */
@Rule(key = "RoutineComments")
public class RoutineCommentsCheck extends DoubleDispatchVisitorCheck {


    private static final String MESSAGE = "Document this %s to match the regular expression %s.";

    private static final String DEFAULT_FORMAT = "[\\s\\S]*(Parameters|IN|INOUT|OUT|RETURNS):[\\s\\S]*";

    @RuleProperty(key = "format",
            description = "regular expression",
            defaultValue = "" + DEFAULT_FORMAT)
    public String format = DEFAULT_FORMAT;

    @Override
    public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
        visitRoutine(tree, String.format(MESSAGE, "function", format));
        super.visitCreateFunctionStatement(tree);

    }

    @Override
    public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
        visitRoutine(tree, String.format(MESSAGE, "procedure", format));
        super.visitCreateProcedureStatement(tree);
    }

    private void visitRoutine(RoutineDeclarationTree tree, String message) {
        String comment = getComment(tree);

        // check comments
        if (isEmptyComment(comment) || !comment.matches(format)) {
            addIssue(tree.firstToken(), message);
        }
    }

    private static String getComment(Tree tree) {
        for (SyntaxTrivia syntaxTrivia : tree.firstToken().trivias()) {
            if (syntaxTrivia.text().startsWith("/*")) {
                return syntaxTrivia.text();
            }
        }

        return null;
    }


    private static boolean isEmptyComment(String comment) {
        if (comment == null) {
            return true;
        }
        // remove start and end of doc as well as stars.
        String cleanedUpJavadoc = comment.trim().substring(2).replace("*/", "").replace("*", "").trim();
        return StringUtils.isBlank(cleanedUpJavadoc);
    }

}
