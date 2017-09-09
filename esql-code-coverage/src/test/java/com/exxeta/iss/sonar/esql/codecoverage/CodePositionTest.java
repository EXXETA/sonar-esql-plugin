package com.exxeta.iss.sonar.esql.codecoverage;

import java.io.File;

import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultInputFile;

import com.google.common.base.Charsets;

public class CodePositionTest {

	@Test
	public void test(){
		InputFile file1 = inputFile("file1.esql");  
		InputFile file2 = inputFile("file2.esql");  
		CodePosition cp1 = new CodePosition(file1, 1);
		CodePosition cp2 = new CodePosition(file1, 2);
		CodePosition cp3 = new CodePosition(file2, 1);
		CodePosition cp4 = new CodePosition(file1, 1);
		CodePosition cp5 = new CodePosition(null, 1);
		CodePosition cp6 = new CodePosition(null, 1);
		
		assert(!cp1.equals(null));
		assert(!cp1.equals(""));
		assert(cp1.equals(cp1));
		assert(!cp1.equals(cp2));
		assert(!cp1.equals(cp3));
		assert(cp1.equals(cp4));
		assert(!cp1.equals(cp5));
		assert(!cp5.equals(cp1));
		assert(cp1.hashCode()==cp4.hashCode());
		assert(cp5.equals(cp6));
	}
	
	
	private InputFile inputFile(String relativePath){
		File moduleBaseDir = new File("");
		DefaultInputFile inputFile = new DefaultInputFile("moduleKey", relativePath)
			      .setModuleBaseDir(moduleBaseDir.toPath())
			      .setLanguage("esql")
			      .setType(Type.MAIN)
			      .setCharset(Charsets.UTF_8);
		return inputFile;
	}
}
