package com.exxeta.iss.sonar.esql.parser;

import java.nio.charset.Charset;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.sonar.sslr.api.typed.ActionParser;

public class EsqlParserBuilder {
	 private EsqlParserBuilder() {
	  }
	
	  public static ActionParser<Tree> createParser(Charset charset) {
	    return new EsqlParser(charset);
	  }
}

