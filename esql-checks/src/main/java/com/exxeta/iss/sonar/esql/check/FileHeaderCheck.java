package com.exxeta.iss.sonar.esql.check;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;

@Rule(key="FileHeader")
public class FileHeaderCheck extends DoubleDispatchVisitorCheck  {

	private static final String DEFAULT_HEADER_FORMAT = "";
	private static final String MESSAGE = "Add or update the header of this file.";

	@RuleProperty(key = "headerFormat", description = "Expected copyright and license header", defaultValue = DEFAULT_HEADER_FORMAT, type = "TEXT")
	public String headerFormat = DEFAULT_HEADER_FORMAT;

	@RuleProperty(key = "isRegularExpression", description = "Whether the headerFormat is a regular expression", defaultValue = "false")
	public boolean isRegularExpression = false;

	private String[] expectedLines;
	private Pattern searchPattern = null;


	@Override
	public void visitProgram(ProgramTree tree) {
		if (isRegularExpression) {
		      checkRegularExpression();

		    } else {
		      checkPlainText();
		    }
	}
	 private void checkPlainText() {
		    if (expectedLines == null) {
		      expectedLines = headerFormat.split("(?:\r)?\n|\r");
		    }
		    EsqlFile file = getContext().getEsqlFile();
		    List<String> lines = CheckUtils.readLines(file);
		    if (!matches(expectedLines, lines)) {
		      addIssue(new FileIssue(this, MESSAGE));
		    }
		  }

		  private void checkRegularExpression() {
		    if (searchPattern == null) {
		      try {
		        searchPattern = Pattern.compile(headerFormat, Pattern.DOTALL);
		      } catch (IllegalArgumentException e) {
		        throw new IllegalArgumentException("[" + getClass().getSimpleName() + "] Unable to compile the regular expression: " + headerFormat, e);
		      }
		    }
		    String fileContent = getContext().getEsqlFile().contents();

		    Matcher matcher = searchPattern.matcher(fileContent);
		    if (!matcher.find() || matcher.start() != 0) {
		      addIssue(new FileIssue(this, MESSAGE));
		    }
		  }

		  private static boolean matches(String[] expectedLines, List<String> lines) {
		    boolean result;

		    if (expectedLines.length <= lines.size()) {
		      result = true;

		      Iterator<String> it = lines.iterator();
		      for (String expectedLine : expectedLines) {
		        String line = it.next();
		        if (!line.equals(expectedLine)) {
		          result = false;
		          break;
		        }
		      }
		    } else {
		      result = false;
		    }

		    return result;
		  }
	
}
