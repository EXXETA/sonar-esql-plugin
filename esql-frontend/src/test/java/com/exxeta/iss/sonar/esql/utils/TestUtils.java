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
package com.exxeta.iss.sonar.esql.utils;

import static com.exxeta.iss.sonar.esql.compat.CompatibilityHelper.wrap;

import java.io.IOException;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.config.MapSettings;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.parser.EsqlParserBuilder;
import com.exxeta.iss.sonar.esql.tree.impl.EsqlTree;
import com.google.common.base.Throwables;

public class TestUtils {

  public static EsqlVisitorContext createContext(InputFile file) {
    try {
      EsqlTree programTree = (EsqlTree) EsqlParserBuilder.createParser().parse(file.contents());
      return new EsqlVisitorContext((ProgramTree)programTree, wrap(file), new MapSettings());
    } catch (IOException e) {
      throw Throwables.propagate(e);
    }
  }
}
