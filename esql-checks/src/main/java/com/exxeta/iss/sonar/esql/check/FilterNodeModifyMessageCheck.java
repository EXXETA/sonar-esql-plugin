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

import java.util.List;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.CreateModuleStatementTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.google.common.collect.ImmutableList;

/**
 * @author Sapna Singh
 *
 */
@Rule(key = "FilterNodeModifyMessage")
public class FilterNodeModifyMessageCheck extends DoubleDispatchVisitorCheck {

	private static final String MESSAGE = "The filter node cannot modify the message";
	private boolean insideFilterModule;

	private List<String> rootElements = ImmutableList.of("Root", "InputRoot", "OutputRoot");

	@Override
	public void visitCreateModuleStatement(CreateModuleStatementTree tree) {
		if ("FILTER".equalsIgnoreCase(tree.moduleType().text())){
			this.insideFilterModule=true;
		}
		super.visitCreateModuleStatement(tree);
		this.insideFilterModule=false;
	}
	
	@Override
	public void visitSetStatement(SetStatementTree tree) {
		if (this.insideFilterModule && tree.variableReference() instanceof FieldReferenceTree) {
			FieldReferenceTree fieldReference = (FieldReferenceTree) tree.variableReference();
			if (rootElements.contains(fieldReference.pathElement().name().name().name())) {
				addIssue(new LineIssue(this, tree, MESSAGE));
			}

		}

		super.visitSetStatement(tree);
	}
}
