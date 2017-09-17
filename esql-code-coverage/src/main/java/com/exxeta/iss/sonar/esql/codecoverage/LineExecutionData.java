package com.exxeta.iss.sonar.esql.codecoverage;

public class LineExecutionData {

	private final String function;
	private final String relativeLine;
	private final String statement;

	public LineExecutionData(String function, String relativeLine, String statement) {
		super();
		this.function = function;
		this.relativeLine = relativeLine;
		this.statement = statement;
	}

	public String getFunction() {
		return function;
	}

	public String getRelativeLine() {
		return relativeLine;
	}

	public String getStatement() {
		return statement;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((function == null) ? 0 : function.hashCode());
		result = prime * result + ((relativeLine == null) ? 0 : relativeLine.hashCode());
		result = prime * result + ((statement == null) ? 0 : statement.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineExecutionData other = (LineExecutionData) obj;
		if (function == null) {
			if (other.function != null)
				return false;
		} else if (!function.equals(other.function))
			return false;
		if (relativeLine == null) {
			if (other.relativeLine != null)
				return false;
		} else if (!relativeLine.equals(other.relativeLine))
			return false;
		if (statement == null) {
			if (other.statement != null)
				return false;
		} else if (!statement.equals(other.statement))
			return false;
		return true;
	}

}
