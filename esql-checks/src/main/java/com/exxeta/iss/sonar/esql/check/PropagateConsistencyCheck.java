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

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.expression.ExpressionTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PropagateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.tree.expression.LiteralTree;
import com.exxeta.iss.sonar.esql.tree.impl.expression.PrefixExpressionTreeImpl;
import com.exxeta.iss.sonar.esql.tree.impl.statement.CreateRoutineTreeImpl;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowParser;
import org.apache.commons.lang.math.NumberUtils;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Rule(key = "PropagateConsistency")
public class PropagateConsistencyCheck extends DoubleDispatchVisitorCheck {

    private static final String MESSAGE = "Compute node connections are inconsistent";
    private static final Logger LOG = Loggers.get(PropagateConsistencyCheck.class);

    private MessageFlowNode msgFlownode = null;

    private CreateRoutineTreeImpl currentRoutine;

    @Override
    public void visitCreateModuleStatement(CreateModuleStatementTree tree) {
        String moduleName = tree.moduleName().name();
        EsqlFile esqlFile = getContext().getEsqlFile();
        Path projectDirectory = getProjectDirectory(esqlFile);

        if (projectDirectory != null) {

            msgFlownode = getMsgFlowNode(moduleName, projectDirectory);
        }
        super.visitCreateModuleStatement(tree);
        msgFlownode = null;

    }

    private MessageFlowNode getMsgFlowNode(String moduleName, Path projectDirectory) {
        for (MessageFlow msgFlow : getMsgFlowFiles(projectDirectory)) {
            for (MessageFlowNode node : msgFlow.getComputeNodes()) {
                if (((String) node.getProperties().get("computeExpression")).equals(moduleName)) {
                    return node;
                }
            }
        }
        return null;
    }

    private Path getProjectDirectory(EsqlFile esqlFile) {
        File projectDirectory = new File(new File(esqlFile.relativePath()).getAbsolutePath());
        if (!projectDirectory.isDirectory()) {
            projectDirectory = projectDirectory.getParentFile();
        }
        while (projectDirectory != null) {
            LOG.debug("Checking " + projectDirectory.getAbsolutePath());
            if (new File(projectDirectory, ".project").exists()) {
                LOG.info("Project directory " + projectDirectory.getAbsolutePath());
                return projectDirectory.toPath();
            }
            projectDirectory = projectDirectory.getParentFile();
        }
        LOG.info("No .project file found");
        return null;
    }

    @Override
    public void visitCreateFunctionStatement(CreateFunctionStatementTree tree) {
        currentRoutine = (CreateRoutineTreeImpl) tree;
        super.visitCreateFunctionStatement(tree);
        currentRoutine = null;
    }

    @Override
    public void visitCreateProcedureStatement(CreateProcedureStatementTree tree) {
        currentRoutine = (CreateRoutineTreeImpl) tree;
        super.visitCreateProcedureStatement(tree);
        currentRoutine = null;
    }

    @Override
    public void visitReturnStatement(ReturnStatementTree tree) {
        if (currentRoutine instanceof CreateFunctionStatementTree // must be a function
                && "MAIN".equalsIgnoreCase(currentRoutine.identifier().name()) // must
                // be
                // the
                // main
                // function
                && "true".equalsIgnoreCase(tree.expression().toString())) { // must return TRUE
            checkTerminal("OutTerminal.out", tree);
        }

        super.visitReturnStatement(tree);
    }

    private void checkTerminal(String terminalName, Tree tree) {
        if (msgFlownode != null // msgFlow needs to be found
                && !msgFlownode.getOutputTerminals().contains(terminalName)) {
            addIssue(tree, MESSAGE);

        }

    }

    @Override
    public void visitPropagateStatement(PropagateStatementTree propagateStatement) {
        if (propagateStatement.targetType() == null) {
            checkTerminal("OutTerminal.out", propagateStatement);
        } else if ("TERMINAL".equalsIgnoreCase(propagateStatement.targetType().text())) {
            String terminalName = getTerminalName(propagateStatement.target());
            if (terminalName != null) {
                checkTerminal("OutTerminal." + terminalName, propagateStatement);
            }
        }

        super.visitPropagateStatement(propagateStatement);
    }

    private static List<MessageFlow> getMsgFlowFiles(Path directory) {
        try (Stream<Path> walk = Files.walk(directory)) {
            return walk.filter(Files::isRegularFile)
                    .filter(f -> f.toString().endsWith(".msgflow"))
                    .map(f -> new MessageFlow(f.toFile().getAbsolutePath(), new MessageFlowParser()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOG.error("Cannot list files", e);
            return Collections.emptyList();
        }
    }

    private static String getTerminalName(ExpressionTree expression) {
        String target = null;
        if (expression instanceof LiteralTree) {
            target = ((LiteralTree) expression).value().replace("'", "");
        } else if (expression instanceof PrefixExpressionTreeImpl) {
            PrefixExpressionTreeImpl prefixEx = (PrefixExpressionTreeImpl) expression;
            target = prefixEx.operator().text() + prefixEx.expression().toString();
        }

        String terminal = "";


        if (NumberUtils.isNumber(target)) {
            int terminalNo = Integer.parseInt(target);
            switch (terminalNo) {
                case -2:
                    terminal = "nowhere";
                    break;
                case -1:
                    terminal = "failure";
                    break;
                case 0:
                    terminal = "out";
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    terminal = "out" + terminalNo;
                    break;
                default:
                    terminal = "nowhere";
            }
            return terminal;
        } else {
            return target;
        }
    }

}
