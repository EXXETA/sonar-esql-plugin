/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2020 Thomas Pohl and EXXETA AG
 * http://www.exxeta.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
