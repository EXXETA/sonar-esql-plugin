package com.exxeta.iss.sonar.esql.api.visitors;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.exxeta.iss.sonar.esql.api.EsqlCheck;
import com.exxeta.iss.sonar.esql.api.tree.Tree;


public class PreciseIssue implements Issue{
	  private EsqlCheck check;
	  private Double cost;
	  private IssueLocation primaryLocation;
	  private List<IssueLocation> secondaryLocations;

	  public PreciseIssue(EsqlCheck check, IssueLocation primaryLocation) {
	    this.check = check;
	    this.primaryLocation = primaryLocation;
	    this.secondaryLocations = new ArrayList<>();
	    this.cost = null;
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
	  public PreciseIssue cost(double cost) {
	    this.cost = cost;
	    return this;
	  }

	  public IssueLocation primaryLocation() {
	    return primaryLocation;
	  }

	  public List<IssueLocation> secondaryLocations() {
	    return secondaryLocations;
	  }

	  public PreciseIssue secondary(IssueLocation secondaryLocation) {
	    secondaryLocations.add(secondaryLocation);
	    return this;
	  }

	  public PreciseIssue secondary(Tree tree, String message) {
	    secondaryLocations.add(new IssueLocation(tree, message));
	    return this;
	  }

	  public PreciseIssue secondary(Tree tree) {
	    secondaryLocations.add(new IssueLocation(tree, null));
	    return this;
	  }

}
