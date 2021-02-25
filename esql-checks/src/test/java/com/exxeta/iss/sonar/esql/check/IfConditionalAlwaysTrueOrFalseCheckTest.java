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
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

public class IfConditionalAlwaysTrueOrFalseCheckTest {
	 @Test
	  public void test() {
		 EsqlCheckVerifier.issues(new IfConditionalAlwaysTrueOrFalseCheck(), new File("src/test/resources/ifTest.esql"))
	        .next().atLine(6).withMessage("Remove this \"IF\" statement.")
	        .next().atLine(7).withMessage("Remove this \"IF\" statement.")
	        .next().atLine(8).withMessage("Remove this \"IF\" statement.")
	        .next().atLine(9).withMessage("Remove this \"IF\" statement.")
	        .next().atLine(13).withMessage("Remove this \"IF\" statement.")
	        .next().atLine(17).withMessage("Remove this \"IF\" statement.")
	        .noMore();
	  }
}
