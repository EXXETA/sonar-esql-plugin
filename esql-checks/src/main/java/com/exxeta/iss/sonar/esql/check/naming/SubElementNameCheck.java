/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	 http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.check.naming;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import org.sonar.check.Rule;

/**
 * This java class is created to implement the logic to check sub-elements
 * should be in UpperCamel-case and elements containing simple value should be
 * in lowercase.
 *
 * @author sapna singh
 */
@Rule(key = "SubElementName")
public class SubElementNameCheck extends DoubleDispatchVisitorCheck {

    private static final String MESSAGE = "sub-elements should be in UpperCamel-case and elements containing simple value should be in lowercase.";

    private static final String UPPERCASE_FORMAT = "^[A-Z][a-zA-Z0-9]*$";
    private static final String LOWERCASE_FORMAT = "^[a-z][a-zA-Z0-9]*$";

    @Override
    public void visitSetStatement(SetStatementTree tree) {
        boolean isValid = true;
        if (tree.variableReference() instanceof FieldReferenceTree) {
            FieldReferenceTree fieldRef = (FieldReferenceTree) tree.variableReference();
            if ("Environment".equalsIgnoreCase(fieldRef.pathElement().name().name().name())) {
                int subElementsSize = fieldRef.pathElements().size();
                if (subElementsSize > 0) {
                    for (int i = 0; i < subElementsSize - 1; i++) {
                        if (!matches(fieldRef.pathElements().get(i), UPPERCASE_FORMAT)) {
                            isValid = false;
                        }
                    }
                    PathElementTree lastElement = fieldRef.pathElements().get(subElementsSize - 1);
                    if (!matches(lastElement, LOWERCASE_FORMAT)) {
                        isValid = false;
                    }

                }

            }
        }
        if (!isValid) {
            addIssue(tree, MESSAGE);
        }
    }


    private boolean matches(PathElementTree element, String format) {
        if (element.name()!=null && element.name().name() != null && element.name().name().name() != null) {
            return element.name().name().name().matches(format);
        } else {
            return true;
        }
    }
}
