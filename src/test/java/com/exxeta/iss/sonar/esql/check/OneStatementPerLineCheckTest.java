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
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;
import org.sonar.squidbridge.api.SourceFile;
import org.sonar.squidbridge.checks.CheckMessagesVerifier;

import com.exxeta.iss.sonar.esql.EsqlAstScanner;
import com.exxeta.iss.sonar.esql.check.OneStatementPerLineCheck;

public class OneStatementPerLineCheckTest {
	@Test
	public void test() {
	OneStatementPerLineCheck check = new OneStatementPerLineCheck();
	SourceFile file = EsqlAstScanner.scanSingleFile(new File("src/test/resources/oneStatementPerLine.esql"), check);
	CheckMessagesVerifier.verify(file.getCheckMessages())
	.next().atLine(5).withMessage("At most one statement is allowed per line, but 2 statements were found on this line.")
	.next().atLine(6).withMessage("At most one statement is allowed per line, but 3 statements were found on this line.")
	.noMore();
	}
}
