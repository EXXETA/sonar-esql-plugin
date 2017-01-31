package com.exxeta.iss.sonar.esql.checks.verifier;

import java.io.File;

import org.sonar.api.config.Settings;

import com.exxeta.iss.sonar.esql.api.tree.ProgramTree;
import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.visitors.EsqlVisitorContext;
import com.exxeta.iss.sonar.esql.parser.EsqlParserBuilder;
import com.google.common.base.Charsets;
import com.sonar.sslr.api.typed.ActionParser;

public class TestUtils {
	protected static final ActionParser<Tree> p = EsqlParserBuilder.createParser(Charsets.UTF_8);

	  private TestUtils() {
	  }

	  public static EsqlVisitorContext createContext(File file) {
	    ProgramTree scriptTree = (ProgramTree) p.parse(file);
	    return new EsqlVisitorContext(scriptTree, file, settings());
	  }

	  private static Settings settings() {
	    Settings settings = new Settings();


	    return settings;
	  }
}

