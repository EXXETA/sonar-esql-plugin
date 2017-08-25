package com.exxeta.iss.sonar.esql.codecoverage;

import java.util.HashSet;

public class ModuleExecutionData {
	private final String schemaAndModuleName;

	private final HashSet<LineExecutionData> lineExecutions = new HashSet<>();
	public ModuleExecutionData(String schemaAndModuleName) {
		super();
		this.schemaAndModuleName = schemaAndModuleName;
	}

	public String getSchemaAndModuleName() {
		return schemaAndModuleName;
	}

	public void addExecution(String function, String relativeLine, String statement) {
		lineExecutions.add(new LineExecutionData(function, relativeLine, statement));
	}

	public int size() {
		return lineExecutions.size();
	}
	
	public HashSet<LineExecutionData> getLineExecutions() {
		return lineExecutions;
	}
}
