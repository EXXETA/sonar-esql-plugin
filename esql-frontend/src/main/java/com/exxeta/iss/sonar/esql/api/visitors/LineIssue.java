/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2023 Thomas Pohl and EXXETA AG
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
