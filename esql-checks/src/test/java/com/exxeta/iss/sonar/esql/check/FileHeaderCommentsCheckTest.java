/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2018 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.check;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * This Java class is a test class to check function header comments
 * @author 
 *
 */
public class FileHeaderCommentsCheckTest {

	  @Rule
	  public ExpectedException thrown = ExpectedException.none();

	  @Test
	  public void test_plain() {	 
		  
		  EsqlCheckVerifier.issues(new FileHeaderCommentsCheck(), new File("src/test/resources/FileHeaderComments.esql"))
	        .next().atLine(1).withMessage("Add or update the header at the start of this file.")	             
	        .noMore();
	    	   
	  }	
		
}
