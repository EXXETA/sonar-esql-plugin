/**
 * This java class is created to implement the logic for checking if comment is included or not,
 * over every 20 lines of code.
 *
 * @author Prerana Agarkar
 */
package com.exxeta.iss.sonar.esql.check;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import java.util.List;

@Rule(key = "Comments")
public class CommentsCheck extends DoubleDispatchVisitorCheck {

    private static final String MESSAGE = "Include comment within the range of every 20 lines of code.";
    private static final int DEFAULT_THRESHOLD = 21;
    @RuleProperty(
            key = "Comments",
            description = "Include comment within the range of every 20 lines of code.",
            defaultValue = "" + DEFAULT_THRESHOLD)
    public int maximumThreshold = DEFAULT_THRESHOLD;


    @Override
    public void visitProgram(ProgramTree tree) {
        EsqlFile file = getContext().getEsqlFile();
        List<String> lines = CheckUtils.readLines(file);

        int linesCounter = 0;
        int commentsCount = 0;


        for (int i = 0; i < lines.size(); i++) {

            String originalLine = lines.get(i);

            if (originalLine.replaceAll("\\s+", "").isEmpty()) {
                continue;
            }

            if (originalLine.replaceAll("\\s+", "").startsWith("--")) {
                linesCounter = 0;
            }

            if (originalLine.replaceAll("\\s+", "").startsWith("/*")) {

                commentsCount = commentsCount + 1;
                continue;
            }

            if (commentsCount >= 1 && !(originalLine.replaceAll("\\s+", "").endsWith("*/"))) {
                continue;
            } else if (commentsCount >= 1 && (originalLine.replaceAll("\\s+", "").endsWith("*/"))) {
                linesCounter = 0;
                commentsCount = 0;
            }

            linesCounter = linesCounter + 1;

            if (linesCounter > DEFAULT_THRESHOLD) {
                addIssue(new LineIssue(this, i + 1, MESSAGE));
                linesCounter = 0;
            }

        }

    }
}
