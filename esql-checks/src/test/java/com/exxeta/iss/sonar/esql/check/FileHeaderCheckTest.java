/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class FileHeaderCheckTest {

		  private final File file1 = new File("src/test/resources/FileHeaderCheck/file1.esql");
		  private final File file2 = new File("src/test/resources/FileHeaderCheck/file2.esql");

		  @Rule
		  public ExpectedException thrown = ExpectedException.none();

		  @Test
		  public void test_plain() {
		    EsqlCheckVerifier.issues(checkPlainText("-- copyright 2005"), file1)
		      .noMore();

		    EsqlCheckVerifier.issues(checkPlainText("-- copyright 20\\d\\d"), file1)
		      .next().atLine(null);

		    EsqlCheckVerifier.issues(checkPlainText("-- copyright 2005"), file2)
		      .next().atLine(null).withMessage("Add or update the header of this file.");

		    EsqlCheckVerifier.issues(checkPlainText("-- copyright 2012"), file2)
		      .noMore();

		    EsqlCheckVerifier.issues(checkPlainText("-- copyright 2012\n-- foo"), file2)
		      .noMore();

		    EsqlCheckVerifier.issues(checkPlainText("-- copyright 2012\r\n-- foo"), file2)
		      .noMore();

		    EsqlCheckVerifier.issues(checkPlainText("-- copyright 2012\r-- foo"), file2)
		      .noMore();

		    EsqlCheckVerifier.issues(checkPlainText("-- copyright 2012\r\r-- foo"), file2)
		      .next().atLine(null);

		    EsqlCheckVerifier.issues(checkPlainText("-- copyright 2012\n-- foo\n\n\n\n\n\n\n\n\n\ngfoo"), file2)
		      .next().atLine(null);

		    EsqlCheckVerifier.issues(checkPlainText("/*foo http://www.example.org*/"), new File("src/test/resources/FileHeaderCheck/file3.esql"))
		      .noMore();
		  }

		  @Test
		  public void test_regular_expression() {
		    EsqlCheckVerifier.issues(checkWithRegex("-- copyright 2005"), file1)
		      .noMore();

		    EsqlCheckVerifier.issues(checkWithRegex("-- copyright 20\\d\\d"), file1)
		      .noMore();

		    EsqlCheckVerifier.issues(checkWithRegex("-- copyright 20\\d\\d"), file2)
		      .noMore();

		    EsqlCheckVerifier.issues(checkWithRegex("-- copyright 20\\d\\d\\n-- foo"), file2)
		      .noMore();

		    EsqlCheckVerifier.issues(checkWithRegex("-- copyright 20\\d{3}\\n-- foo"), file2)
		      .next().atLine(null);

		    EsqlCheckVerifier.issues(checkWithRegex("-- copyright 20\\d{2}\\r?\\n-- foo"), file2)
		      .noMore();

		    EsqlCheckVerifier.issues(checkWithRegex("-- copyright 20\\d\\d\\r-- foo"), file2)
		      .next().atLine(null);
		  }

		  @Test
		  public void should_fail_with_bad_regular_expression() {
		    thrown.expect(IllegalArgumentException.class);
		    thrown.expectMessage("[" + FileHeaderCheck.class.getSimpleName() + "] Unable to compile the regular expression: *");

		    FileHeaderCheck check = new FileHeaderCheck();
		    check.headerFormat = "*";
		    check.isRegularExpression = true;
		    EsqlCheckVerifier.issues(checkWithRegex("*"), file1);
		  }

		  private static EsqlCheck checkWithRegex(String pattern) {
		    FileHeaderCheck check = new FileHeaderCheck();
		    check.isRegularExpression = true;
		    check.headerFormat = pattern;
		    return check;
		  }

		  private static EsqlCheck checkPlainText(String format) {
		    FileHeaderCheck check = new FileHeaderCheck();
		    check.isRegularExpression = false;
		    check.headerFormat = format;
		    return check;
		  }
		}