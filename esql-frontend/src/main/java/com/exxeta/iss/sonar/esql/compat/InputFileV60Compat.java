/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.compat;

import java.io.IOException;
import java.nio.file.Files;
import org.sonar.api.batch.fs.InputFile;

/**
 * Makes the wrapped API 6.0+ InputFile instance compatible with API 6.2,
 * by providing the inputStream() and contents() methods.
 */
class InputFileV60Compat extends CompatibleInputFile {
  InputFileV60Compat(InputFile wrapped) {
    super(wrapped);
  }

  @Override
  public String contents() {
    try {
      return new String(Files.readAllBytes(this.path()), this.charset());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }
}
