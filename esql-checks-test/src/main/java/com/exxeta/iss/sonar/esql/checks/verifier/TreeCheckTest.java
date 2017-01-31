package com.exxeta.iss.sonar.esql.checks.verifier;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sonar.squidbridge.api.CheckMessage;

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
	    File file = new File(relativePath);

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

