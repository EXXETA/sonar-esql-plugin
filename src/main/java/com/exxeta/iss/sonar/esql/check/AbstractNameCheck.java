/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
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
 */package com.exxeta.iss.sonar.esql.check;

import java.util.regex.Pattern;

import org.sonar.squidbridge.checks.SquidCheck;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import com.sonar.sslr.api.Grammar;

public abstract class AbstractNameCheck extends SquidCheck<Grammar> {
	
	private Pattern pattern = null;

	@Override
	public void init() {
		pattern = Pattern.compile(getFormat());
		subscribeTo(getType());
	}

	protected abstract String getFormat() ;

	@Override
	public void visitNode(AstNode astNode) {
		for (AstNode nameNode : astNode.getChildren(EsqlGrammar.NAME)){
			String name = nameNode.getTokenOriginalValue();
			if (!pattern.matcher(name).matches()) {
				getContext().createLineViolation(this,
						"Rename {0} \"{1}\" to match the regular expression {2}.",
						nameNode, typeName(), name, getFormat());
			}
		}
	}

	public abstract String typeName();
	public abstract AstNodeType getType();
}