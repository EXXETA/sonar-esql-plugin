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
package com.exxeta.iss.sonar.esql.api.visitors;

import java.io.IOException;
import java.net.URI;
import org.sonar.api.batch.fs.InputFile;

public class EsqlFileImpl implements EsqlFile {

  private InputFile inputFile;

  public EsqlFileImpl(InputFile inputFile) {
    this.inputFile = inputFile;
  }

  @Override
  @Deprecated
  public String relativePath() {
    return inputFile.relativePath();
  }

  @Override
  public String fileName() {
    return inputFile.filename();
  }

  @Override
  public String contents() throws IOException {
    return inputFile.contents();
  }

  @Override
  public URI uri() {
    return inputFile.uri();
  }

  public InputFile inputFile() {
    return inputFile;
  }
}
