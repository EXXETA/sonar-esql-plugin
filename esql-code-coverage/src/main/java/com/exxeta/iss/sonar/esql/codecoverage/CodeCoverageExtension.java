package com.exxeta.iss.sonar.esql.codecoverage;

import java.util.List;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import com.google.common.collect.ImmutableList;

public class CodeCoverageExtension {

	  public static final Logger LOG = Loggers.get(CodeCoverageExtension.class.getName());

	  private CodeCoverageExtension(){}

	  public static List<Object> getExtensions() {
		    ImmutableList.Builder<Object> extensions = ImmutableList.builder();

		    extensions.add(
		    		TraceSensor.class);
		    
		    return extensions.build();

	  }

}
