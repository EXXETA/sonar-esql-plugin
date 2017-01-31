package com.exxeta.iss.sonar.esql;

import org.apache.commons.lang.StringUtils;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.AbstractLanguage;

public class EsqlLanguage extends AbstractLanguage {

  public static final String KEY = "esql";

  private Settings settings;

  public EsqlLanguage(Settings configuration) {
    super(KEY, "JavaScript");
    this.settings = configuration;
  }

  @Override
  public String[] getFileSuffixes() {
    String[] suffixes = settings.getStringArray(EsqlPlugin.FILE_SUFFIXES_KEY);
    if (suffixes == null || suffixes.length == 0) {
      suffixes = StringUtils.split(EsqlPlugin.FILE_SUFFIXES_DEFVALUE, ",");
    }
    return suffixes;
  }

}
