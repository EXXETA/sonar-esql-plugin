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
 */package com.exxeta.iss.sonar.esql.check;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.check.Rule;

import com.exxeta.iss.sonar.esql.api.tree.Tree;
import com.exxeta.iss.sonar.esql.api.tree.Tree.Kind;
import com.exxeta.iss.sonar.esql.api.visitors.FileIssue;
import com.exxeta.iss.sonar.esql.api.visitors.LineIssue;
import com.exxeta.iss.sonar.esql.api.visitors.SubscriptionVisitorCheck;
import com.exxeta.iss.sonar.esql.lexer.EsqlLexer;
import com.exxeta.iss.sonar.esql.tree.impl.statement.SetStatementTreeImpl;
import com.google.common.collect.ImmutableList;
/**
 * This java class is created to implement the logic for checking that space should be given around equal sign.
 * @author sapna singh
 *
 */
@Rule(key = "SpaceAroundEqualSign")
public class SpaceAroundEqualSignCheck extends SubscriptionVisitorCheck {

	public static final Logger LOG = Loggers.get(SpaceAroundEqualSignCheck.class.getName());
	
	
	private static final String MESSAGE = "Add space around \"=\" sign.";

	private List<String> lines = null;
	private String filePath = null;
	  @Override
	  public List<Kind> nodesToVisit() {
	    return ImmutableList.of(	      
	        Kind.SET_STATEMENT);
	  }
	  
	  @Override
	  public void visitNode(Tree tree) {
		 
		  if(lines == null || !filePath.equals(getContext().getFile().getPath())){
				try {
					 filePath = getContext().getFile().getPath();
					 lines = null;
					lines = Files.readAllLines(Paths.get(filePath));
					
				} catch (IOException e) {
					addIssue(new FileIssue(this, "Unable to read file."));					
					LOG.error("Unable to read file.",e);
				}
				}
		  SetStatementTreeImpl equalTree = (SetStatementTreeImpl)tree;
		  int lineNumber;
		  int column;
		  String line = null;
				
				lineNumber = equalTree.equalSign().line();	
				column = equalTree.equalSign().column();
				line = lines.get(lineNumber-1);
				
				if (!line.substring(column-1, column).matches(EsqlLexer.WHITESPACE) || !line.substring(column+1, column+2).matches(EsqlLexer.WHITESPACE)) {
					addIssue(new LineIssue(this, lineNumber, MESSAGE));
				}
	  }

	

}