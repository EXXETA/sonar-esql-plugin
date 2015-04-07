/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
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
package com.exxeta.iss.sonar.esql.core;

import org.sonar.api.batch.AbstractSourceImporter;
import org.sonar.api.batch.Phase;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.resources.InputFileUtils;
import org.sonar.api.resources.ProjectFileSystem;

@Phase(name = Phase.Name.PRE)
public class EsqlSourceImporter extends AbstractSourceImporter {

  public EsqlSourceImporter(Esql esql) {
    super(esql);
  }

  protected void analyse(ProjectFileSystem fileSystem, SensorContext context) {
    parseDirs(context, InputFileUtils.toFiles(fileSystem.mainFiles(Esql.KEY)), fileSystem.getSourceDirs(), false, fileSystem.getSourceCharset());
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }

}
