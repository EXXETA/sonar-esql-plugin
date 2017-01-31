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
import com.exxeta.iss.sonar.esql.check.NestedIfDepthCheck;

public class NestedIfDepthCheckTest {
	 @Test
	  public void test() {
	    NestedIfDepthCheck check = new NestedIfDepthCheck();
	    check.maximumNestingLevel=3;
	    
	    SourceFile file =EsqlAstScanner.scanSingleFile(new File("src/test/resources/ifTest.esql"), check);
	    CheckMessagesVerifier.verify(file.getCheckMessages())
	        .next().atLine(8).withMessage("This if has a nesting level of 4, which is higher than the maximum allowed 3.")
	        .next().atLine(12).withMessage("This if has a nesting level of 4, which is higher than the maximum allowed 3.")
	        .noMore();
	  }
}
