/*
 * SonarQube JavaScript Plugin
 * Copyright (C) 2011-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
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
