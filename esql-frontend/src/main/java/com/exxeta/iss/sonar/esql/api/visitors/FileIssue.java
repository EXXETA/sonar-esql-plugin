package com.exxeta.iss.sonar.esql.api.visitors;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;


public class FileIssue implements Issue {

	  private EsqlCheck check;
	  private Double cost;
	  private String message;

	  public FileIssue(EsqlCheck check, String message) {
	    this.check = check;
	    this.message = message;
	    this.cost = null;
	  }

	  public String message() {
	    return message;
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
