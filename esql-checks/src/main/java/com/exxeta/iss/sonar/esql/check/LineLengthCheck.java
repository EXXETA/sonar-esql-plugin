/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2017 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.check;

import java.util.List;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.DoubleDispatchVisitorCheck;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlFile;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;




@Rule(key = "LineLength")
public class LineLengthCheck extends DoubleDispatchVisitorCheck  {

  private static final String MESSAGE = "Split this %s characters long line (which is greater than %s authorized).";
  private static final int DEFAULT_MAXIMUM_LINE_LENGTH = 180;

  @RuleProperty(
    key = "maximumLineLength",
    description = "The maximum authorized line length.",
    defaultValue = "" + DEFAULT_MAXIMUM_LINE_LENGTH)
  public int maximumLineLength = DEFAULT_MAXIMUM_LINE_LENGTH;

  @Override
  public void visitProgram(ProgramTree tree) {
	  EsqlFile file = getContext().getEsqlFile();
	   List<String> lines = CheckUtils.readLines(file);

    for (int i = 0; i < lines.size(); i++) {
      int length = lines.get(i).length();

      if (length > maximumLineLength) {
        addIssue(new LineIssue(
          this,
          i + 1,
          String.format(MESSAGE, length, maximumLineLength)));
      }
    }
  }

}
