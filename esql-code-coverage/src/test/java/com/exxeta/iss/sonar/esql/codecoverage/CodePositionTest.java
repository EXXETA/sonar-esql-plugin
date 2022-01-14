/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013-2022 Thomas Pohl and EXXETA AG
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
package com.exxeta.iss.sonar.esql.codecoverage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;

import com.google.common.base.Charsets;

class CodePositionTest {

	@Test
	void test(){
		InputFile file1 = inputFile("file1.esql");  
		InputFile file2 = inputFile("file2.esql");  
		CodePosition cp1 = new CodePosition(file1, 1);
		CodePosition cp2 = new CodePosition(file1, 2);
		CodePosition cp3 = new CodePosition(file2, 1);
		CodePosition cp4 = new CodePosition(file1, 1);
		CodePosition cp5 = new CodePosition(null, 1);
		CodePosition cp6 = new CodePosition(null, 1);
		
		assertFalse(cp1.equals(null));
		assertFalse(cp1.equals(""));
		assertTrue(cp1.equals(cp1));
		assertFalse(cp1.equals(cp2));
		assertFalse(cp1.equals(cp3));
		assertTrue(cp1.equals(cp4));
		assertFalse(cp1.equals(cp5));
		assertFalse(cp5.equals(cp1));
		assertThat(cp1.hashCode()).isEqualTo(cp4.hashCode());
		assertThat(cp5.hashCode()).isEqualTo(cp6.hashCode());
		assertTrue(cp5.equals(cp6));
	}
	
	
	private InputFile inputFile(String relativePath){
		File moduleBaseDir = new File("");
		DefaultInputFile inputFile = new TestInputFileBuilder("moduleKey", relativePath)
			      .setModuleBaseDir(moduleBaseDir.toPath())
			      .setLanguage("esql")
			      .setType(Type.MAIN)
			      .setCharset(Charsets.UTF_8)
			      .build();
		return inputFile;
	}
}
