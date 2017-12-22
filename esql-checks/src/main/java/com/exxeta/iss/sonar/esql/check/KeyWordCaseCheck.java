package com.exxeta.iss.sonar.esql.check;

import java.util.List;
import java.util.Set;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.EsqlNonReservedKeyword;
import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.lexer.EsqlReservedKeyword;
import com.google.common.collect.ImmutableSet;
/**
 * This Java Class Is created to ensure that all the keywords in esql file should be in UPPER CASE
 * @author sapna singh
 *
 */

@Rule(key = "KeyWordCaseCheck")
public class KeyWordCaseCheck extends DoubleDispatchVisitorCheck {
	private static final Set<String> nonReservedKeywords = ImmutableSet.copyOf(EsqlNonReservedKeyword.keywordValues());
	private static final Set<String> reservedKeywords = ImmutableSet.copyOf(EsqlReservedKeyword.keywordValues());
	private static final String MESSAGE = "All keywords should be in Uppercase.";

	@Override
	public void visitProgram(ProgramTree tree) {
		EsqlFile file = getContext().getEsqlFile();
		List<String> lines = CheckUtils.readLines(file);
		int i = 0;
		boolean commentSection = false;
		for (String line : lines) {
			i = i + 1;
			if (!line.trim().startsWith("--") && !line.trim().startsWith("/*") && !commentSection) {
				for (String word : line.split(" ")) {
					String trimmed = word.trim();
					if (CheckForKeywords(trimmed.toUpperCase())) {
						if (!trimmed.toUpperCase().equals(trimmed)) {
							addIssue(new LineIssue(this, i, "Check keyword \"" + trimmed + "\". " + MESSAGE));
						}
					}
				}
			} else if (line.trim().startsWith("/*") && !commentSection && !line.trim().endsWith("*/")) {
				commentSection = true;
			} else if (commentSection && line.trim().endsWith("*/")) {
				commentSection = false;
			}

		}

	}

	private static boolean CheckForKeywords(String s) {

		if (reservedKeywords.contains(s)) {
			return true;
		} else if (nonReservedKeywords.contains(s) && !s.equals("ENVIRONMENT")) {
			return true;
		} else {
			return false;
		}
	}
}
