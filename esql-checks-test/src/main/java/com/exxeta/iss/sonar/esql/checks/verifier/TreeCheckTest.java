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
package com.exxeta.iss.sonar.esql.checks.verifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sonar.api.batch.fs.InputFile;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import com.exxeta.iss.sonar.esql.api.visitors.Issue;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.PreciseIssue;

class TreeCheckTest {

	  private TreeCheckTest() {
	  }

	  public static Collection<CheckMessage> getIssues(String relativePath, EsqlCheck check) {
		InputFile file =  TestUtils.createTestInputFile(relativePath);

	    EsqlVisitorContext context = TestUtils.createContext(file);
	    List<Issue> issues = check.scanFile(context);

	    return getCheckMessages(issues);
	  }

	  private static Collection<CheckMessage> getCheckMessages(List<Issue> issues) {
	    List<CheckMessage> checkMessages = new ArrayList<>();
	    for (Issue issue : issues) {
	      CheckMessage checkMessage;
	      if (issue instanceof FileIssue) {
	        FileIssue fileIssue = (FileIssue)issue;
	        checkMessage = new CheckMessage(fileIssue.check(), fileIssue.message());

	      } else if (issue instanceof LineIssue) {
	        LineIssue lineIssue = (LineIssue)issue;
	        checkMessage = new CheckMessage(lineIssue.check(), lineIssue.message());
	        checkMessage.setLine(lineIssue.line());

	      } else {
	        PreciseIssue preciseIssue = (PreciseIssue) issue;
	        checkMessage = new CheckMessage(preciseIssue.check(), preciseIssue.primaryLocation().message());
	        checkMessage.setLine(preciseIssue.primaryLocation().startLine());
	      }

	      if (issue.cost() != null) {
	        checkMessage.setCost(issue.cost());
	      }

	      checkMessages.add(checkMessage);
	    }

	    return checkMessages;
	  }

	}

