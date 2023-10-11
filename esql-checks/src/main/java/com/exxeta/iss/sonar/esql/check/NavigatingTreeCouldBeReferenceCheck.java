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


import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.PathElementTree;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang.StringUtils;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This java class is created to implement the logic for reference check, Navigating message tree could be replaced by a reference.
 *
 * @author Sapna  singh
 */
@Rule(key = "NavigatingTreeCouldBeReference")
public class NavigatingTreeCouldBeReferenceCheck extends DoubleDispatchVisitorCheck {

    private static final String MESSAGE = "Navigating message tree could be replaced by a reference.";

    private static final int DEFAULT_ALLOWED_NUMBER_OF_FIELD_REFERENCE_USES = 2;
    @RuleProperty(
            key = "AllowedNumberOfReferences",
            description = "The maximum allowed number of reused field references. Issues will only created if a field reference is used more often.",
            defaultValue = "" + DEFAULT_ALLOWED_NUMBER_OF_FIELD_REFERENCE_USES)
    public int allowedNumberOfFieldReferenceUses = DEFAULT_ALLOWED_NUMBER_OF_FIELD_REFERENCE_USES;

    private static final int DEFAULT_MAXIMUM_ALLOWED_PATH_ELEMENTS = 2;
    @RuleProperty(
            key = "MaximumAllowedPathElements",
            description = "The number of allowed of path elements in a field reference for which no reference will be suggested. Issues will only be created for field references with more path elements.",
            defaultValue = "" + DEFAULT_MAXIMUM_ALLOWED_PATH_ELEMENTS)
    public int maximumAllowedPathElements = DEFAULT_MAXIMUM_ALLOWED_PATH_ELEMENTS;

    private final Multimap<String, FieldReferenceTree> occurrences = ArrayListMultimap.create();

    @Override
    public void visitFieldReference(FieldReferenceTree tree) {

        if (tree.pathElements() != null && !tree.pathElements().isEmpty()) {
            StringBuilder asString = new StringBuilder(tree.pathElement().toString());
            for (PathElementTree element : tree.pathElements()) {
                asString.append(".");
                asString.append(element.toString());
                occurrences.put(asString.toString(), tree);
            }
        }

        super.visitFieldReference(tree);
    }

    @Override
    public void visitProgram(ProgramTree tree) {

        occurrences.clear();

        super.visitProgram(tree);

        List<String> sortedKeys = occurrences.keySet().stream()
                .filter(a -> StringUtils.countMatches(a, ".") + 1 > maximumAllowedPathElements)
                .sorted((k1, k2) -> StringUtils.countMatches(k2, ".") - StringUtils.countMatches(k1, "."))
                .collect(Collectors.toList());

        ArrayList<String> issueKeys = new ArrayList<>();

        for (String entry : sortedKeys) {
            Collection<FieldReferenceTree> fieldReferenceTrees = occurrences.get(entry);
            if (fieldReferenceTrees.size() > allowedNumberOfFieldReferenceUses) {
                boolean alreadyIssued = false;
                for (String issueKey : issueKeys) {
                    if (issueKey.startsWith(entry + ".")) {
                        alreadyIssued = true;
                    }
                }
                if (!alreadyIssued) {
                    PreciseIssue issue = new PreciseIssue(this,
                            new IssueLocation(fieldReferenceTrees.iterator().next(), MESSAGE));
                    fieldReferenceTrees.forEach(element -> issue.secondary(new IssueLocation(element)));
                    addIssue(issue);
                    issueKeys.add(entry);
                }
            }

        }

    }


}
