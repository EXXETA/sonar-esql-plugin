package com.exxeta.iss.sonar.esql.codecoverage;

import static com.exxeta.iss.sonar.esql.codecoverage.CodeCoverageExtension.LOG;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.exxeta.iss.sonar.esql.trace.UserTraceLog;
import com.exxeta.iss.sonar.esql.trace.UserTraceType;
public class TraceFileReader {

	private final File traceFile;

	private Pattern pattern;

	private static final String PATTERN = ".* at \\('(.*)', '(\\w+)\\.\\w+'\\).*";

	//Contains the executionData per module
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
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(traceFile)));
			String firstLine = reader.readLine();
			if (firstLine.startsWith("<?xml")){
				readXML();
			} else {
				readLine(firstLine);
				while (reader.ready()) {
					readLine(reader.readLine());
				}
			}
			reader.close();
		} catch(IOException e){
			throw new RuntimeException(String.format("Unable to read %s", traceFile.getAbsolutePath()), e);
		}

		// Iterate over the moduleCache and the visitor
		for (String moduleName : moduleCache.keySet()){
			ModuleExecutionData data = moduleCache.get(moduleName);
			executionDataVisitor.visitModuleExecution(data);
		}
		
		return this;
	}

	private void readXML() {
		try {
			JAXBContext jc = JAXBContext.newInstance(UserTraceLog.class);

			Unmarshaller unmarshaller = jc.createUnmarshaller();
			UserTraceLog userTraceLog =(UserTraceLog) unmarshaller.unmarshal(traceFile);
			
			for (UserTraceType trace : userTraceLog.getUserTraceOrInformation()){
				if (trace.getText().matches("'.* at \\(.*\\)'")){
					String function = trace.getInsert().get(0).getValue();
					function =  function.substring(1, function.length()-1);
					String relativeLine = trace.getInsert().get(1).getValue();
					relativeLine =  relativeLine.substring(1, relativeLine.indexOf('.'));
					String statement = trace.getInsert().get(2).getValue();
					statement=statement.substring(1, statement.length()-1);
					String schemaAndModuleName="";
					if (function.contains(".")) {
						schemaAndModuleName = function.substring(0, function.lastIndexOf('.')).trim();
					}
					addExecution(function, relativeLine, statement, schemaAndModuleName);
				}
			}
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

		
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
			addExecution(function, relativeLine, statement, schemaAndModuleName);
		}
	}

	private void addExecution(String function, String relativeLine, String statement, String schemaAndModuleName) {
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
			LOG.debug("Added execution data for "+function+" "+relativeLine+" "+statement);
		}
	}

	public int getModuleCount(){
		return moduleCache.size();
	}
	
	public ModuleExecutionData getModuleExecutionData(String moduleName){
		return moduleCache.get(moduleName);
	}
}
