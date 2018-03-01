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

import org.junit.Test;

import com.exxeta.iss.sonar.esql.checks.verifier.EsqlCheckVerifier;

/**
 * This Java class is a test class to check function header comments
 * @author 
 *
 */
public class RoutineCommentsCheckTest {
	@Test
	public void test() {
				
		EsqlCheckVerifier.issues(new RoutineCommentsCheck(), new File("src/test/resources/routineComments.esql"))
        .next().atLine(3).withMessage("Document this function with all parameters and return types.")
        .next().atLine(15).withMessage("Document this function with all parameters and return types.")      
        .next().atLine(42).withMessage("Document this function with all parameters and return types.")      
        .next().atLine(57).withMessage("Document this procedure with all parameters and return types.")
        .next().atLine(66).withMessage("Document this procedure with all parameters and return types.")      
        .noMore();
	}
}
