/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2021 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.api;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.TokenType;

public enum EsqlTokenType implements TokenType {

	REGULAR_EXPRESSION_LITERAL, NEWLINE, NUMBER, STRING, HEX, TIME, DATE;

	@Override
	public String getName() {
		return name();
	}

	@Override
	public String getValue() {
		return name();
	}

	@Override
	public boolean hasToBeSkippedFromAst(AstNode node) {
		return false;
	}
}
