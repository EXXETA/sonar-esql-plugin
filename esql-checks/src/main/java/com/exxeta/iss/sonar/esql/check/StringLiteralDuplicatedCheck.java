/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.DeclareStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Rule(key = "StringLiteralDuplicated")
public class StringLiteralDuplicatedCheck extends DoubleDispatchVisitorCheck {

    private static final int DEFAULT_THRESHOLD = 3;

    // String literals include quotes, so this means length 5 as defined in RSPEC
    private static final int MINIMAL_LITERAL_LENGTH = 7;

    @RuleProperty(
            key = "threshold",
            description = "Number of times a literal must be duplicated to trigger an issue",
            defaultValue = "" + DEFAULT_THRESHOLD)
    public int threshold = DEFAULT_THRESHOLD;

    private final Multimap<String, LiteralTree> occurrences = ArrayListMultimap.create();
    private final Map<String, DeclareStatementTree> constants = new HashMap<>();


    @Override
    public void visitProgram(ProgramTree tree) {
        occurrences.clear();
        constants.clear();
        super.visitProgram(tree);
        for (String entry : occurrences.keySet()) {
            Collection<LiteralTree> literalTrees = occurrences.get(entry);
            int literalOccurrence = literalTrees.size();
            if (constants.containsKey(entry)) {
                DeclareStatementTree constant = constants.get(entry);
                List<LiteralTree> duplications = literalTrees.stream().filter(literal -> literal.parent() != constant).collect(Collectors.toList());
                PreciseIssue issue = new PreciseIssue(this,
                        new IssueLocation(duplications.iterator().next(), "Use already-defined constant '" + constant.nameList().get(0).name() + "' instead of duplicating its value here."));
                duplications.subList(1, duplications.size()).forEach(element -> issue.secondary(new IssueLocation(element, "Duplication")));
                addIssue(issue);
            } else if (literalOccurrence >= threshold) {
                PreciseIssue issue = new PreciseIssue(this,
                        new IssueLocation(literalTrees.iterator().next(), "Define a constant instead of duplicating this literal " + entry + " " + literalOccurrence + " times."));
                literalTrees.forEach(element -> issue.secondary(new IssueLocation(element, "Duplication")));
                addIssue(issue);
            }
        }

    }

    @Override
    public void visitLiteral(LiteralTree tree) {
        if (tree.is(Tree.Kind.STRING_LITERAL)) {
            String literal = tree.value();
            if (literal.length() >= MINIMAL_LITERAL_LENGTH) {
                occurrences.put(literal, tree);
            }
        }
    }

    @Override
    public void visitDeclareStatement(DeclareStatementTree tree) {
        ExpressionTree initializer = tree.initialValueExpression();
        if (initializer != null && initializer.is(Tree.Kind.STRING_LITERAL)
                && tree.isConstant() && tree.parent().parent().is(Tree.Kind.PROGRAM) && tree.nameList().size() == 1) {
            constants.putIfAbsent(((LiteralTree) initializer).value(), tree);
            return;
        }
        super.visitDeclareStatement(tree);
    }

}
