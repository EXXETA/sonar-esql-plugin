package com.exxeta.iss.sonar.esql.check;

import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.lexical.SyntaxToken;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;

@Rule(key = "MissingNewlineAtEndOfFile")
public class MissingNewlineAtEndOfFileCheck extends DoubleDispatchVisitorCheck {

  private static final String MESSAGE = "Add a new line at the end of this file.";

  @Override
  public void visitProgram(ProgramTree tree) {
	SyntaxToken lastToken = null;
	  
    if (tree.esqlContents() != null && tree.esqlContents().items().size()>0) {
    	lastToken = ((EsqlTree) tree.esqlContents()).getLastToken();
    } else if (tree.semiToken()!=null){
    	lastToken=tree.semiToken();
    } else if (tree.pathClause()!=null){
    	lastToken=((EsqlTree)tree.pathClause()).getLastToken();
    } else if (tree.brokerSchemaStatement()!=null){
    	lastToken=((EsqlTree)tree.brokerSchemaStatement()).getLastToken();
    }
    if (lastToken!=null){
      int lastLine = tree.EOFToken().line();
      int lastTokenLine = ((EsqlTree) tree.esqlContents()).getLastToken().endLine();

      if (lastLine == lastTokenLine) {
        addIssue(new FileIssue(this, MESSAGE));
      }
    }
  }

}
