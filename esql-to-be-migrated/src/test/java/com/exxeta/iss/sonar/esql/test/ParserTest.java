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
package com.exxeta.iss.sonar.esql.test;

import static org.fest.assertions.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.sonar.squidbridge.AstScanner;
import org.sonar.squidbridge.SquidAstVisitor;

import com.exxeta.iss.sonar.esql.EsqlAstScanner;
import com.exxeta.iss.sonar.esql.EsqlConfiguration;
import com.google.common.base.Charsets;
import com.sonar.sslr.api.AstAndTokenVisitor;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Token;


public class ParserTest {

	private class Visitor extends SquidAstVisitor<Grammar> implements AstAndTokenVisitor {
		public boolean ok = false;

		public void visitToken(Token token) {
			ok = true;

		}
	}

	@Test
	public void test() {

		Visitor visitor = new Visitor();
		File dir = new File("src/test/resources");
		AstScanner<Grammar> scanner = EsqlAstScanner.create(new EsqlConfiguration(Charsets.UTF_8), visitor);
		parseDirectory(dir, scanner, visitor);

	}

	private void parseDirectory(File dir, AstScanner<Grammar> scanner, Visitor visitor) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()){
				parseDirectory(f, scanner, visitor);
			}else if (f.getAbsolutePath().endsWith(".esql") && !f.getName().contains("parsingError")) {
				visitor.ok = false;
				scanner.scanFile(f);
				assertThat(visitor.ok).isTrue();
				
			}
		}
	}
}
