package com.exxeta.iss.sonar.esql.codecoverage;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.MapSettings;
import org.sonar.api.config.Settings;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
public class TraceSensorTest {
	  private SensorContextTester context;
	 private Settings settings;
	 
	 private File moduleBaseDir = new File("src/test/resources/codecoverage/");
	 
	  private Map<InputFile, Set<Integer>> linesOfCode;
	  
	  private TraceSensor sensor;
	 
	@Before
	  public void init() {
	    settings = new MapSettings();
	    settings.setProperty("sonar.esql.trace.reportPaths", "TraceSensorTest/trace.txt");
	    context = SensorContextTester.create(moduleBaseDir);
	    context.setSettings(settings);

	    InputFile inputFile1 = inputFile("file1.esql", Type.MAIN);
	    InputFile inputFile2 = inputFile("file2.esql", Type.MAIN);
	    //inputFile("tests/file1.esql", Type.TEST);

	    linesOfCode = new HashMap<>();
	    linesOfCode.put(inputFile1, ImmutableSet.of(1, 2, 3, 4));
	    linesOfCode.put(inputFile2, ImmutableSet.of(1, 2, 3));
	    
	    
	    sensor = new TraceSensor();
	    
	    
	    
	  }

	  private InputFile inputFile(String relativePath, Type type) {
	    DefaultInputFile inputFile = new DefaultInputFile("moduleKey", relativePath)
	      .setModuleBaseDir(moduleBaseDir.toPath())
	      .setLanguage("esql")
	      .setType(type)
	      .setCharset(Charsets.UTF_8);

	    try {
			inputFile.initMetadata(new FileMetadata().readMetadata(new FileReader(   inputFile.file())));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	    context.fileSystem().add(inputFile);

	    return inputFile;
	  }
	
	  @Test
	  public void report_not_found() throws Exception {

	    sensor.execute(context,linesOfCode,new String[]{"/fake/path/trace.txt"});

	    assertThat(context.lineHits("moduleKey:file1.js", 1)).isNull();
	  }
	  
	  @Test
	  public void test_overall_coverage() {
	    sensor.execute(context,linesOfCode,new String[]{"TraceSensorTest/trace.txt"});

	    Integer[] file1Expected = {3, 3, 1, null};
	    Integer[] file2Expected = {5, 5, null, null};


	    assertThat(context.conditions("moduleKey:file1.esql", 1)).isNull();
	    assertThat(context.coveredConditions("moduleKey:file1.esql", 2)).isNull();
	    assertThat(context.lineHits("moduleKey:file1.esql", 3)).isNull();
	    assertThat(context.lineHits("moduleKey:file1.esql", 4)).isEqualTo(1);
	  }

}
