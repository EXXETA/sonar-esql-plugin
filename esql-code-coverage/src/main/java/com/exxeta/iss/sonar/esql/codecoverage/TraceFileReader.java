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
		try(FileInputStream fis = new FileInputStream(traceFile)){
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
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
		for (ModuleExecutionData data : moduleCache.values()){
			executionDataVisitor.visitModuleExecution(data);
		}
		
		return this;
	}

	private void readXML() {
		try {
			UserTraceLog userTraceLog = parseTraceXml();
			
			for (UserTraceType trace : userTraceLog.getUserTraceOrInformation()){
				if (trace.getFunction().endsWith("::execute") && trace.getInsert().size()>2){
					String function = trace.getInsert().get(0).getValue();
					function =  function.substring(1, function.length()-1);
					String relativeLine = trace.getInsert().get(1).getValue();
					relativeLine =  relativeLine.substring(1, relativeLine.indexOf('.'));
					String statement = trace.getInsert().get(2).getValue();
					statement=statement.substring(1, statement.length()-1);
					String schemaAndModuleName="";
					if (function.contains(".")) {
						schemaAndModuleName = function.trim();
					}
					addExecution(function, relativeLine, statement, schemaAndModuleName);
				}
			}
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}

		
	}

	protected UserTraceLog parseTraceXml() throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(UserTraceLog.class);

		Unmarshaller unmarshaller = jc.createUnmarshaller();
		UserTraceLog userTraceLog =(UserTraceLog) unmarshaller.unmarshal(traceFile);
		return userTraceLog;
	}

	private void readLine(String line) {
		Matcher matcher = pattern.matcher(line);
		if (matcher.matches()) {
			String function = matcher.group(1).trim();
			String relativeLine = matcher.group(2).trim();
			int statementBegin = line.indexOf("''") + 2;
			int statementEnd = line.lastIndexOf("''", line.indexOf(" at "));
			if (statementEnd > statementBegin){
				String statement = line.substring(statementBegin, statementEnd)
						.trim();
				String schemaAndModuleName = "";
				if (function.contains(".")) {
					schemaAndModuleName = function.substring(0, function.lastIndexOf('.')).trim();
				}
				addExecution(function, relativeLine, statement, schemaAndModuleName);
			}
		}
	}

	private void addExecution(String function, String relativeLine, String statement, String schemaAndModuleName) {
		if (!".statusACTIVE".equals(function) && !".statusINACTIVE".equals(function)) {
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
