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
package com.exxeta.iss.sonar.esql.cpd;

import net.sourceforge.pmd.cpd.Tokenizer;

import org.sonar.api.batch.AbstractCpdMapping;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.ProjectFileSystem;

import com.exxeta.iss.sonar.esql.core.Esql;

import java.nio.charset.Charset;

public class EsqlCpdMapping extends AbstractCpdMapping {

  private final Esql language;
  private final Charset charset;

  public EsqlCpdMapping(Esql language, ProjectFileSystem fs) {
    this.language = language;
    this.charset = fs.getSourceCharset();
  }

  public Tokenizer getTokenizer() {
    return new EsqlTokenizer(charset);
  }

  public Language getLanguage() {
    return language;
  }

}
