/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import org.sonar.check.Rule;

@Rule(key = UseBrokerSchemaCheck.CHECK_KEY)
public class UseBrokerSchemaCheck extends DoubleDispatchVisitorCheck {
    public static final String CHECK_KEY = "UseBrokerSchema";
    public static final String MESSAGE = "The default Schema should not be used. Move the file to a broker schema.";

    @Override
    public void visitProgram(ProgramTree tree) {

        if (tree.brokerSchemaStatement() == null) {

            addIssue(new FileIssue(this, MESSAGE));
        }

        super.visitProgram(tree);

    }

}
