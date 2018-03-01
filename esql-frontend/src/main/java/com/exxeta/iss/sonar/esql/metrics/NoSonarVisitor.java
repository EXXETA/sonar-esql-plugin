package com.exxeta.iss.sonar.esql.metrics;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.issue.NoSonarFilter;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitor;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFileImpl;

public class NoSonarVisitor extends DoubleDispatchVisitor {
	 private final NoSonarFilter noSonarFilter;
	  private final boolean ignoreHeaderComments;

	  public NoSonarVisitor(NoSonarFilter noSonarFilter, boolean ignoreHeaderComments) {
	    this.noSonarFilter = noSonarFilter;
	    this.ignoreHeaderComments = ignoreHeaderComments;
	  }

	  @Override
	  public void visitProgram(ProgramTree tree) {
	    CommentLineVisitor commentVisitor = new CommentLineVisitor(tree, ignoreHeaderComments);

	    InputFile inputFile = ((EsqlFileImpl) getContext().getEsqlFile()).inputFile();
	    noSonarFilter.noSonarInFile(inputFile, commentVisitor.noSonarLines());
	  }
}
