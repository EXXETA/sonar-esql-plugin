package com.exxeta.iss.sonar.esql.api.visitors;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.base.Preconditions;

public class LineIssue implements Issue{
	  private EsqlCheck check;
	  private Double cost;
	  private String message;
	  private int line;

	  public LineIssue(EsqlCheck check, int line, String message) {
	    Preconditions.checkArgument(line > 0);
	    this.check = check;
	    this.message = message;
	    this.line = line;
	    this.cost = null;
	  }

	  public LineIssue(EsqlCheck check, Tree tree, String message) {
	    this.check = check;
	    this.message = message;
	    this.line = ((EsqlTree) tree).getLine();
	    this.cost = null;
	  }

	  public String message() {
	    return message;
	  }

	  public int line() {
	    return line;
	  }

	  @Override
	  public EsqlCheck check() {
	    return check;
	  }

	  @Nullable
	  @Override
	  public Double cost() {
	    return cost;
	  }

	  @Override
	  public Issue cost(double cost) {
	    this.cost = cost;
	    return this;
	  }

}
