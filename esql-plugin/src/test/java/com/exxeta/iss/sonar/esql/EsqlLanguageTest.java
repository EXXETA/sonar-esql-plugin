package com.exxeta.iss.sonar.esql;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.config.Settings;

public class EsqlLanguageTest {
	  private Settings settings;
	  private EsqlLanguage esqlLanguage;

	  @Before
	  public void setUp() {
	    settings = new Settings();
	    esqlLanguage = new EsqlLanguage(settings);
	  }

	  @Test
	  public void defaultSuffixes() {
	    settings.setProperty(EsqlPlugin.FILE_SUFFIXES_KEY, "");
	    assertThat(esqlLanguage.getFileSuffixes()).containsOnly(".esql");
	  }

	  @Test
	  public void customSuffixes() {
	    settings.setProperty(EsqlPlugin.FILE_SUFFIXES_KEY, "esql");
	    assertThat(esqlLanguage.getFileSuffixes()).containsOnly("esql");
	  }

}
