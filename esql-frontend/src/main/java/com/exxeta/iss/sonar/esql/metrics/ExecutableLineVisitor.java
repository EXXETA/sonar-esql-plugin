/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.metrics;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.collect.ImmutableList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExecutableLineVisitor extends SubscriptionVisitorCheck {

  private final Set<Integer> executableLines = new HashSet<>();

  public ExecutableLineVisitor(Tree tree) {
    scanTree(tree);
  }

  @Override
  public List<Kind> nodesToVisit() {
    return ImmutableList.of(
    		Kind.BEGIN_END_STATEMENT,
    		Kind.CALL_STATEMENT,
    		Kind.CASE_STATEMENT,
    		Kind.DECLARE_STATEMENT,
    		Kind.IF_STATEMENT,
    		Kind.ITERATE_STATEMENT,
    		Kind.LEAVE_STATEMENT,
    		Kind.LOOP_STATEMENT,
    		Kind.PROPAGATE_STATEMENT,
    		Kind.REPEAT_STATEMENT,
    		Kind.RETURN_STATEMENT,
    		Kind.SET_STATEMENT,
    		Kind.CREATE_FUNCTION_STATEMENT,
    		Kind.CREATE_MODULE_STATEMENT,
    		Kind.CREATE_PROCEDURE_STATEMENT);
  }

  @Override
  public void visitNode(Tree tree) {
    executableLines.add(((EsqlTree) tree).getLine());
  }

  public Set<Integer> getExecutableLines() {
    return executableLines;
  }

}
