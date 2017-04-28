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
package com.exxeta.iss.sonar.esql.compat;

import java.nio.charset.Charset;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;

/**
 * Makes the wrapped API 5.6+ InputFile instance compatible with API 6.0,
 * by providing the charset() method.
 */
class InputFileV56Compat extends InputFileV60Compat {

  private final Charset charset;

  InputFileV56Compat(InputFile wrapped, SensorContext context) {
    super(wrapped);
    this.charset = context.fileSystem().encoding();
  }

  @Override
  public Charset charset() {
    return charset;
  }
}
