package com.exxeta.iss.sonar.esql.codecoverage;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;


import org.apache.commons.lang.StringUtils;


public class TestUtils {
	
	  public static File getResource(String path) {
	    String resourcePath = path;
	    if (!resourcePath.startsWith("/")) {
	      resourcePath = "/" + resourcePath;
	    }
	    URL url = TestUtils.class.getResource(resourcePath);
	    if (url != null) {
	      try {
	        return new File(url.toURI());
	      } catch (URISyntaxException e) {
	        return null;
	      }
	    }
	    return null;
	  }
	
	  public static File getResource(Class baseClass, String path) {
	    String resourcePath = StringUtils.replaceChars(baseClass.getCanonicalName(), '.', '/');
	    if (!path.startsWith("/")) {
	      resourcePath += "/";
	    }
	    resourcePath += path;
	    return getResource(resourcePath);
	  }
}
