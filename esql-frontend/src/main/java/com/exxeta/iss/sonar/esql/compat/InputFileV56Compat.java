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
