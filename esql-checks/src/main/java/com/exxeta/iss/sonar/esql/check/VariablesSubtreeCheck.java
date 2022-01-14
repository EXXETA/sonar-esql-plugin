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

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.FieldReferenceTree;
import com.exxeta.iss.sonar.esql.api.tree.statement.SetStatementTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;

@Rule(key="VariablesSubtree")
public class VariablesSubtreeCheck extends DoubleDispatchVisitorCheck{

	@Override
	public void visitSetStatement(SetStatementTree tree) {
		if (tree.variableReference() instanceof FieldReferenceTree){
			FieldReferenceTree fieldRef = (FieldReferenceTree)tree.variableReference();
			if ("Environment".equalsIgnoreCase(fieldRef.pathElement().name().name().name()) ){
				
				if (fieldRef.pathElements().isEmpty()
					|| fieldRef.pathElements().get(0).name()==null 
					|| fieldRef.pathElements().get(0).name().name()==null 
					|| !"Variables".equalsIgnoreCase(fieldRef.pathElements().get(0).name().name().name())){

					addIssue(tree, "Environment variables should be written to the Variables-subtree.");
					
				}
				
			}
		}
	}
	
}
