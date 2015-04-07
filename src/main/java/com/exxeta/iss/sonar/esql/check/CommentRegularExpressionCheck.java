package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.BelongsToProfile;
import org.sonar.check.Cardinality;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.EsqlGrammar;
import com.sonar.sslr.squid.checks.AbstractCommentRegularExpressionCheck;

@Rule(
  key = "CommentRegularExpression",
  priority = Priority.MAJOR,
  cardinality = Cardinality.MULTIPLE)
@BelongsToProfile(title = CheckList.SONAR_WAY_PROFILE, priority = Priority.MAJOR)
public class CommentRegularExpressionCheck extends AbstractCommentRegularExpressionCheck<EsqlGrammar> {

  private static final String DEFAULT_REGULAR_EXPRESSION = "";
  private static final String DEFAULT_MESSAGE = "The regular expression matches this comment";

  @RuleProperty(
    key = "regularExpression",
    defaultValue = "" + DEFAULT_REGULAR_EXPRESSION)
  public String regularExpression = DEFAULT_REGULAR_EXPRESSION;

  @RuleProperty(
    key = "message",
    defaultValue = "" + DEFAULT_MESSAGE)
  public String message = DEFAULT_MESSAGE;

  @Override
  public String getRegularExpression() {
    return regularExpression;
  }

  @Override
  public String getMessage() {
    return message;
  }

}