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
