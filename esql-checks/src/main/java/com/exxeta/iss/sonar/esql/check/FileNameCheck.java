package com.exxeta.iss.sonar.esql.check;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;

@Rule(key = FileNameCheck.CHECK_KEY)
public class FileNameCheck extends SubscriptionVisitorCheck {
	
	public static final String CHECK_KEY = "FileName";
	private static final String DEFAULT_FORMAT = "^[A-Z][a-zA-Z]{1,30}\\.esql$";
	private static final String MESSAGE = "Rename file \"%s\"  to match the regular expression %s.";
	
	@RuleProperty(key = "format",
			description="regular expression",
			defaultValue = "" + DEFAULT_FORMAT)
	public String format = DEFAULT_FORMAT;
	
	private Pattern pattern;
	public FileNameCheck() {
	}
	
	
	@Override
	public List<Kind> nodesToVisit() {
		return Collections.emptyList();
	}

	@Override
	public void visitFile(Tree scriptTree) {
		if (!Pattern.compile(format).matcher(getContext().getEsqlFile().fileName()).matches()){
			addIssue(new FileIssue(this, String.format(MESSAGE, getContext().getEsqlFile().fileName(), format)));
		}
		super.visitFile(scriptTree);
	}
	
	
}
