package com.exxeta.iss.sonar.esql.check;

import java.util.regex.Pattern;

import org.sonar.check.BelongsToProfile;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.sonar.squidbridge.annotations.NoSqale;
import org.sonar.squidbridge.checks.SquidCheck;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Grammar;

@Rule(key = FileNameCheck.CHECK_KEY, priority = Priority.MAJOR, name = "File names should comply with a naming convention", tags = Tags.CONVENTION)
@NoSqale
@BelongsToProfile(title=CheckList.SONAR_WAY_PROFILE,priority=Priority.MAJOR)
public class FileNameCheck extends SquidCheck<Grammar> {
	
	public static final String CHECK_KEY = "FileName";
	private static final String DEFAULT_FORMAT = "^[A-Z][a-zA-Z]{1,30}\\.esql$";
	
	@RuleProperty(key = "format",
			description="regular expression",
			defaultValue = "" + DEFAULT_FORMAT)
	public String format = DEFAULT_FORMAT;
	
	private Pattern pattern;
	@Override
	public void init() {
		pattern = Pattern.compile(format);
		subscribeTo(GenericTokenType.EOF);
	}

	@Override
	public void visitNode(AstNode astNode) {
		String filename = getContext().getFile().getName();
		if (!pattern.matcher(filename).matches()){
			String message = "Rename file \"{0}\"  to match the regular expression {1}.";
			getContext().createFileViolation(this, message, filename, format);
		}
	}
}
