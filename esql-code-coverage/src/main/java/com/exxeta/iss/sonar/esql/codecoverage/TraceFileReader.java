package com.exxeta.iss.sonar.esql.codecoverage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TraceFileReader {

	private final File traceFile;

	private Pattern pattern;

	private static final String PATTERN = ".* at \\('(.*)', '(\\w+)\\.\\w+'\\).*";

	private HashMap<String, ModuleExecutionData> moduleCache = new HashMap<>();

	public TraceFileReader(File traceFile) {

		pattern = Pattern.compile(PATTERN);
		this.traceFile = traceFile;
	}

	public TraceFileReader readTrace(ExecutionDataVisitor executionDataVisitor) {
		if (traceFile == null) {
			return this;
		}

		CodeCoverageExtension.LOG.info("Analysing {}", traceFile);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(traceFile)))) {
			while (reader.ready()) {
				readLine(reader.readLine());
			}

		} catch (IOException e) {
			throw new RuntimeException(String.format("Unable to read %s", traceFile.getAbsolutePath()), e);
			// TODO other Exception
		}
		for (String moduleName : moduleCache.keySet()){
			ModuleExecutionData data = moduleCache.get(moduleName);
			executionDataVisitor.visitModuleExecution(data);
		}
		return this;
	}

	private void readLine(String line) {
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {
			String function = matcher.group(1).trim();
			String relativeLine = matcher.group(2).trim();
			String statement = line.substring(line.indexOf("''") + 2, line.lastIndexOf("''", line.indexOf(" at ")))
					.trim();
			String schemaAndModuleName = "";
			if (function.contains(".")) {
				schemaAndModuleName = function.substring(0, function.lastIndexOf('.')).trim();
			}
			if (!function.equals(".statusACTIVE") && !function.equals(".statusINACTIVE")) {
				ModuleExecutionData moduleExecutionData;
				if (moduleCache.containsKey(schemaAndModuleName)) {
					moduleExecutionData = moduleCache.get(schemaAndModuleName);
				} else {
					moduleExecutionData = new ModuleExecutionData(schemaAndModuleName);
					moduleCache.put(schemaAndModuleName, moduleExecutionData);
					CodeCoverageExtension.LOG.info("New Module {}", schemaAndModuleName);
				}
				moduleExecutionData.addExecution(function, relativeLine, statement);
			}
		}
	}

	public int getModuleCount(){
		return moduleCache.size();
	}
	
	public ModuleExecutionData getModuleExecutionData(String moduleName){
		return moduleCache.get(moduleName);
	}
}
