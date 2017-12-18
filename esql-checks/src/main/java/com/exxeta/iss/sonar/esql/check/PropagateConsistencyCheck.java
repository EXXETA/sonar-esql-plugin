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
package com.exxeta.iss.sonar.esql.check;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.internal.apachecommons.codec.digest.MessageDigestAlgorithms;
import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.tree.statement.BeginEndStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateFunctionStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateProcedureStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.PropagateStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.ReturnStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.StatementsTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.IssueLocation;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;
import com.exxeta.iss.sonar.msgflow.model.MessageFlow;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowNode;
import com.exxeta.iss.sonar.msgflow.model.MessageFlowParser;

@Rule(key = "PropagateConsistency")
public class PropagateConsistencyCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "Compute node connections are inconsistent";

	@Override
	public void visitCreateModuleStatement(CreateModuleStatementTree tree) {
		super.visitCreateModuleStatement(tree);
		String moduleName = tree.moduleName().name();
		EsqlFile esqlFile = getContext().getEsqlFile();
		File file = new File(esqlFile.relativePath());
		boolean parentFound = false;
		File parentFile = new File(esqlFile.relativePath());
		while(!parentFound) {
			parentFile = parentFile.getParentFile();
			if(parentFile.getName().matches("^[a-zA-Z]*(_App_v|_Lib_v)[0-9]")) {
				parentFound = true;
				parentFile = parentFile.getParentFile();
			}
		}
		MessageFlow msgFlow = null;
		MessageFlowNode msgFlownode = null;
		boolean msgFlowFound = false;
		for(File msgflowFile : getMsgFlowFiles(parentFile.listFiles())) {
			msgFlow = new MessageFlow(msgflowFile.getAbsolutePath(), new MessageFlowParser());
			for(MessageFlowNode node : msgFlow.getComputeNodes()) {
				msgFlownode = node;
				if(((String)node.getProperties().get("computeExpression")).equals(moduleName)) {
					msgFlowFound = true;
					break;
				}
			}
			if(msgFlowFound) {
				break;
			}
			
		}
		
		
		List<StatementTree> statementList = tree.moduleStatementsList();
		for (StatementTree stat : statementList) {
			if (stat.is(Kind.CREATE_FUNCTION_STATEMENT)) {
				CreateFunctionStatementTree createFunction = (CreateFunctionStatementTree) stat;
				BeginEndStatementTree beginEnd = (BeginEndStatementTree) createFunction.routineBody().statement();
				StatementsTree statements = beginEnd.statements();
				for (StatementTree funcStat : statements.statements()) {
					if (funcStat.is(Kind.PROPAGATE_STATEMENT)) {
						PropagateStatementTree propagateStatement = (PropagateStatementTree) funcStat;
						if ("TERMINAL".equalsIgnoreCase(propagateStatement.targetType().text())) {
							String terminal = getTerminalName(propagateStatement.target().toString());
							if(!msgFlownode.getOutputTerminals().contains("OutTerminal."+terminal)) {
								addIssue(tree, MESSAGE);
							}
						}
					} else if (funcStat.is(Kind.RETURN_STATEMENT)) {
						ReturnStatementTree retStatement = (ReturnStatementTree) funcStat;
						if(retStatement.expression().toString().equalsIgnoreCase("true")){
							if(!msgFlownode.getOutputTerminals().contains("OutTerminal.out")) {
								addIssue(tree, MESSAGE);
							}
						}
					}
				}
			} else if (stat.is(Kind.CREATE_PROCEDURE_STATEMENT)) {
				CreateProcedureStatementTree createFunction = (CreateProcedureStatementTree) stat;
				BeginEndStatementTree beginEnd = (BeginEndStatementTree) createFunction.routineBody().statement();
				StatementsTree statements = beginEnd.statements();
				for (StatementTree funcStat : statements.statements()) {
					if (funcStat.is(Kind.PROPAGATE_STATEMENT)) {
						PropagateStatementTree propagateStatement = (PropagateStatementTree) funcStat;
						if ("TERMINAL".equalsIgnoreCase(propagateStatement.targetType().text())) {
							String terminal = getTerminalName(propagateStatement.target().toString());
							if(!msgFlownode.getOutputTerminals().contains("OutTerminal."+terminal)) {
								addIssue(tree, MESSAGE);
							}
						}
					} else if (funcStat.is(Kind.RETURN_STATEMENT)) {
						ReturnStatementTree retStatement = (ReturnStatementTree) funcStat;
						if(retStatement.expression().toString().equalsIgnoreCase("true")){
							if(!msgFlownode.getOutputTerminals().contains("OutTerminal.out")) {
								addIssue(tree, MESSAGE);
							}
						}
					}
				}
			}
		}
		
		
	}

	public static ArrayList<File> getMsgFlowFiles(File[] files) {
		ArrayList<File> fileList = new ArrayList<File>();
		for (File tmpFile : files) {
			if (tmpFile.isDirectory()) {
				fileList.addAll(getMsgFlowFiles(tmpFile.listFiles()));
			} else {
				if (tmpFile.getAbsolutePath().endsWith(".msgflow")) {
					fileList.add(tmpFile);
				}
			}
		}
		return fileList;
	}
	
	public static String getTerminalName(String target) {
		String terminal = "";
		if(StringUtils.isNumeric(target)) {
			int terminalNo = Integer.parseInt(target);
			switch(terminalNo) {
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
				terminal = "out"+terminalNo;
				break;
			default:
				terminal = "nowhere";
			}
			return terminal;
		}
		else {
			return target;
		}
	}
}